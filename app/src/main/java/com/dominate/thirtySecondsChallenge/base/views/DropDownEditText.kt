package com.dominate.thirtySecondsChallenge.base.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.dominate.thirtySecondsChallenge.R
import com.google.android.material.textfield.TextInputEditText
import com.dominate.thirtySecondsChallenge.common.CommonEnums
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil

class DropDownEditText @JvmOverloads constructor(
    context: Context,
    private var attrs: AttributeSet,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    init {
        initAttrs()
    }

    private fun initAttrs() {
        val etAttr = context.obtainStyledAttributes(
            attrs,
            R.styleable.AppTextInputEditText
        )
        handleTextViewFontFamily(etAttr)

        etAttr.recycle()
    }

    private fun handleTextViewFontFamily(tvAttrs: TypedArray) {
        val selectedLanguage = CommonEnums.Languages.getLanguageByValue(
            SharedPreferencesUtil.getInstance(context).getStringPreferences(
                PrefConstants.APP_LANGUAGE_VALUE,
                CommonEnums.Languages.English.value
            )
        )

        val tvFontType =
            tvAttrs.getInt(
                R.styleable.AppTextInputEditText_etFontType,
                1
            )


    }
}