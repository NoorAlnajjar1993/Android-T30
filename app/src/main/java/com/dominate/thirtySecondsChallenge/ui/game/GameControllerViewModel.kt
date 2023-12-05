package com.dominate.thirtySecondsChallenge.ui.game

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.daos.remote.gift.GiftRemoteDao
import com.dominate.thirtySecondsChallenge.data.repo.gift.GiftRepo
import com.dominate.thirtySecondsChallenge.data.repo.home.HomeRepo
import com.dominate.thirtySecondsChallenge.data.signalR.isready.JsonDataIsReadyResponse

class GameControllerViewModel@ViewModelInject constructor(
    private val giftRepo: GiftRepo
) : BaseViewModel() {

    val userName : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val playerName : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userImage : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val playerImage : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val playerId : MutableLiveData<Int> by lazy { MutableLiveData<Int>() }


}