package com.fetch.weather.ui.feature.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_CUSTOM
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.fetch.weather.R

/** How to use:
    <com.milwaukee.loyalty.ui.widgets.AppEditText
    android:id="@+id/edt_password"
    android:layout_width="match_parent"
    android:layout_height="@dimen/_62sdp"
    android:layout_marginHorizontal="@dimen/_24sdp"
    android:layout_marginTop="@dimen/_18sdp"
    app:endIconDrawable="@drawable/ic_password_toggle"
    app:endIconDrawableSecond="@drawable/ic_biometric"
    app:hint="@string/login_password"
    app:isPassword="true"
    app:label="@string/login_password"
    app:labelSecond="@string/account_verify_email_des"
    app:labelStar="true" />
* */


class AppEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var inputTextLayout : TextInputLayout
    private var textInputEditText : TextInputEditText
    private var tvLabelFirst : TextView
    private var tvLabelSecond : TextView
    private var tvLabelStar : TextView
    private var ivIconEndSecond : ImageView

    private var hint: String
    private var label: String?
    private var labelSecond: String?
    private var labelStar: Boolean
    private var iconEnd: Drawable?
    private var tintColorIconEnd: Int
    private var iconEndSecond: Drawable?
    private var isPassword: Boolean
    private var isNumber: Boolean
    private var paddingEnd: Int
    private var paddingStart: Int
    private var focusable: Boolean

    var text: String?
        get() {
            return textInputEditText.text.toString()
        }
        set(value) {
            if (value != null)
                textInputEditText.setText(value)
        }

    init {
        val view = inflate(context, R.layout.layout_app_edit_text, this)

        context.obtainStyledAttributes(
            attrs,
            R.styleable.AppEditText,
            defStyleAttr, 0).apply {
            try {
                hint = getString(R.styleable.AppEditText_hint)?:""
                label = getString(R.styleable.AppEditText_label)
                labelSecond = getString(R.styleable.AppEditText_labelSecond)
                labelStar = getBoolean(R.styleable.AppEditText_labelStar,false)
                text = getString(R.styleable.AppEditText_text)
                iconEnd = getDrawable(R.styleable.AppEditText_endIconDrawable)
                iconEndSecond = getDrawable(R.styleable.AppEditText_endIconDrawableSecond)
                isPassword = getBoolean(R.styleable.AppEditText_isPassword, false)
                isNumber = getBoolean(R.styleable.AppEditText_isNumber, false)
                paddingEnd = getDimensionPixelOffset(R.styleable.AppEditText_paddingEnd,0)
                paddingStart = getDimensionPixelOffset(R.styleable.AppEditText_paddingStart,0)
                tintColorIconEnd = getColor(R.styleable.AppEditText_tintEndIcon,0)
                focusable = getBoolean(R.styleable.AppEditText_focusable,true)
            } finally {
                recycle()
            }
        }

        layoutParams = MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        view.apply {
            inputTextLayout = findViewById(R.id.text_input_layout)
            textInputEditText = findViewById(R.id.text_input_edit_text)
            tvLabelFirst = findViewById(R.id.tv_label_one)
            tvLabelSecond = findViewById(R.id.tv_label_second)
            tvLabelStar = findViewById(R.id.tv_label_star)
            ivIconEndSecond = findViewById(R.id.iv_icon_end_second)
        }

        textInputEditText.hint = hint
        if (label != null) {
            tvLabelFirst.text = label
        } else {
            inputTextLayout.isHintEnabled = false
            textInputEditText.gravity = Gravity.CENTER_VERTICAL
        }
        tvLabelStar.isVisible = labelStar
        labelSecond?.let {
            tvLabelSecond.text = buildString {
                append('(')
                append(it)
                append(')')
            }
        }

        if (isPassword) {
            textInputEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        } else if (isNumber) {
            textInputEditText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        iconEnd?.let {
            inputTextLayout.endIconMode =
                if (isPassword) END_ICON_PASSWORD_TOGGLE else END_ICON_CUSTOM
            inputTextLayout.endIconDrawable = it
            if (tintColorIconEnd != 0)
                inputTextLayout.setEndIconTintList(ColorStateList.valueOf(tintColorIconEnd))
        }

        iconEndSecond?.let {
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 60, 0)
                textInputEditText.layoutParams = this
            }
            ivIconEndSecond.setImageDrawable(iconEndSecond)
        }

        if (paddingEnd > 0)
            inputTextLayout.setPadding(0, 0, paddingEnd, 0)
        if (paddingStart > 0)
            inputTextLayout.setPadding(paddingStart, 0, 0, 0)

        if(!focusable){
            textInputEditText.inputType = InputType.TYPE_NULL
            textInputEditText.isFocusable = false
            inputTextLayout.isFocusable = false
        }
    }

    fun addTextChangedListener(afterChange: (CharSequence?) -> Unit) =
        textInputEditText.addTextChangedListener(afterTextChanged = afterChange)

    fun setOnClickIconEndSecond(l: OnClickListener){
        ivIconEndSecond.setOnClickListener(l)
    }

    fun setOnClickIconEnd(l: OnClickListener) {
        inputTextLayout.setEndIconOnClickListener(l)
    }

    fun setVisibleSecondIcon(isVisible: Boolean) {
        ivIconEndSecond.isVisible = isVisible
        ivIconEndSecond.isClickable = isVisible
    }

    fun setOnClickListenerEdittext(l: OnClickListener) {
        inputTextLayout.setOnClickListener(l)
        textInputEditText.setOnClickListener(l)
    }

    fun hint(string: String) {
        textInputEditText.hint = string
    }
}