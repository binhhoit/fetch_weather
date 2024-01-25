package com.fetch.weather.ui.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("visibility")
fun View.setVisibility(value: Boolean) {
    visibility = if (value) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("showError")
fun TextInputLayout.updateError(value: String?) {
    isErrorEnabled = value != null
    error = value
}