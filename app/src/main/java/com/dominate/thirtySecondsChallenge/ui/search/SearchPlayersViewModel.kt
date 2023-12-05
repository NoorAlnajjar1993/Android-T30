package com.dominate.thirtySecondsChallenge.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.friends.PaginationResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.friends.FriendsRepo
import com.dominate.thirtySecondsChallenge.data.repo.leader.LeaderRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchPlayersViewModel @ViewModelInject constructor(
    private val friendsRepo: FriendsRepo,
) : BaseViewModel() {


    val isEmptyList : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }

    // pagination
    val paginationResult: MutableLiveData<Result<PaginationResponseModel>> =
        MutableLiveData()

    fun getPagination(page:Int,query: String): MutableLiveData<Result<PaginationResponseModel>> {
        paginationResult.value = Result.Loading
        compositeDisposable + friendsRepo.getPagination(
            page = page,
            query = query
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    paginationResult.value = Result.Success(it.data, it.message)
                } else {
                    paginationResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                paginationResult.value = Result.Error(it)
            })
        return paginationResult
    }


}