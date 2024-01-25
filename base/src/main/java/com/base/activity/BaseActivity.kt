package com.base.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import com.base.R
import com.base.dialog.ConfirmDialog
import com.base.dialog.ConnectionErrorDialog
import com.base.dialog.InformDialog
import com.base.dialog.LoadingDialog
import com.base.dialog.UnknownErrorDialog
import com.base.exception.ManuallyException
import com.base.exception.NoConnectionException
import com.base.utils.DeviceUtil
import com.base.utils.DeviceUtil.Companion.CAMERA_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.GALLERY_MULTIPLE_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.GALLERY_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_CALL_PHONE_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_CAMERA_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_LOCATION_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_READ_EXTERNAL_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_WRITE_STORAGE_REQUEST_CODE
import com.base.viewmodel.CommonView
import retrofit2.HttpException

/**
 * Created by vophamtuananh on 1/7/18.
 */
abstract class BaseActivity : AppCompatActivity(), CommonView {

    private var mCapturedPath: String? = null
    private var mCurrentPhoneNumber: String? = null
    private var mLoadingDialog: LoadingDialog? = null
    private var mConnectionErrorDialog: ConnectionErrorDialog? = null
    private var mInformDialog: InformDialog? = null
    private var mConfirmDialog: ConfirmDialog? = null
    private var mUnknownErrorDialog: UnknownErrorDialog? = null
    protected lateinit var contextLanguage: Context

    abstract val lifecycleObserver: DefaultLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contextLanguage = this

        lifecycle.addObserver(lifecycleObserver)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (showFullScreen()) {
            makeFullscreen()
        } else if (transparentStatusBar()) {
            makeTransparentStatusBar()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mInformDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        mLoadingDialog?.dismiss()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v != null && v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
            if (showFullScreen()) {
                makeFullscreen()
            }
        }
        return try {
            super.dispatchTouchEvent(event)
        } catch (e: Exception) {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMERA_REQUEST_CODE -> {
                if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    openCamera()
                } else {
                    onRejectedCameraPermission()
                }
            }

