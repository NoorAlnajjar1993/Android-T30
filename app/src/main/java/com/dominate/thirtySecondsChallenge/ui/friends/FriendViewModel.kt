package com.dominate.thirtySecondsChallenge.ui.friends

import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.friends.AcceptRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.PaginationResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.ReferralResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.RejectRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UnfriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserBlockResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.leader.LeaderboardFiltersModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.dominate.thirtySecondsChallenge.data.repo.friends.FriendsRepo
import com.dominate.thirtySecondsChallenge.data.repo.leader.LeaderRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.ui.friends.model.FriendModel
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody.Companion.toRequestBody

class FriendViewModel @ViewModelInject constructor(
    private val friendsRepo: FriendsRepo,
    private val leaderRepo: LeaderRepo,
) : BaseViewModel() {


    val isEmptyListFriends : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isEmptyRequestFriends : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    val countFriendsRequest: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val _isRemoveFriendsLiveData = MutableLiveData<Boolean>(false)
    val isRemoveFriendsLiveData: LiveData<Boolean> get() = _isRemoveFriendsLiveData

    val _friendModelListLiveData = MutableLiveData<List<FriendModel>>()
    val friendModelListLiveData: LiveData<List<FriendModel>> get() = _friendModelListLiveData

    val _isInvite = MutableLiveData<Boolean>(false)
    val isInvite: LiveData<Boolean> get() = _isInvite

    // get All User Friends
    val getAllUserFriendsResult: MutableLiveData<Result<List<UserFriendsResponseModel>>> =
        MutableLiveData()

    val _allUserFriendsLiveData = MutableLiveData<List<UserFriendsResponseModel>>()
    val allUserFriendsLiveData: LiveData<List<UserFriendsResponseModel>> get() = _allUserFriendsLiveData

    fun getAllUserFriends(): MutableLiveData<Result<List<UserFriendsResponseModel>>> {
        getAllUserFriendsResult.value = Result.Loading
        compositeDisposable + friendsRepo.getAllUserFriends()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    getAllUserFriendsResult.value = Result.Success(it.data,it.message)
                    _allUserFriendsLiveData.value = it.data?: arrayListOf()
                } else {
                    getAllUserFriendsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                getAllUserFriendsResult.value = Result.Error(it)
            })
        return getAllUserFriendsResult
    }

    // get All User Friends
    val allRequestResult: MutableLiveData<Result<List<UserFriendsResponseModel>>> =
        MutableLiveData()

    val _allRequestLiveData = MutableLiveData<List<UserFriendsResponseModel>>()
    val allRequestLiveData: LiveData<List<UserFriendsResponseModel>> get() = _allRequestLiveData

    fun getAllRequest(): MutableLiveData<Result<List<UserFriendsResponseModel>>> {
        allRequestResult.value = Result.Loading
        compositeDisposable + friendsRepo.getAllRequest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    allRequestResult.value = Result.Success(it.data,it.message)
                    _allRequestLiveData.value = it.data?: arrayListOf()

                    if (!it.data.isNullOrEmpty()){
                        countFriendsRequest.value = it.data.count().toString()
                    }else{
                        countFriendsRequest.value = null
                    }

                } else {
                    allRequestResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                allRequestResult.value = Result.Error(it)
            })
        return allRequestResult
    }

    // accept Request
    val acceptRequestResult: MutableLiveData<Result<List<UserFriendsResponseModel>>> =
        MutableLiveData()

    fun acceptRequest(id: Int): MutableLiveData<Result<List<UserFriendsResponseModel>>> {
        acceptRequestResult.value = Result.Loading
        compositeDisposable + friendsRepo.acceptRequest(
            AcceptRequestModel(
                userFriendId = id
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    acceptRequestResult.value = Result.Success(it.data, it.message)
                    if (!it.data.isNullOrEmpty()){
                        countFriendsRequest.value = it.data.count().toString()
                    }else{
                        countFriendsRequest.value = null
                    }

                } else {
                    acceptRequestResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                acceptRequestResult.value = Result.Error(it)
            })
        return acceptRequestResult
    }

    // reject Request
    val rejectRequestResult: MutableLiveData<Result<List<UserFriendsResponseModel>>> =
        MutableLiveData()

    fun rejectRequest(id: Int): MutableLiveData<Result<List<UserFriendsResponseModel>>> {
        rejectRequestResult.value = Result.Loading
        compositeDisposable + friendsRepo.rejectRequest(
            RejectRequestModel(
                userFriendId = id
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    rejectRequestResult.value = Result.Success(it.data, it.message)
                    if (!it.data.isNullOrEmpty()){
                        countFriendsRequest.value = it.data.count().toString()
                    }else{
                        countFriendsRequest.value = null
                    }

                } else {
                    rejectRequestResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                rejectRequestResult.value = Result.Error(it)
            })
        return rejectRequestResult
    }

    // unfriend Request
    val unfriendRequestResult: MutableLiveData<Result<List<UserFriendsResponseModel>>> =
        MutableLiveData()

    fun unfriendRequest(id: Int): MutableLiveData<Result<List<UserFriendsResponseModel>>> {
        unfriendRequestResult.value = Result.Loading
        compositeDisposable + friendsRepo.unfriendRequest(
            UnfriendRequestModel(
                userFriendId = id
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    unfriendRequestResult.value = Result.Success(it.data,it.message)
                    _allUserFriendsLiveData.value = it.data?: arrayListOf()
                } else {
                    unfriendRequestResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                unfriendRequestResult.value = Result.Error(it)
            })
        return unfriendRequestResult
    }

    // block Request
    val userBlockResult: MutableLiveData<Result<List<UserBlockResponseModel>>> =
        MutableLiveData()

    fun userBlock(id: Int): MutableLiveData<Result<List<UserBlockResponseModel>>> {
        userBlockResult.value = Result.Loading
        compositeDisposable + friendsRepo.userBlock(
            id = id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    userBlockResult.value = Result.Success(it.data,it.message)
                } else {
                    userBlockResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                userBlockResult.value = Result.Error(it)
            })
        return userBlockResult
    }

    // get Leaderboard Filters
    val leaderboardFiltersResult: MutableLiveData<Result<List<LeaderboardFiltersModel>>> =
        MutableLiveData()

    val leaderboardFiltersEntry: ArrayList<String> = ArrayList(arrayListOf())
    val selectedPosition: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
    val leaderboardFilters = ArrayList<LeaderboardFiltersModel>()

    fun getLeaderboardFilters(): MutableLiveData<Result<List<LeaderboardFiltersModel>>> {
        leaderboardFiltersResult.value = Result.Loading
        compositeDisposable + leaderRepo.getLeaderboardFilters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    leaderboardFiltersResult.value = Result.Success(it.data,it.message)
                } else {
                    leaderboardFiltersResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                leaderboardFiltersResult.value = Result.Error(it)
            })
        return leaderboardFiltersResult
    }

    // get Leaderboard
    val leaderboardResult: MutableLiveData<Result<List<UserInfoModel>>> =
        MutableLiveData()

    fun getLeaderboard(id: Int): MutableLiveData<Result<List<UserInfoModel>>> {
        leaderboardResult.value = Result.Loading
        compositeDisposable + leaderRepo.getLeaderboard(
            id = id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    leaderboardResult.value = Result.Success(it.data,it.message)
                } else {
                    leaderboardResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                leaderboardResult.value = Result.Error(it)
            })
        return leaderboardResult
    }

    // get Referral

    val referralCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val referralResult: MutableLiveData<Result<ReferralResponseModel>> =
        MutableLiveData()

    fun getReferral(): MutableLiveData<Result<ReferralResponseModel>> {
        referralResult.value = Result.Loading
        compositeDisposable + friendsRepo.referral()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    referralResult.value = Result.Success(it.data,it.message)
                } else {
                    referralResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                referralResult.value = Result.Error(it)
            })
        return referralResult
    }

}