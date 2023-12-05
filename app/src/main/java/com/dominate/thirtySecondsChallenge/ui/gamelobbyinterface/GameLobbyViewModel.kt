package com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface

import android.os.CountDownTimer
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.gift.StickersResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.repo.gift.GiftRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.data.signalR.creategame.GameRequestModel
import com.dominate.thirtySecondsChallenge.data.signalR.exitgame.ExitGameRequestModel
import com.dominate.thirtySecondsChallenge.data.signalR.isready.JsonDataIsReadyResponse
import com.dominate.thirtySecondsChallenge.data.signalR.joingame.ArgumentJoinGame
import com.dominate.thirtySecondsChallenge.data.signalR.joingame.IsReadyJsonDataRequest
import com.dominate.thirtySecondsChallenge.data.signalR.selectanswer.JsonDataIsSelectAnswerResponse
import com.dominate.thirtySecondsChallenge.utils.extensions.DateTimeUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.millisecondFormatting
import com.dominate.thirtySecondsChallenge.utils.extensions.secondToMillisecond
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameLobbyViewModel @ViewModelInject constructor(
    private val userPref: UserPref,
    private val giftRepo: GiftRepo

) : BaseViewModel() {

    companion object {

        // Code Timer
        const val RESEND_ENABLE_TIME_IN_SECOND: Long = 30
        const val RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND: Long = 1
    }

    val isSound: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isMusic: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val typeGame: MutableLiveData<Int> by lazy { MutableLiveData<Int>(1) }
    val titleFound: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val gameId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val playerName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userImage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val playerImage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val playerId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    var dataJsonModel: JsonDataIsReadyResponse? = null
    var collectionFinishDataJsonModel: JsonDataIsSelectAnswerResponse? = null
    val achievedBadgesImage: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val isReady: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isGameFinished: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    val questionTitle: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val gamePlayTitle: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val timerToAnswer: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val firstPlayerPoint: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val secondPlayerPoint: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val strikeUrl: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val strikeUrlPlayer: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val isTurnPlaying: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }
    val isShowDialog: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }
    val playerTurn: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val gamePlayId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val gameRoundId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val questionId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val firstPlayer: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val secondPlayer: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val userPointsFinish: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
    val playerPointsFinish: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
    val isUserWin: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val enablePass: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }

    val userXp: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val userCoins: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val playerXp: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val playerCoins: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val isSickerUser: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isSickerPlayer: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val inviteGameId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    val _isTimerEnabled = MutableLiveData<Boolean>(false)
    val isTimerEnabled: LiveData<Boolean> get() = _isTimerEnabled

    // timer to answer
//    val isTimerEnabled: MutableLiveData<Boolean> by lazy {
//        MutableLiveData<Boolean>(
//            false
//        )
//    }
    val timerCountDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(
            timerToAnswer.value!!.toLong().secondToMillisecond(),
            RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND.secondToMillisecond()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                timerToAnswer.value =
                    millisUntilFinished.millisecondFormatting(DateTimeUtil.TIME_FORMATTING__SECOND_MIL_SECOND)
            }

            override fun onFinish() {
                _isTimerEnabled.value = true
            }
        }
    }

    fun startHandleTimer() {
        _isTimerEnabled.value = false
        timerCountDownTimer.cancel()
        timerCountDownTimer.start()
    }

    fun stopTimer() {
        timerCountDownTimer.cancel()
    }

    // stickers
    val stickersResult: MutableLiveData<Result<List<StickersResponseModel>>> =
        MutableLiveData()

    val stickersData: MutableLiveData<List<StickersResponseModel>> =
        MutableLiveData()

    fun getStickers(): MutableLiveData<Result<List<StickersResponseModel>>> {
        stickersResult.value = Result.Loading
        compositeDisposable + giftRepo.getStickers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    stickersResult.value = Result.Success(it.data, it.message)
                    stickersData.value = it.data
                } else {
                    stickersResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                stickersResult.value = Result.Error(it)
            })
        return stickersResult
    }

    fun joinGameHub(): ArgumentJoinGame{
        val gameRequestModel = GameRequestModel(
            GameTypeId = typeGame.value?.toInt() ?: 1
        )
        val jsonString = Json.encodeToString(gameRequestModel)
        return ArgumentJoinGame(
            MessageType = 7,
            JsonData = jsonString
        )
    }

    fun inviteJoinGameHub(): ArgumentJoinGame {
        val gameRequestModel = GameRequestModel(
            GameTypeId = typeGame.value?.toInt() ?: 1,
            GameId = inviteGameId.value
        )
        val jsonString = Json.encodeToString(gameRequestModel)
        return ArgumentJoinGame(
            MessageType = 7,
            JsonData = jsonString
        )
    }

    fun setIsReadyHub(): ArgumentJoinGame{
        val isReadyJsonDataRequest = IsReadyJsonDataRequest(
            GameId = gameId.value ?: 0
        )
        val jsonString = Json.encodeToString(isReadyJsonDataRequest)
        return ArgumentJoinGame(
            MessageType = 11,
            JsonData = jsonString
        )
    }

    fun exitGame(): ArgumentJoinGame {
        val gameRequestModel = ExitGameRequestModel(
            GameId = gameId.value?.toInt() ?: 1
        )
        val jsonString = Json.encodeToString(gameRequestModel)
        return ArgumentJoinGame(
            MessageType = 10,
            JsonData = jsonString
        )
    }

}