            PERMISSION_READ_EXTERNAL_REQUEST_CODE,
            PERMISSION_READ_EXTERNAL_REQUEST_CODE xor GALLERY_REQUEST_CODE,
            PERMISSION_READ_EXTERNAL_REQUEST_CODE xor GALLERY_MULTIPLE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when (requestCode xor PERMISSION_READ_EXTERNAL_REQUEST_CODE) {
                        GALLERY_REQUEST_CODE -> openGallery()
                        GALLERY_MULTIPLE_REQUEST_CODE -> openGallery(true)
                    }
                } else {
                    onRejectedReadExternalPermission()
                }
            }

            PERMISSION_CALL_PHONE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCurrentPhoneNumber?.let { callToPhoneNumber(it) }
                } else {
                    onRejectedPhoneCallPermission()
                }
            }

            PERMISSION_WRITE_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onAgreedWriteExternal()
                } else {
                    onRejectedWriteExternalPermission()
                }
            }

            PERMISSION_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onAgreedLocationPermission()
                } else {
                    onRejectedLocationPermission()
                }
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    if (mCapturedPath != null) {
                        onCapturedImage(mCapturedPath!!)
                    }
                }

                GALLERY_REQUEST_CODE -> {
                    if (data != null) {
                        val selectedImage = data.data
                        selectedImage?.let {
                            onChoseImage(it)
                        }
                    }
                }

                GALLERY_MULTIPLE_REQUEST_CODE -> {
                    data?.clipData?.let {
                        if (it.itemCount > 0) {
                            val uris = mutableListOf<Uri>()
                            for (i in 0 until it.itemCount) {
                                uris.add(it.getItemAt(i).uri)
                            }
                            onChoseImages(uris)
                        }
                    }
                    data?.data?.let {
                        onChoseImages(mutableListOf(it))
                    }
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            onChoseNoImage()
        }
    }

    override fun showLoading(whiteBackground: Boolean, onDismiss: (() -> Unit)?, cancelable: Boolean) {
        showLoadingDialog(onDismiss, whiteBackground, cancelable)
    }

    override fun hideLoading() {
        hideLoadingDialog()
    }

    override fun showError(throwable: Throwable, tryAgainAction: (() -> Unit)?) {
        if (throwable is NoConnectionException) {
            val dialog = getConnectionErrorDialog()
            dialog.show(tryAgainAction)
        } else {
            val msg = getThrowableMessage(throwable)
            val dialog = getInformDialog()
            dialog.show(
                title = contextLanguage.getString(R.string.appname),
                informType = InformDialog.InformType.WARNING,
                description = msg,
                confirmAction = tryAgainAction
            )
        }
    }

    override fun showError(throwable: Throwable) {
        val dialog = getInformDialog()
        if (!dialog.isShowing) {
            dialog.show(
                title = contextLanguage.getString(R.string.appname),
                buttonText = contextLanguage.getString(R.string.ok),
                informType = InformDialog.InformType.WARNING,
                description = getThrowableMessage(throwable)
            )
        }
    }


    fun getThrowableMessage(e: Throwable): String {
        var msg: String? = null
        when (e) {
            is NoConnectionException -> {
                msg = contextLanguage.getString(R.string.no_network)
            }

            is ManuallyException -> {
                msg = e.message
            }

            is HttpException -> {
                msg = getHttpExceptionMessage(e)
            }
        }
        return if (TextUtils.isEmpty(msg)) contextLanguage.getString(R.string.unknown_error) else msg!!
    }

    protected open fun getHttpExceptionMessage(httpException: HttpException): String {
        return contextLanguage.getString(R.string.server_error)
    }

    private fun makeTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.statusBarColor = Color.TRANSPARENT
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.insetsController?.apply {
                show(WindowInsets.Type.statusBars())
//                systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

        } else {
            val window: Window = window
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    fun updateStatusBar(isLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetController.isAppearanceLightStatusBars = isLight
        } else {
            val decorView = window.decorView
            val flag = SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            decorView.systemUiVisibility =
                if (isLight) {
                    flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    flag
                }
        }
    }

    private fun makeFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window?.apply {
                setDecorFitsSystemWindows(false)
                insetsController?.apply {
                    hide(WindowInsets.Type.systemBars())
                    systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        } else {
            val decorView = window.decorView
            val uiOptions = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE
                    )
            decorView.systemUiVisibility = uiOptions
        }
    }

    fun openCamera(fileName: String? = null) {
        mCapturedPath = DeviceUtil.openCamera(this, fileName)
    }

    fun openGallery(multiple: Boolean = false) {
        DeviceUtil.openGallery(this, multiple)
    }

    fun callToPhoneNumber(phoneNumber: String) {
        val telMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telMgr.simState
        if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            return
        }
        mCurrentPhoneNumber = phoneNumber
        DeviceUtil.callToPhoneNumber(this, mCurrentPhoneNumber!!)
    }

    protected open fun showFullScreen(): Boolean {
        return false
    }

    protected open fun transparentStatusBar(): Boolean {
        return false
    }

    protected open fun darkActionBar(): Boolean {
        return false
    }

    protected open fun onCapturedImage(path: String) {}

    protected open fun onChoseImage(uri: Uri) {}

    protected open fun onChoseImages(uris: List<Uri>) {}

    protected open fun onChoseNoImage() {}

    protected open fun onRejectedCameraPermission() {}

    protected open fun onRejectedReadExternalPermission() {}

    protected open fun onRejectedPhoneCallPermission() {}

    protected open fun onAgreedWriteExternal() {}

    protected open fun onRejectedWriteExternalPermission() {}

    protected open fun onAgreedLocationPermission() {}

    protected open fun onRejectedLocationPermission() {}

    private fun showLoadingDialog(
        onDismiss: (() -> Unit)? = null,
        whiteBackground: Boolean = false,
        cancelable: Boolean
    ) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog()
        }

        mLoadingDialog?.showWithListener(supportFragmentManager, onDismiss, whiteBackground, cancelable)
    }

    fun hideLoadingDialog() {
        mLoadingDialog?.dismiss()
    }

    fun getConnectionErrorDialog(): ConnectionErrorDialog {
        if (mConnectionErrorDialog == null) {
            mConnectionErrorDialog = ConnectionErrorDialog(this)
        }
        return mConnectionErrorDialog!!
    }

    fun getInformDialog(): InformDialog {
        if (mInformDialog == null) {
            mInformDialog = InformDialog(this)
        }
        return mInformDialog!!
    }

    fun getConfirmDialog(): ConfirmDialog {
        if (mConfirmDialog == null) {
            mConfirmDialog = ConfirmDialog(this)
        }
        return mConfirmDialog!!
    }

    fun getUnknownErrorDialog(): UnknownErrorDialog {
        if (mUnknownErrorDialog == null) {
            mUnknownErrorDialog = UnknownErrorDialog(this)
        }
        return mUnknownErrorDialog!!
    }
}
