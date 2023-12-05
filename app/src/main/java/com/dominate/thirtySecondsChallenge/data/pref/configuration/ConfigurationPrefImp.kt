package com.dominate.thirtySecondsChallenge.data.pref.configuration

import com.dominate.thirtySecondsChallenge.common.CommonEnums.Languages
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants.APP_LANGUAGE_VALUE
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import javax.inject.Inject


class ConfigurationPrefImp @Inject constructor(private val prefUtil: SharedPreferencesUtil) :
    ConfigurationPref {

    override fun setAppLanguageValue(selectedLanguageValue: String) {
        prefUtil.setStringPreferences(APP_LANGUAGE_VALUE, selectedLanguageValue)
    }

    override fun getAppLanguageValue(): String {
        return prefUtil.getStringPreferences(
            APP_LANGUAGE_VALUE,
           Languages.English.value
        )
    }
}