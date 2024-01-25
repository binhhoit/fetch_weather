package com.base.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.ViewConfiguration
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File


/**
 * Created by vophamtuananh on 1/7/18.
 */
class DeviceUtil {

    companion object {
        private const val NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height"
        private const val SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar"

        const val PERMISSION_CAMERA_REQUEST_CODE = 1001
        const val PERMISSION_READ_EXTERNAL_REQUEST_CODE = 1002
        const val PERMISSION_CALL_PHONE_REQUEST_CODE = 1003
        const val PERMISSION_WRITE_STORAGE_REQUEST_CODE = 1004
        const val PERMISSION_LOCATION_REQUEST_CODE = 1005

        const val CAMERA_REQUEST_CODE = 1011
        const val GALLERY_REQUEST_CODE = 1012
        const val GALLERY_MULTIPLE_REQUEST_CODE = 1013

        fun checkWriteStoragePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        fun checkLocationPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        fun requestLocationPermission(activity: Activity): Boolean {
            if (!checkLocationPermission(activity)) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_LOCATION_REQUEST_CODE
                )
                return false
            }
            return true
        }

        fun checkPhoneStatePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        fun checkCameraPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        private fun checkReadStoragePermision(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        private fun checkPhonePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        fun openCamera(activity: Activity, fileName: String?): String? {
            if (checkCameraPermission(activity.applicationContext)) {
                val tempFile = FileUtil.getOutputMediaFile(activity, fileName)
                tempFile?.let {
                    camera(activity, it)
                    return tempFile.absolutePath
                }
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_CAMERA_REQUEST_CODE
                )
            }
            return null
        }

        private fun camera(activity: Activity, tempFile: File) {
            val capturedFileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    activity,
                    activity.applicationContext.packageName + ".FileProvider", tempFile
                )
            } else {
                Uri.fromFile(tempFile)
            }
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePhotoIntent.resolveActivity(activity.packageManager) != null) {
                takePhotoIntent.putExtra("return-data", true)
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedFileUri)
                val chooserIntent = Intent.createChooser(takePhotoIntent, "Selection Photo")
                if (chooserIntent != null)
                    activity.startActivityForResult(chooserIntent, CAMERA_REQUEST_CODE)
            }
        }

        fun openGallery(activity: Activity, multiple: Boolean) {
            if (checkReadStoragePermision(activity.applicationContext)) {
                if (multiple) galleryMultiple(activity) else gallery(activity)
            } else {
                val requestCode =
                    if (multiple) PERMISSION_READ_EXTERNAL_REQUEST_CODE xor GALLERY_MULTIPLE_REQUEST_CODE
                    else PERMISSION_READ_EXTERNAL_REQUEST_CODE xor GALLERY_REQUEST_CODE
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    requestCode
                )
            }
        }

        private fun gallery(activity: Activity) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            if (photoPickerIntent.resolveActivity(activity.packageManager) != null) {
                photoPickerIntent.type = "image/*"
                val chooserIntent = Intent.createChooser(photoPickerIntent, "Selection Photo")
                if (chooserIntent != null)
                    activity.startActivityForResult(chooserIntent, GALLERY_REQUEST_CODE)
            }
        }

        private fun galleryMultiple(activity: Activity) {
            val photoPickerIntent = Intent()
            photoPickerIntent.type = "image/*"
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            photoPickerIntent.action = Intent.ACTION_GET_CONTENT
            if (photoPickerIntent.resolveActivity(activity.packageManager) != null) {
                val chooserIntent =
                    Intent.createChooser(photoPickerIntent, "Selection Multiple Photo")
                if (chooserIntent != null)
                    activity.startActivityForResult(chooserIntent, GALLERY_MULTIPLE_REQUEST_CODE)
            }
        }

        @SuppressLint("MissingPermission")
        fun callToPhoneNumber(activity: Activity, phoneNumber: String) {
            if (checkPhonePermission(activity.applicationContext)) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                activity.startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    PERMISSION_CALL_PHONE_REQUEST_CODE
                )
            }
        }

        fun getNavigationBarHeight(context: Context): Int {
            val res = context.resources
            val result = 0
            if (hasNavBar(context)) {
                val key: String = NAV_BAR_HEIGHT_RES_NAME
                return getInternalDimensionSize(res, key)
            }
            return result
        }

        @SuppressLint("PrivateApi")
        private fun hasNavBar(context: Context): Boolean {
            var sNavBarOverride: String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    val c = Class.forName("android.os.SystemProperties")
                    val m = c.getDeclaredMethod("get", String::class.java)
                    m.isAccessible = true
                    sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
                } catch (e: Throwable) {
                    sNavBarOverride = ""
                }
                val res = context.resources
                val resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android")
                return if (resourceId != 0) {
                    var hasNav = res.getBoolean(resourceId)
                    // check override flag (see static block)
                    if ("1" == sNavBarOverride) {
                        hasNav = false
                    } else if ("0" == sNavBarOverride) {
                        hasNav = true
                    }
                    hasNav
                } else { // fallback
                    !ViewConfiguration.get(context).hasPermanentMenuKey()
                }
            }
            return false
        }

        private fun getInternalDimensionSize(res: Resources, key: String): Int {
            var result = 0
            val resourceId = res.getIdentifier(key, "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
            return result
        }
    }
}