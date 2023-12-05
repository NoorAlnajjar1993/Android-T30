package com.dominate.thirtySecondsChallenge.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.home.ActiveGameTypesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.AdsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.BannersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.HomeResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.home.HomeRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.iv_banners

class HomeViewModel @ViewModelInject constructor(
    private val homeRepo: HomeRepo,
) : BaseViewModel() {

    val imageBanner : MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val urlBanner : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val isBannersClickable : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val adsList = mutableListOf<AdsResponseModel>()

    val bannersResult: MutableLiveData<Result<HomeResponseModel>> =
        MutableLiveData()

    val activeGameTypesResult: MutableLiveData<Result<List<ActiveGameTypesResponseModel>>> =
        MutableLiveData()
    val _activeGameTypesListLiveData = MutableLiveData<List<ActiveGameTypesResponseModel>>()
    val activeGameTypesLiveData: LiveData<List<ActiveGameTypesResponseModel>> get() = _activeGameTypesListLiveData

    val gameTypesEntry: MutableLiveData<List<String>> = MutableLiveData(arrayListOf())
    val selectedGameTypePosition : MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }

    val _adsListLiveData = MutableLiveData<List<AdsResponseModel>>()
    val adsLiveData: LiveData<List<AdsResponseModel>> get() = _adsListLiveData

    val isGetHome : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }


    fun getHome(): MutableLiveData<Result<HomeResponseModel>> {
        bannersResult.value = Result.Loading
        compositeDisposable + homeRepo.getHome()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    it.data?.let {it1 ->
                        bannersResult.value = Result.Success(it1,it.message)
                        imageBanner.value = it1.banners[0].backgroundImageUrl
                        urlBanner.value = it1.banners[0].redirectUrl
                        adsList.addAll(it1.ads)

                        _adsListLiveData.value = it1.ads
                        activeGameTypesResult.value = Result.Success(it1.gameTypes,it.message)
                        _activeGameTypesListLiveData.postValue(it1.gameTypes)
                    }

                    if (urlBanner.value == null){
                        isBannersClickable.value = false
                    }else if (!urlBanner.value?.contains("http")!!){
                        isBannersClickable.value = false
                    }else{
                        isBannersClickable.value = !urlBanner.value.isNullOrEmpty()
                    }

                    isGetHome.value = false

                } else {
                    bannersResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                    isGetHome.value = true

                }
            }, {
                isGetHome.value = true
                bannersResult.value = Result.Error(it)
            })
        return bannersResult
    }

}