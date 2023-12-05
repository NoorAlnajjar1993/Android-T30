package com.dominate.thirtySecondsChallenge.common

import androidx.annotation.StringRes
import com.dominate.thirtySecondsChallenge.R


interface CommonEnums {

    enum class Languages(val value: String, @StringRes val languageName: Int) {
        English("en", R.string.english),
        Arabic("ar", R.string.arabic);


        companion object {
            fun getLanguageByValue(value: String): Languages {
                return when (value) {
                    "en" -> English
                    else -> Arabic
                }
            }
        }
    }

}