package com.dominate.thirtySecondsChallenge.base.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.dominate.thirtySecondsChallenge.R
import com.google.android.material.button.MaterialButton
import com.dominate.thirtySecondsChallenge.common.CommonEnums
import com.dominate.thirtySecondsChallenge.utils.anim.AnimationHelper
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil


class AppButton @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {



    init {
        initAttrs()
    }

    private fun initAttrs() {
        val btnAttr = context.obtainStyledAttributes(
            attrs,
            R.styleable.AppButton
        )
        handleTextViewFontFamily(btnAttr)
        AnimationHelper(this,context)

        btnAttr.recycle()
    }

    private fun handleTextViewFontFamily(tvAttrs: TypedArray) {
        val selectedLanguage = CommonEnums.Languages.getLanguageByValue(
            SharedPreferencesUtil.getInstance(context).getStringPreferences(
                PrefConstants.APP_LANGUAGE_VALUE,
                CommonEnums.Languages.English.value
            )
        )

        val btnFontType =
            tvAttrs.getInt(R.styleable.AppButton_btnFontType, 2)

        val typeface: Typeface?

        when (btnFontType) {
            1 -> { // regular
                typeface = ResourcesCompat.getFont(
                    context,
                    if (selectedLanguage == CommonEnums.Languages.Arabic) R.font.ar_regular_font else R.font.en_regular_font
                )
            }
            2 -> { // MEDIUM
                typeface = ResourcesCompat.getFont(
                    context,
                    if (selectedLanguage == CommonEnums.Languages.Arabic) R.font.ar_medium_font else R.font.en_medium_font
                )
            }
            3 -> { // SEMI_BOLD
                typeface = ResourcesCompat.getFont(
                    context,
                    if (selectedLanguage == CommonEnums.Languages.Arabic) R.font.ar_medium_font else R.font.en_medium_font
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

}