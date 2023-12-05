package com.dominate.thirtySecondsChallenge.data.pref.user

import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants.FULLNAME
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants.SOCIALMEDIAID
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants.TOKEN
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants.USERNAME
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import com.google.gson.Gson
import javax.inject.Inject


class UserPrefImp @Inject constructor(private val prefUtil: SharedPreferencesUtil) :
    UserPref {
    override fun setSocialMediaId(socialmediaid: String) {
        prefUtil.setStringPreferences(SOCIALMEDIAID, socialmediaid)
    }

    override fun getSocialMediaId(): String {
        return prefUtil.getStringPreferences(SOCIALMEDIAID, "")
    }

    override fun setUserName(userName: String) {
        prefUtil.setStringPreferences(USERNAME, userName)
    }

    override fun getUserName(): String {
        return prefUtil.getStringPreferences(USERNAME, "")
    }

    override fun setToken(token: String) {
        prefUtil.setStringPreferences(TOKEN, token)
    }

    override fun getToken(): String {
        return prefUtil.getStringPreferences(TOKEN, "")
    }

    override fun setFullName(fullName: String) {
        prefUtil.setStringPreferences(FULLNAME, fullName)
    }

    override fun getFullName(): String {
        return prefUtil.getStringPreferences(FULLNAME, "")
    }

    override fun getUser(): UserDetailsResponseModel? {
        val gson = Gson()
        val json: String = prefUtil.getStringPreferences("user", "") ?: " "
        return gson.fromJson(json, UserDetailsResponseModel::class.java)
    }

    override fun setUser(value: UserDetailsResponseModel?) {
        val gson = Gson()
        val json = gson.toJson(value)
        prefUtil.setStringPreferences("user", json)
    }

    override fun logout() {
        setToken("")
        setSocialMediaId("")

    }


}