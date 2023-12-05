package com.dominate.thirtySecondsChallenge.data.pref.configuration


interface ConfigurationPref {

    fun setAppLanguageValue(selectedLanguageValue: String)
    fun getAppLanguageValue():String
}