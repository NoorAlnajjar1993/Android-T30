package com.dominate.thirtySecondsChallenge.base.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.res.ResourcesCompat
import com.dominate.thirtySecondsChallenge.R
import com.google.android.material.radiobutton.MaterialRadioButton
import com.dominate.thirtySecondsChallenge.common.CommonEnums
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil


class AppRadioButton @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet,
    defStyleAttr: Int = android.R.attr.radioButtonStyle
) : MaterialRadioButton(context, attrs, defStyleAttr) {

    init {
        initAttrs()
    }

    private fun initAttrs() {
        val btnAttr = context.obtainStyledAttributes(
            attrs,
            R.styleable.AppRadioButton
        )
        handleTextViewFontFamily(btnAttr)
        handleTextViewIgnoreLocalization(btnAttr)
        btnAttr.recycle()
    }

    private fun handleTextViewFontFamily(tvAttrs: TypedArray) {
        val selectedLanguage = CommonEnums.Languages.getLanguageByValue(
            SharedPreferencesUtil.getInstance(context).getStringPreferences(
                PrefConstants.APP_LANGUAGE_VALUE,
                CommonEnums.Languages.English.value
            )
        )

        val rbFontType =
            tvAttrs.getInt(R.styleable.AppRadioButton_rbFontType, 1)

        val typeface: Typeface?

        when (rbFontType) {
            1 -> { // regular
                typeface = ResourcesCompat.getFont(
                    context,
                    if (selectedLanguage == CommonEnums.Languages.Arabic) R.font.ar_regular_font else R.font.en_regular_font
                )
            }
            2 -> { // MEDIUM
                typeface = ResourcesCompat.getFont(
                    context,
                    if (selectedLanguage == CommonEnums.Languages.Arabic) R.font.ar_medium_font else R.font.en_med_font
                )
            }
            3 -> { // SEMI_BOLD
                typeface = ResourcesCompat.getFont(
                    context,
                    if (selectedLanguage == CommonEnums.Languages.Arabic) R.font.ar_semibold_font else R.font.en_medium_font
                )
            }
            4 -> { // BOLD
                typeface = ResourcesCompat.getFont(
                    context,
                    if (selectedLanguage == CommonEnums.Languages.Arabic) R.font.ar_bold_font else R.font.en_bold_font
                )
            }
            else -> { // Light
                typeface = ResourcesCompat.getFont(
                    context,
                    if (selectedLanguage == CommonEnums.Languages.Arabic) R.font.ar_light_font else R.font.en_light_font
                )
            }
        }

        this.typeface = typeface
    }

    @SuppressLint("RtlHardcoded")
    private fun handleTextViewIgnoreLocalization(tvAttrs: TypedArray) {
        val tvIgnoreLocalization =
            tvAttrs.getBoolean(R.styleable.AppTextView_tvIgnoreLocalization, true)
        if(tvIgnoreLocalization) return

        val selectedLanguage = CommonEnums.Languages.getLanguageByValue(
            SharedPreferencesUtil.getInstance(context).getStringPreferences(
                PrefConstants.APP_LANGUAGE_VALUE,
                CommonEnums.Languages.English.value
            )
        )
        gravity = if(selectedLanguage  == CommonEnums.Languages.English){ // TODO SORRY
            Gravity.LEFT
        }else{
            Gravity.RIGHT
        }
    }

}