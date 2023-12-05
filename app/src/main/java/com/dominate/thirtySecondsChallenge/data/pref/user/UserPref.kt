package com.dominate.thirtySecondsChallenge.data.pref.user

import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel


interface UserPref {

    fun setSocialMediaId(fullName: String)
    fun getSocialMediaId(): String

    fun setUserName(userName: String)
    fun getUserName(): String

    fun setToken(token: String)
    fun getToken(): String

    fun setFullName(fullName: String)
    fun getFullName(): String

    fun getUser(): UserDetailsResponseModel?

    fun setUser(value: UserDetailsResponseModel?)

    fun logout()
}