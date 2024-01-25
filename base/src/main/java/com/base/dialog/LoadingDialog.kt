package com.base.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.base.R
import com.base.databinding.DialogLoadingBinding
import com.base.utils.getColorWithAlpha

/**
 * Created by vophamtuananh on 1/7/18.
 */
class LoadingDialog : CustomDimDialogFragment<DialogLoadingBinding>() {

    private var isWhiteBackground = false

    private var mOnDismissListener: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun dismiss() {
        mOnDismissListener?.invoke()
        super.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(isWhiteBackground) {
            binding.background.background =  ColorDrawable(Color.WHITE)
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), theme)
        if(isWhiteBackground) {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } else {
            dialog.window?.setBackgroundDrawable(ColorDrawable(getColorWithAlpha(Color.BLACK, 0.15f)))
        }
        dialog.window?.attributes?.windowAnimations = R.style.DialogTheme
        return dialog
    }

    fun showWithListener(fm: FragmentManager, onDismiss: (() -> Unit)? = null, whiteBackground: Boolean = false, cancelable: Boolean = false) {
        isWhiteBackground = whiteBackground
        mOnDismissListener = onDismiss
        isCancelable = cancelable
        super.show(fm, this.tag)
    }
}