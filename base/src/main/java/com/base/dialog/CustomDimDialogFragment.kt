package com.base.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.base.R
import com.base.utils.getColorWithAlpha

abstract class CustomDimDialogFragment<T : ViewDataBinding> : AppCompatDialogFragment() {

    protected lateinit var binding: T

    private var isDialogShow = false
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun getTheme(): Int {
        return R.style.FullscreenDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), theme)
        dialog.window?.setBackgroundDrawable(ColorDrawable(getColorWithAlpha(Color.BLACK, 0.15f)))
        dialog.window?.attributes?.windowAnimations = R.style.DialogTheme
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        isDialogShow = false
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isDialogShow)
            return

        isDialogShow = true

        super.show(manager, tag)
    }

    override fun dismiss() {
        super.dismiss()
        if (isDialogShow) {
            isDialogShow = false
            dialog?.setOnCancelListener(null)
        }
    }
}