package com.fetch.weather.ui.feature.widgets

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import com.base.utils.RippleUtil
import com.fetch.weather.R

class AppButton : AppCompatButton {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val normalColor = resources.getColor(R.color.colorPrimary, null)
        val rippleColor = resources.getColor(R.color.colorPrimaryHover, null)
        val cornerRadius = resources.getDimension(com.base.R.dimen._4sdp)
        val disableColor = resources.getColor(R.color.colorPrimaryLight, null)

        setTextAppearance(R.style.AppButtonTextAppearance)

        gravity = Gravity.CENTER

        background =
            RippleUtil.getRippleStateDrawable(normalColor, disableColor, rippleColor, cornerRadius,
                cornerRadius, cornerRadius,
                cornerRadius, cornerRadius)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(layoutParams.width == LayoutParams.WRAP_CONTENT) {
            minWidth = resources.getDimension(com.base.R.dimen._327sdp).toInt()
        }
        if(layoutParams.height == LayoutParams.WRAP_CONTENT) {
            minHeight = resources.getDimension(com.base.R.dimen._56sdp).toInt()
        }
    }
}