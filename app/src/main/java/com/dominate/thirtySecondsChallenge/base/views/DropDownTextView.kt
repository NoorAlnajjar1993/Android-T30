package com.dominate.thirtySecondsChallenge.base.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import com.dominate.thirtySecondsChallenge.R
import com.google.android.material.textview.MaterialTextView
import com.dominate.thirtySecondsChallenge.common.CommonEnums
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil

class DropDownTextView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = android.R.attr.textViewStyle,
    private val defStyleRe: Int = 0
) : MaterialTextView(
    context, attrs, defStyleAttr, defStyleRe
) {


    init {
        initAttrs()
    }

    private fun initAttrs() {
        val tvAttrs = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AppTextView,
            defStyleAttr,
            defStyleRe
        )
        handleTextViewFontFamily(tvAttrs)
        handleTextViewUnderline(tvAttrs)
        handleTextViewIgnoreLocalization(tvAttrs)
    }

    @SuppressLint("RtlHardcoded")
    private fun handleTextViewFontFamily(tvAttrs: TypedArray) {
        val selectedLanguage = CommonEnums.Languages.getLanguageByValue(
            SharedPreferencesUtil.getInstance(context)
                .getStringPreferences(
                    PrefConstants.APP_LANGUAGE_VALUE,
                    CommonEnums.Languages.English.value
                )
        )

        val tvFontType =
            tvAttrs.getInt(R.styleable.AppTextView_tvFontType, 1)


    }

    private fun handleTextViewUnderline(tvAttrs: TypedArray) {
        val tvUnderline =
            tvAttrs.getBoolean(R.styleable.AppTextView_tvUnderline, false)

        if (tvUnderline) paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }


    @SuppressLint("RtlHardcoded")
    private fun handleTextViewIgnoreLocalization(tvAttrs: TypedArray) {
        val tvIgnoreLocalization =
            tvAttrs.getBoolean(R.styleable.AppTextView_tvIgnoreLocalization, true)
        if (tvIgnoreLocalization) return

        val selectedLanguage = CommonEnums.Languages.getLanguageByValue(
            SharedPreferencesUtil.getInstance(context)
                .getStringPreferences(
                    PrefConstants.APP_LANGUAGE_VALUE,
                    CommonEnums.Languages.English.value
                )
        )
        gravity = if (selectedLanguage == CommonEnums.Languages.English) { // TODO SORRY
            Gravity.LEFT
        } else {
            Gravity.RIGHT
        }
    }

}