package com.dominate.thirtySecondsChallenge.ui.creategame

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.home.ActiveGameTypesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.home.HomeRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CreateGameViewModel  @ViewModelInject constructor(
    private val homeRepo: HomeRepo,
) : BaseViewModel() {

    val typeGame : MutableLiveData<Int> by lazy { MutableLiveData<Int>(1) }

    // get Active Game Types
//    val activeGameTypesResult: MutableLiveData<Result<List<ActiveGameTypesResponseModel>>> =
//        MutableLiveData()
//    val _activeGameTypesListLiveData = MutableLiveData<List<ActiveGameTypesResponseModel>>()
//    val activeGameTypesLiveData: LiveData<List<ActiveGameTypesResponseModel>> get() = _activeGameTypesListLiveData
//
//    val gameTypesEntry: MutableLiveData<List<String>> = MutableLiveData(arrayListOf())
//    val selectedGameTypePosition : MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }

//    fun getActiveGameTypes(): MutableLiveData<Result<List<ActiveGameTypesResponseModel>>> {
//        activeGameTypesResult.value = Result.Loading
//        compositeDisposable + homeRepo.getActiveGameTypes()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                if (it.succeeded) {
//                    activeGameTypesResult.value = Result.Success(it.data)
//                    _activeGameTypesListLiveData.postValue(it.data?: arrayListOf())
//                } else {
//                    activeGameTypesResult.value =
//                        Result.CustomError(
//                            errorCode = it.error?.code,
//                            message = it.error?.message
//                        )
//                }
//            }, {
//                activeGameTypesResult.value = Result.Error(it)
//            })
//        return activeGameTypesResult
//    }

}