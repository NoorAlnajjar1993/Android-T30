package com.dominate.thirtySecondsChallenge.ui.whatdoyouknow

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.signalR.exitgame.ExitGameResultModel
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame.IS_SELECT_ANSWER
import com.dominate.thirtySecondsChallenge.data.signalR.isready.AnswerModel
import com.dominate.thirtySecondsChallenge.data.signalR.joingame.ArgumentJoinGame
import com.dominate.thirtySecondsChallenge.data.signalR.selectanswer.JsonDataIsSelectAnswerResponse
import com.dominate.thirtySecondsChallenge.data.signalR.selectanswer.SelectAnswerDataRequest
import com.dominate.thirtySecondsChallenge.databinding.FragmentWhatDoYouKnowBinding
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.adapter.QuestionAdapter
import com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.adapter.StrikeAdapter
import com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.model.StrikeModel
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogAnswer
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogCircular
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogLottie
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_what_do_you_know.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@AndroidEntryPoint
class WhatDoYouKnowFragment : BaseBindingFragment<FragmentWhatDoYouKnowBinding>(),
    OnItemClickListener<AnswerModel>,
    HubConnectionListener,
    HubEventListener {

    override fun getLayoutId(): Int = R.layout.fragment_what_do_you_know

    private val viewModel by activityViewModels<GameLobbyViewModel>()

    @Inject
    lateinit var userPref: UserPref

    @Inject
    lateinit var adapter: QuestionAdapter

    @Inject
    lateinit var strikeUserAdapter: StrikeAdapter

    @Inject
    lateinit var strikePlayerAdapter: StrikeAdapter

    var myApplication: MyApplication? = null

    lateinit var audioPlayerStartRound: AudioPlayer

    var handler = Handler()

    var messageStatusAnswer ="اجابة\nصحيحة!"
    var newAnswer :String?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setRecycleView()
        setUpViewsListeners()
        setStrikeRecycleView()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        startConnection()

        audioPlayerStartRound = AudioPlayer(requireContext(), R.raw.start_round_t30)
//        audioCountDown = AudioPlayer(requireContext(), R.raw.count_down_clock_one_tick_t30)

        viewModel.isTimerEnabled.observe(
            viewLifecycleOwner
        ) { items ->
            if (items == true) {
                AudioPlayer(requireContext(), R.raw.time_over_t30).startPlayback()
                if (viewModel.isTurnPlaying.value == true) {
                    setSelectAnswerHub(
                        answerId = 0,
                        answerTitle = "end time",
                        true
                    )
                    viewModel._isTimerEnabled.value = false
                }
            }
        }

        startDialogWhenTurnGame()


    }

    private fun startConnection() {
        myApplication = requireActivity().application as MyApplication
        myApplication?.connection?.addListener(this)
    }

    private fun startDialogWhenTurnGame() {
        dialogStartRound()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            startTimer()
            delay(3000)
            viewModel.startHandleTimer()
            if (viewModel.isTurnPlaying.value == true) {
//                audioCountDown.startPlaybackLoop()
                adapter.submitItems(
                    viewModel.dataJsonModel?.QuestionsDetails?.Answers ?: arrayListOf()
                )
            }
        }
    }

    private fun setUpViewsListeners() {

        iv_keyboard.onClick {
//            val winGameFragment = WinGameFragment()
//            winGameFragment.show(parentFragmentManager, "win Game Fragment")
//            findNavController().navigate(WhatDoYouKnowFragmentDirections.actionWhatDoYouKnowFragmentToWinGameFragment())
        }

        cv_Pass.onClick {
            if (viewModel.enablePass.value == true) {
                messageStatusAnswer = "تخطي"
                AudioPlayer(requireContext(), R.raw.pass_t30).startPlayback()
                setSelectAnswerHub(
                    answerId = 0,
                    answerTitle = "قام بتمرير الإجابة 'باس'",
                    false,
                    pass = true
                )
            } else {
                requireView().showSnackbar(
                    "يمكنك استعمال زر باس مرة واحدة فقط",
                    R.drawable.snackbar_error
                )
            }
        }

    }

    private fun setRecycleView() {
        context?.let {
            adapter = QuestionAdapter()
            rvQuestions.adapter = adapter
            adapter.onItemClickListener = this
        }
    }

    private fun setStrikeRecycleView() {

        context?.let {
            strikeUserAdapter = StrikeAdapter()
            rv_StrikeUser.adapter = strikeUserAdapter
        }

        val itemStrikeUser: ArrayList<StrikeModel> = ArrayList()
        repeat(3) {
            itemStrikeUser.add(
                StrikeModel(
                    isStrike = false
                )
            )
        }
        strikeUserAdapter.submitItems(itemStrikeUser)

        val itemStrikePlayer: ArrayList<StrikeModel> = ArrayList()
        repeat(3) {
            itemStrikePlayer.add(
                StrikeModel(
                    isStrike = false
                )
            )
        }

        context?.let {
            strikePlayerAdapter = StrikeAdapter()
            rv_StrikePlayer.adapter = strikePlayerAdapter
        }
        strikePlayerAdapter.submitItems(itemStrikePlayer)

    }

    private fun setSelectAnswerHub(
        answerId: Int,
        answerTitle: String,
        timeOut: Boolean,
        pass: Boolean = false
    ) {
        // TODO: request set select answer hub
        println("-------------------GameId------------------")
        println(viewModel.gameId.value.toString())
        println("-------------------GameRoundId------------------")
        println(viewModel.gameRoundId.value.toString())
        println("-------------------GamePlayId------------------")
        println(viewModel.gamePlayId.value.toString())
        println("-------------------QuestionId------------------")
        println(viewModel.questionId.value.toString())
        println("-------------------AnswerId------------------")
        println(answerId.toString())
        println("-------------------AnswerTitle------------------")
        println(answerTitle.toString())
        println("-------------------timeOut------------------")
        println(timeOut.toString())
        println("-------------------timerToAnswer------------------")
        val (minutes, seconds) = viewModel.timerToAnswer.value!!.split(":")
        println(minutes)
        println(seconds)
        try {
            val selectAnswerDataRequest = SelectAnswerDataRequest(
                GameId = viewModel.gameId.value ?: 0,
                GameRoundId = viewModel.gameRoundId.value ?: 0,
                GamePlayId = viewModel.gamePlayId.value ?: 0,
                QuestionId = viewModel.questionId.value ?: 0,
                AnswerId = answerId,
                AnswerTitle = answerTitle,
                TimeOut = timeOut,
                Pass = pass,
                TimeToAnswer = if (seconds.toString() == "00"){0}else{seconds.toString().toInt()}
            )
            val jsonString = Json.encodeToString(selectAnswerDataRequest)
            val atr = ArgumentJoinGame(
                MessageType = 16,
                JsonData = jsonString
            )
            myApplication?.connection?.invoke("SendMessage", atr)
        } catch (e: Exception) {
            Log.i("errorSelectAnswer", e.message.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: AnswerModel, position: Int) {
        AudioPlayer(requireContext(), R.raw.answer_click_choice_t30).startPlayback()
        setSelectAnswerHub(
            answerId = item.Id ?: 0,
            answerTitle = item.AnswerTitle ?: "",
            false
        )
    }

    override fun onConnected() {
    }

    override fun onDisconnected() {
        requireActivity().runOnUiThread {
            view?.showSnackbar("disconnected from server", R.drawable.snackbar)
        }
    }

    override fun onMessage(message: HubMessage?) {
        try {
            val arguments = Gson().toJson(message?.arguments)
            val responseArguments = Json.decodeFromString<List<ArgumentsResponse>>(arguments)

            when (responseArguments[0].messageType) {
                IS_SELECT_ANSWER -> {
                    val dataJson = Json {
                        ignoreUnknownKeys = true
                    }.decodeFromString<JsonDataIsSelectAnswerResponse>(responseArguments[0].jsonData)

                    lifecycleScope.launchWhenStarted {

                        viewModel.isShowDialog.value =
                            userPref.getUser()?.id == viewModel.playerTurn.value
                        viewModel.isTurnPlaying.value = !dataJson.Question.Answers.isNullOrEmpty()
                        viewModel.timerToAnswer.value = dataJson.GameRound.TimerToAnswer.toString()

                        // check new question
                        if (dataJson.HaveNewQuestion == true){
                            AudioPlayer(requireContext(), R.raw.question_t30).startPlayback()
                            viewModel.questionTitle.value =
                                dataJson.Question.QuestionTitle.toString()
                            newAnswer = "السؤال التالي"
                        }else{
                            newAnswer = null
                        }

//                        if (viewModel.questionTitle.value != dataJson.Question.QuestionTitle.toString()) {
//                            AudioPlayer(requireContext(), R.raw.question_t30).startPlayback()
//                            viewModel.questionTitle.value =
//                                dataJson.Question.QuestionTitle.toString()
//                            newAnswer = "السؤال التالي"
//                        }else{
//                            newAnswer = null
//                        }
                        viewModel.playerTurn.value = dataJson.PlayerTurn
                        viewModel.gamePlayId.value = dataJson.GameRound.GamePlayId
                        viewModel.questionId.value = dataJson.Question.Id
                        viewModel.enablePass.value = dataJson.AllowPass

                        viewModel.stopTimer()
//                        audioCountDown.stopPlayback()

                        // finish game
                        viewModel.collectionFinishDataJsonModel = dataJson
                        if (userPref.getUser()?.id == dataJson.Game?.FirstPlayerId) {
                            if (viewModel.userPointsFinish.value != dataJson.GameResult.FirstPlayerPoints) {
                                AudioPlayer(requireContext(), R.raw.point_t30).startPlayback()
                                viewModel.userPointsFinish.value =
                                    dataJson.GameResult.FirstPlayerPoints
                                viewModel.userXp.value =
                                    dataJson.GameResult.FirstPlayerXp
                                viewModel.userCoins.value =
                                    dataJson.GameResult.FirstPlayerCoins
                            }
                            viewModel.playerPointsFinish.value =
                                dataJson.GameResult.SecoundPlayerPoints

                        } else {
                            if (viewModel.userPointsFinish.value != dataJson.GameResult.SecoundPlayerPoints) {
                                AudioPlayer(requireContext(), R.raw.point_t30).startPlayback()
                                viewModel.userPointsFinish.value =
                                    dataJson.GameResult.SecoundPlayerPoints
                                viewModel.userXp.value =
                                    dataJson.GameResult.SecoundPlayerXp
                                viewModel.userCoins.value =
                                    dataJson.GameResult.SecoundPlayerCoins
                            }
                            viewModel.playerPointsFinish.value =
                                dataJson.GameResult.FirstPlayerPoints
                        }

                        when (dataJson.IsGameFinished) {
                            true -> {
                                viewModel.isGameFinished.value = true
                                try {
                                    viewModel.stopTimer()
                                    if (dataJson.GameResult.WinnerId == userPref.getUser()?.id) {
                                        findNavController().navigate(WhatDoYouKnowFragmentDirections.actionWhatDoYouKnowFragmentToWinGameFragment())
                                        viewModel.isUserWin.value = true
                                    } else {
                                        findNavController().navigate(WhatDoYouKnowFragmentDirections.actionWhatDoYouKnowFragmentToLossGameFragment())
                                        viewModel.isUserWin.value = false
                                    }
                                } catch (e: Exception) {

                                }

                            }

                            false -> {
                                viewModel.isGameFinished.value = false
                                when (viewModel.isShowDialog.value) {
                                    true -> {
                                        // check is answer correct or no
                                        adapter.submitItems(
                                            dataJson.Question.Answers ?: arrayListOf()
                                        )
                                        when (dataJson.IsCorrectAnswer) {
                                            true -> isAnswerCorrect()
                                            false -> isStrike()
                                        }
                                    }

                                    false -> {
                                        // turn on round between players and show answer player and set answer
                                        isPlayerTurn(
                                            viewModel.isTurnPlaying.value ?: false,
                                            dataJson.PreviousAnswerTitle ?: "",
                                            dataJson.Question.Answers ?: arrayListOf()
                                        )
                                    }
                                }

                                // Strikes
                                when (userPref.getUser()?.id == dataJson.Game?.FirstPlayerId) {
                                    true -> {
                                        viewModel.firstPlayer.value =
                                            dataJson.GameRound.FirstPlayerStrikes
                                        viewModel.secondPlayer.value =
                                            dataJson.GameRound.SecondPlayerStrikes

                                        viewModel.firstPlayerPoint.value =
                                            dataJson.GameRound.FirstPlayerPoints.toString()
                                        viewModel.secondPlayerPoint.value =
                                            dataJson.GameRound.SecondPlayerPoints.toString()
                                    }

                                    false -> {
                                        viewModel.firstPlayer.value =
                                            dataJson.GameRound.SecondPlayerStrikes
                                        viewModel.secondPlayer.value =
                                            dataJson.GameRound.FirstPlayerStrikes

                                        viewModel.firstPlayerPoint.value =
                                            dataJson.GameRound.SecondPlayerPoints.toString()
                                        viewModel.secondPlayerPoint.value =
                                            dataJson.GameRound.FirstPlayerPoints.toString()
                                    }
                                }

                                handleStrikes(
                                    viewModel.firstPlayer.value,
                                    viewModel.secondPlayer.value
                                )

                            }
                        }

                    }
                    Log.i("response select answer", dataJson.toString())
                }

                MessageGame.EXIT_GAME -> {
                    lifecycleScope.launchWhenStarted {
                        val dataJson =
                            Json.decodeFromString<ExitGameResultModel>(responseArguments[0].jsonData)

                        viewModel.stopTimer()
                        if (dataJson.WinnerId == userPref.getUser()?.id) {
                            if (viewModel.gameId.value == dataJson.GameId)
                                findNavController().navigate(WhatDoYouKnowFragmentDirections.actionWhatDoYouKnowFragmentToWinGameFragment())
                            viewModel.isUserWin.value = true
                        }
                    }
                }

            }

        } catch (e: Exception) {
            Log.i("errorSerializableAnswer", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorAnswerHub", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageAnswerHub", message.toString())
    }

    private fun setInit() {

    }

    private fun setUserAnswer(userTurn: Int?, answers: List<AnswerModel>?) {

        if (userPref.getUser()?.id == userTurn) {
            adapter.clear()
            adapter.submitItems(answers ?: arrayListOf())
        } else {
            adapter.clear()
        }

    }

    private fun setPlayerAnswer(userTurn: Int?, answers: List<AnswerModel>?) {

        if (userPref.getUser()?.id != userTurn) {
            adapter.clear()
            adapter.submitItems(answers ?: arrayListOf())
        } else {
            adapter.clear()
        }

    }

    private fun handleStrikes(firstPlayerStrikes: Int?, secondPlayerStrikes: Int?) {

        if (firstPlayerStrikes != 0) {
            var _firstPlayerStrikes = (firstPlayerStrikes?.minus(1))
            when (_firstPlayerStrikes) {
                0 -> {
                    strikeUserAdapter.items[_firstPlayerStrikes].isStrike = true
                    strikeUserAdapter.notifyItemChanged(_firstPlayerStrikes)
                }

                1 -> {
                    strikeUserAdapter.items[_firstPlayerStrikes].isStrike = true
                    strikeUserAdapter.notifyItemChanged(_firstPlayerStrikes)
                }

                2 -> {
                    strikeUserAdapter.items[_firstPlayerStrikes].isStrike = true
                    strikeUserAdapter.notifyItemChanged(_firstPlayerStrikes)
                }
            }
        } else {
            strikeUserAdapter.items.forEach {
                it.isStrike = false
            }
            strikeUserAdapter.notifyDataSetChanged()
        }

        if (secondPlayerStrikes != 0) {
            var _secondPlayerStrikes = (secondPlayerStrikes?.minus(1))
            when (_secondPlayerStrikes) {
                0 -> {
                    strikePlayerAdapter.items[_secondPlayerStrikes].isStrike = true
                    strikePlayerAdapter.notifyItemChanged(_secondPlayerStrikes)
                }

                1 -> {
                    strikePlayerAdapter.items[_secondPlayerStrikes].isStrike = true
                    strikePlayerAdapter.notifyItemChanged(_secondPlayerStrikes)
                }

                2 -> {
                    strikePlayerAdapter.items[_secondPlayerStrikes].isStrike = true
                    strikePlayerAdapter.notifyItemChanged(_secondPlayerStrikes)
                }
            }
        } else {
            strikePlayerAdapter.items.forEach {
                it.isStrike = false
            }
            strikePlayerAdapter.notifyDataSetChanged()
        }

    }

    private fun isPlayerTurn(isShow: Boolean, answer: String, itemAnswer: List<AnswerModel>?) {
        isShowAnswerToPlayer(answer)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            requireContext().showDialogCircular(
                "دورك الآن", R.drawable.ic_circuler_blue, 2000 , newAnswer
            )
            AudioPlayer(requireContext(), R.raw.starting_gamer_turn_t30).startPlayback()
            delay(2000)
            adapter.submitItems(itemAnswer ?: arrayListOf())
//            audioCountDown.startPlaybackLoop()
            viewModel.startHandleTimer()

        }
    }

    private fun isAnswerCorrect() {
        CoroutineScope(Dispatchers.Main).launch {
            requireContext().showDialogCircular(
                messageStatusAnswer, R.drawable.ic_circuler_green,
                4000
            )
            AudioPlayer(requireContext(), R.raw.right_answer_t30).startPlayback()
            messageStatusAnswer ="اجابة\nصحيحة!"
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            viewModel.startHandleTimer()
        }

    }

    private fun isAnswerUnCorrect() {
        CoroutineScope(Dispatchers.Main).launch {
            requireContext().showDialogCircular(
                "اجابة\nخاطئة", R.drawable.ic_circuler_red, 1000
            )
            AudioPlayer(requireContext(), R.raw.get_strick_t30).startPlayback()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun isShowAnswerToPlayer(msg: String) {
        if (msg.isNotEmpty())
            requireContext().showDialogAnswer(
                msg, true, 2000
            )
    }

    private fun isStrike() {
        requireContext().showDialogLottie(
            R.raw.strike,
            "1 سترايك",
            isTimer = 4000
        )
        AudioPlayer(requireContext(), R.raw.get_strick_t30).startPlayback()
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            viewModel.startHandleTimer()
        }
    }


    private fun dialogStartRound() {
        requireContext().showDialogLottie(
            R.raw.what_do_u_know_round, "جولة ماذا تعرف؟", isTimer = 3000
        )
        audioPlayerStartRound.startPlayback()
    }

    private fun startTimer() {
        AudioPlayer(requireContext(), R.raw.start_time_t30).startPlayback()
        requireContext().showDialogLottie(
            R.raw.time_started,
            "بدأ الوقت!",
            isTimer = 3000
        )
    }


}