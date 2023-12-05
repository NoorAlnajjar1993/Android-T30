package com.dominate.thirtySecondsChallenge.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.home.request.InterestedRequest
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.DtoRegisterRequest
import com.dominate.thirtySecondsChallenge.data.model.user.request.LoginRequestModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.RegisterRequestModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.repo.auth.UserRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody.Companion.toRequestBody

class AuthViewModel @ViewModelInject constructor(
    private val testRepo: UserRepo,
    private val userPref: UserPref,
    ) : BaseViewModel() {

    val socialMediaId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val firstName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val lastName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val phone: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val xp: MutableLiveData<String> by lazy { MutableLiveData<String>("0") }
    val nextXp: MutableLiveData<String> by lazy { MutableLiveData<String>("0") }
    val levelXpResult: MutableLiveData<String> by lazy { MutableLiveData<String>("0/0") }
    val coins: MutableLiveData<String> by lazy { MutableLiveData<String>("0") }
    val diamonds: MutableLiveData<String> by lazy { MutableLiveData<String>("0") }
    val levelUpCount: MutableLiveData<String> by lazy { MutableLiveData<String>("0") }
    val referralCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val countNotification: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val loginResult: MutableLiveData<Result<UserDetailsResponseModel>> =
        MutableLiveData()

//    val listOfInterested = mutableListOf<InterestedRequest>()
    var listOfInterested: ArrayList<Int> = ArrayList()

    fun loginApi(): MutableLiveData<Result<UserDetailsResponseModel>> {
        loginResult.value = Result.Loading
        compositeDisposable + testRepo.login(
            LoginRequestModel(
                socialMediaId.value.toString()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let {it1 ->
                        loginResult.value = Result.Success(it1,it.message)
                        userPref.setToken(it1.token)
                        userPref.setUser(it1)
                    }

                } else {
                    loginResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                loginResult.value = Result.Error(it)
            })
        return loginResult
    }

    val registerResult: MutableLiveData<Result<UserDetailsResponseModel>> =
        MutableLiveData()

    fun registerApi(): MutableLiveData<Result<UserDetailsResponseModel>> {
        registerResult.value = Result.Loading
        compositeDisposable + testRepo.register(
            RegisterRequestModel(
                dto = DtoRegisterRequest(
                    socialMediaId = socialMediaId.value.toString(),
                    deviceTypeId = 2, // note 1: Web, 2: Android, 3: iOS, 4: Postman
                    firstName = firstName.value.toString(),
                    lastName = lastName.value.toString(),
                    phone = phone.value.toString(),
                    email = email.value.toString(),
                    intrests = listOfInterested,
                    ReferralCode = referralCode.value?:null
                )

            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let {it1 ->
                        registerResult.value = Result.Success(it1, it.message)
                        userPref.setToken(it1.token)
                        userPref.setUser(it1)
                        userPref.setSocialMediaId(socialMediaId.value.toString())
                        levelXpResult.value =  "${it1.nextLevelXP.toString()} / ${it1.xp.toString()}"
                        xp.value = it1.xp.toString()
                        nextXp.value = it1.nextLevelXP.toString()
                        coins.value = it1.coins.toString()
                        diamonds.value = it1.diamonds.toString()
                        levelUpCount.value = it1.level
                    }

                } else {
                    registerResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                registerResult.value = Result.Error(it)
            })
        return registerResult
    }

}