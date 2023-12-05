package com.dominate.thirtySecondsChallenge.ui.player

import android.annotation.SuppressLint
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.friends.DeleteFriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.SendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UnfriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.gift.request.DtoSendGiftsModel
import com.dominate.thirtySecondsChallenge.data.model.gift.request.SendGiftsRequestModel
import com.dominate.thirtySecondsChallenge.data.model.player.FriendshipStatusResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.request.DtoReportPlayerModel
import com.dominate.thirtySecondsChallenge.data.model.player.request.ReportPlayerAddRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.repo.auth.UserRepo
import com.dominate.thirtySecondsChallenge.data.repo.friends.FriendsRepo
import com.dominate.thirtySecondsChallenge.data.repo.gift.GiftRepo
import com.dominate.thirtySecondsChallenge.data.repo.player.PlayerRepo
import com.dominate.thirtySecondsChallenge.data.repo.profile.ProfileRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest.JsonFriendsRequestResponse
import com.dominate.thirtySecondsChallenge.data.signalR.gift.JsonDataSendGiftRequest
import com.dominate.thirtySecondsChallenge.data.signalR.joingame.ArgumentJoinGame
import com.dominate.thirtySecondsChallenge.data.signalR.joingame.IsReadyJsonDataRequest
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

class PlayerViewModel @ViewModelInject constructor(
    private val userRepo: UserRepo,
    private val playerRepo: PlayerRepo,
    private val friendsRepo: FriendsRepo,
    private val giftRepo: GiftRepo,
    private val userPref: UserPref,
) : BaseViewModel() {

    val statusPlayer: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val statusPlayerId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    val playerNameId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val playerName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val firstName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val lastName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val personId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userLevelTitle: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val phone: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val xp: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val coins: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val imagePerson: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val imagePersonBackground: MutableLiveData<String> by lazy { MutableLiveData<String>() }


    val _isSendGiftLiveData = MutableLiveData<Boolean>(false)
    val isSendGiftLiveData: LiveData<Boolean> get() = _isSendGiftLiveData


    // get player info
    val playerResult: MutableLiveData<Result<ProfileResponseModel>> =
        MutableLiveData()

    fun getPlayerInfo(id: Int): MutableLiveData<Result<ProfileResponseModel>> {
        playerResult.value = Result.Loading
        compositeDisposable + playerRepo.getPlayerProfile(
            id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    playerResult.value = Result.Success(it.data, it.message)
                    it.data?.userInfo?.let {
                        playerName.value = "${it.firstName} ${it.lastName}"
                        firstName.value = if (it.firstName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.firstName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        lastName.value = if (it.lastName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.lastName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        personId.value = it.id.toString()
                        userLevelTitle.value = it.userLevelTypeTitle
                        phone.value = if (it.phone.isNullOrEmpty()) {
                            ""
                        } else {
                            it.phone.takeIf { it.isNotEmpty() } ?: ""
                        }
                        email.value = it.email.takeIf { it.isNotEmpty() } ?: ""
                        xp.value = it.xp.toString()
                        coins.value = it.coins.toString()
                        imagePerson.value = it.profileImageUrl
                        imagePersonBackground.value = it.backgroundImageUrl
                    }

                } else {
                    playerResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                playerResult.value = Result.Error(it)
            })
        return playerResult
    }

    // get Report
    val reportReasonResult: MutableLiveData<Result<List<ReportReasonResponseModel>>> =
        MutableLiveData()

    val _reportListLiveData = MutableLiveData<List<ReportReasonResponseModel>>()
    val reportListLiveData: LiveData<List<ReportReasonResponseModel>> get() = _reportListLiveData

    fun getReportReason(): MutableLiveData<Result<List<ReportReasonResponseModel>>> {
        reportReasonResult.value = Result.Loading
        compositeDisposable + playerRepo.getReportReason()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    reportReasonResult.value = Result.Success(it.data, it.message)
                } else {
                    reportReasonResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                reportReasonResult.value = Result.Error(it)
            })
        return reportReasonResult
    }

    // get gift

    val giftsResult: MutableLiveData<Result<List<GiftsResponseModel>>> =
        MutableLiveData()

    val giftsData: MutableLiveData<List<GiftsResponseModel>> =
        MutableLiveData()

    fun getAllGifts(): MutableLiveData<Result<List<GiftsResponseModel>>> {
        giftsResult.value = Result.Loading
        compositeDisposable + giftRepo.getAllGifts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    giftsResult.value = Result.Success(it.data, it.message)
                    giftsData.value = it.data
                } else {
                    giftsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                giftsResult.value = Result.Error(it)
            })
        return giftsResult
    }

    // send Gifts
    val giftSelectedId: MutableLiveData<Int> by lazy { MutableLiveData<Int>(-1) }
    val sendGiftsResult: MutableLiveData<Result<List<GiftsResponseModel>>> =
        MutableLiveData()

    fun sendGifts(): MutableLiveData<Result<List<GiftsResponseModel>>> {
        sendGiftsResult.value = Result.Loading
        compositeDisposable + giftRepo.sendGifts(
            SendGiftsRequestModel(
                dto = DtoSendGiftsModel(
                    userPlayerId = playerNameId.value ?: -1,
                    giftId = giftSelectedId.value?.toInt()
                )
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    sendGiftsResult.value = Result.Success(it.data, it.message)
                    giftsData.value = it.data
                    giftSelectedId.value = -1
                } else {
                    sendGiftsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                sendGiftsResult.value = Result.Error(it)
            })
        return sendGiftsResult
    }

    fun sendGifts(id:Int): MutableLiveData<Result<List<GiftsResponseModel>>> {
        sendGiftsResult.value = Result.Loading
        compositeDisposable + giftRepo.sendGifts(
            SendGiftsRequestModel(
                dto = DtoSendGiftsModel(
                    userPlayerId = id,
                    giftId = giftSelectedId.value?.toInt()
                )
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    sendGiftsResult.value = Result.Success(it.data, it.message)
                    giftsData.value = it.data
                    giftSelectedId.value = -1
                } else {
                    sendGiftsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                sendGiftsResult.value = Result.Error(it)
            })
        return sendGiftsResult
    }


    val reportPlayerAddResult: MutableLiveData<Result<List<ReportResponseModel>>> =
        MutableLiveData()

    fun reportPlayerAdd(): MutableLiveData<Result<List<ReportResponseModel>>> {
        reportPlayerAddResult.value = Result.Loading
        compositeDisposable + playerRepo.reportPlayerAdd(
            ReportPlayerAddRequestModel(
                dto = DtoReportPlayerModel(
                    reportedUserPlayerId = playerNameId.value?.toInt() ?: -1,
                    reportReasonId = playerNameId.value ?: -1,
                    notes = "".toString()
                )
            )

        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    reportPlayerAddResult.value = Result.Success(it.data, it.message)
                } else {
                    reportPlayerAddResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                reportPlayerAddResult.value = Result.Error(it)
            })
        return reportPlayerAddResult
    }

    // send Request
    val sendRequestResult: MutableLiveData<Result<UserFriendsResponseModel>> =
        MutableLiveData()

    fun sendRequest(): MutableLiveData<Result<UserFriendsResponseModel>> {
        sendRequestResult.value = Result.Loading
        compositeDisposable + playerRepo.sendRequest(
            SendRequestModel(
                userId = playerNameId.value ?: -1
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    sendRequestResult.value = Result.Success(it.data, it.message)
                } else {
                    sendRequestResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                sendRequestResult.value = Result.Error(it)
            })
        return sendRequestResult
    }

    fun sendRequestFriendsHub(): ArgumentJoinGame {
        val jsonFriendsRequestResponse = JsonFriendsRequestResponse(
            UserId = playerNameId.value ?: -1
        )
        val jsonString = Json.encodeToString(jsonFriendsRequestResponse)
        return ArgumentJoinGame(
            MessageType = 2,
            JsonData = jsonString
        )
    }

    fun sendGiftHub(playerId:Int): ArgumentJoinGame {
        val jsonDataSendGiftRequest = JsonDataSendGiftRequest(
            UserPlayerId = playerId,
            GiftId = giftSelectedId.value?.toInt()
        )
        val jsonString = Json.encodeToString(jsonDataSendGiftRequest)
        return ArgumentJoinGame(
            MessageType = 8,
            JsonData = jsonString
        )
    }

    // delete Request
    val deleteFriendResult: MutableLiveData<Result<List<UserFriendsResponseModel>>> =
        MutableLiveData()

    fun deleteFriendRequest(): MutableLiveData<Result<List<UserFriendsResponseModel>>> {
        deleteFriendResult.value = Result.Loading
        compositeDisposable + friendsRepo.deleteFriendRequest(
            DeleteFriendRequestModel(
                userFriendId = playerNameId.value ?: -1
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    deleteFriendResult.value = Result.Success(it.data, it.message)
                } else {
                    deleteFriendResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                deleteFriendResult.value = Result.Error(it)
            })
        return deleteFriendResult
    }

    // send Gifts
    val friendshipStatusResult: MutableLiveData<Result<FriendshipStatusResponseModel>> =
        MutableLiveData()

    fun friendshipStatus(): MutableLiveData<Result<FriendshipStatusResponseModel>> {
        friendshipStatusResult.value = Result.Loading
        compositeDisposable + playerRepo.friendshipStatus(
            id = playerNameId.value ?: -1
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    friendshipStatusResult.value = Result.Success(it.data, it.message)
                } else {
                    friendshipStatusResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                friendshipStatusResult.value = Result.Error(it)
            })
        return friendshipStatusResult
    }

    // unfriend Request
    val unfriendRequestResult: MutableLiveData<Result<List<UserFriendsResponseModel>>> =
        MutableLiveData()

    fun unfriendRequest(): MutableLiveData<Result<List<UserFriendsResponseModel>>> {
        unfriendRequestResult.value = Result.Loading
        compositeDisposable + friendsRepo.unfriendRequest(
            UnfriendRequestModel(
                userFriendId = playerNameId.value ?: -1
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    unfriendRequestResult.value = Result.Success(it.data, it.message)
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

    fun clearData() {
        statusPlayer.value = ""
        statusPlayerId.value = 1
        playerName.value = ""
        firstName.value = ""
        lastName.value = ""
        personId.value = ""
        userLevelTitle.value = ""
        phone.value = ""
        email.value = ""
        xp.value = ""
        coins.value = ""
        imagePerson.value = ""
        imagePersonBackground.value = ""
    }


}