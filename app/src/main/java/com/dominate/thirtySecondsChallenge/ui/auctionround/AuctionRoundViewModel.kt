package com.dominate.thirtySecondsChallenge.ui.auctionround

import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel

class AuctionRoundViewModel : BaseViewModel() {

    val countAnswer : MutableLiveData<String> by lazy { MutableLiveData<String>("00") }
    val answerUser : MutableLiveData<String> by lazy { MutableLiveData<String>("02/23") }
    val answerPlayer : MutableLiveData<String> by lazy { MutableLiveData<String>("00") }
    val startAuctionRound : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }

}