package com.dominate.thirtySecondsChallenge.ui.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.LoginRequestModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.repo.auth.UserRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.ui.auth.AuthFragment
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody.Companion.toRequestBody

class MainViewModel @ViewModelInject constructor(
    private val testRepo: UserRepo,
    private val userPref: UserPref,
) : BaseViewModel() {

    val firstName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val lastName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val phone: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val loginResult: MutableLiveData<Result<UserDetailsResponseModel>> =
        MutableLiveData()

    fun loginApi(): MutableLiveData<Result<UserDetailsResponseModel>> {
        loginResult.value = Result.Loading
        compositeDisposable + testRepo.login(
            LoginRequestModel(
                userPref.getSocialMediaId()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    try {
                        it.data?.let {it1 ->
                            loginResult.value = Result.Success(it1, it.message)
                        }
                    }catch (e:Exception){

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

}