package com.dominate.thirtySecondsChallenge.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.home.request.AddFirebaseTokenRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveInterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.repo.home.HomeRepo
import com.dominate.thirtySecondsChallenge.data.repo.profile.ProfileRepo
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.dominate.thirtySecondsChallenge.data.response.Result

class SplashViewModel @ViewModelInject constructor(
    private val profileRepo: ProfileRepo,
    private val userPref: UserPref,
    private val homeRepo: HomeRepo,

): BaseViewModel() {

    val languageName : MutableLiveData<Int> by lazy { MutableLiveData<Int>(77) }
    val btntitle : MutableLiveData<String> by lazy { MutableLiveData<String>("ابدأ") }

    val isVisible : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val hasStartedSoundPlay : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    val tokenFireBase : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    // on boarding step three
    val activeInterestsResult: MutableLiveData<Result<List<ActiveInterestsResponseModel>>> =
        MutableLiveData()
    fun getActiveInterests(): MutableLiveData<Result<List<ActiveInterestsResponseModel>>> {
        activeInterestsResult.value = Result.Loading
        compositeDisposable + profileRepo.getActiveInterests()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let {it1->
                        activeInterestsResult.value = Result.Success(it1 , it.message)
                    }

                } else {
                    activeInterestsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                activeInterestsResult.value = Result.Error(it)
            })
        return activeInterestsResult
    }

    // add token device firebase
    val addTokenFirebaseResult: MutableLiveData<Result<Boolean>> =
        MutableLiveData()

    fun addTokenFirebaseResult(): MutableLiveData<Result<Boolean>> {
        addTokenFirebaseResult.value = Result.Loading
        compositeDisposable + homeRepo.addFirebaseToken(
            AddFirebaseTokenRequestModel(
                userPlayerId= userPref.getUser()?.id,
                firebaseToken= tokenFireBase.value.toString()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let {it1->
                        addTokenFirebaseResult.value = Result.Success(it1 , it.message)
                    }

                } else {
                    addTokenFirebaseResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                addTokenFirebaseResult.value = Result.Error(it)
            })
        return addTokenFirebaseResult
    }

}