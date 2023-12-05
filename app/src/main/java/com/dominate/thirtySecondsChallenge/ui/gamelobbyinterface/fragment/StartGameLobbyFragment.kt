package com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.signalR.exitgame.ExitGameResultModel
import com.dominate.thirtySecondsChallenge.databinding.FragmentStartGameLobbyBinding
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.data.signalR.isready.JsonDataIsReadyResponse
import com.dominate.thirtySecondsChallenge.data.signalR.stickers.JsonDataStickersResponse
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyInterfaceFragmentDirections
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.interactiongame.InteractionInGameFragment
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.ui.setting.FragmentSettingDialog
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.anim.Shaking
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_start_game_lobby.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@OptIn(ExperimentalSerializationApi::class)
@AndroidEntryPoint
class StartGameLobbyFragment : BaseBindingFragment<FragmentStartGameLobbyBinding>(),
    HubConnectionListener,
    HubEventListener {

    override fun getLayoutId(): Int = R.layout.fragment_start_game_lobby

    private val viewModel by activityViewModels<GameLobbyViewModel>()

    @Inject
    lateinit var userPref: UserPref

    lateinit var audioPlayer: AudioPlayer

    var handler = Handler()

    var myApplication: MyApplication? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        onBackPressed()
    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        startConnection()
        sound()
        startAnimation()
        viewModel.isReady.value = false

    }

    private fun startConnection(){
        myApplication = requireActivity().application as MyApplication
        myApplication?.connection?.addListener(this)
    }

    private fun sound(){
        audioPlayer = AudioPlayer(requireContext(), R.raw.sending_sticker_t30)
    }

    private fun startAnimation(){
        requireContext().Shaking(ic_vs)
    }

    private fun setUpViewsListeners() {

        btn_Ready.onClick {
            setIsReadyHub()
        }

        tv_ExitGame.onClick {
            handler.removeCallbacks(myRunnable)
            handler.removeCallbacks(runnablePlayer)
            audioPlayer.stopPlayback()
            findNavController().navigate(GameLobbyInterfaceFragmentDirections.actionGlobalEndGameFragment())
        }

        iv_PlayerPerson.onClick {
            if (viewModel.playerId.value != null && viewModel.playerId.value != 0) {
                val playerProfileFragment =
                    PlayerProfileFragment.newInstance(viewModel.playerId.value ?: -1)
                playerProfileFragment.show(parentFragmentManager, "player Profile Fragment")
            }
        }

        iv_setting.onClick {
            val fragmentSettingDialog = FragmentSettingDialog()
            fragmentSettingDialog.show(parentFragmentManager, "fragment Setting Dialog")
        }

        iv_interaction.onClick {
            val interactionInGameFragment = InteractionInGameFragment()
            interactionInGameFragment.setTargetFragment(this, 0)
            interactionInGameFragment.show(parentFragmentManager, "interaction In Game Fragment")
        }

    }

    private fun setIsReadyHub() {
        try {
            myApplication?.connection?.invoke("SendMessage", viewModel.setIsReadyHub())
        } catch (e: Exception) {
            Log.i("errorSetIsReadyHub", e.message.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setStickersUser(isUser: Boolean) {
        if (isUser) {
            audioPlayer.startPlayback()
            viewModel.isSickerUser.value = true
            handler.postDelayed(myRunnable, 3000)
        } else {
            viewModel.isSickerPlayer.value = true
            handler.postDelayed(runnablePlayer, 3000)
        }

    }

    val myRunnable = Runnable {
        viewModel.isSickerUser.value = false
    }

    val runnablePlayer = Runnable {
        viewModel.isSickerPlayer.value = false
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
            val responseArguments = Json { ignoreUnknownKeys = true }.decodeFromString<List<ArgumentsResponse>>(arguments)

            when (responseArguments[0].messageType) {

                MessageGame.IS_READY_ID -> {
                    val dataJson = Json { ignoreUnknownKeys = true }.decodeFromString<JsonDataIsReadyResponse>(responseArguments[0].jsonData)

                    // check user click is ready
                    lifecycleScope.launchWhenStarted {
                        when (userPref.getUser()?.id == dataJson.GameDetails.FirstPlayerId) {

                            true -> {
                                viewModel.isReady.value =
                                    dataJson.GameDetails.FirstPlayerIsReady.toString().toBoolean()
                            }

                            false -> {
                                viewModel.isReady.value =
                                    dataJson.GameDetails.SecondPlayerIsReady.toString().toBoolean()
                            }

                        }
                    }

                    if (dataJson.IsGameReady) {
                        lifecycleScope.launchWhenStarted {
                            viewModel.isReady.value = false
                            viewModel.dataJsonModel = dataJson
                            viewModel.gamePlayTitle.value = dataJson.GameRoundDetails.GamePlayTitle
                            viewModel.timerToAnswer.value = dataJson.GameRoundDetails.TimerToAnswer.toString()
                            viewModel.questionTitle.value = dataJson.QuestionsDetails.QuestionTitle.toString()
                            viewModel.gameRoundId.value = dataJson.GameRoundDetails.Id
                            viewModel.questionId.value = dataJson.QuestionsDetails.Id
                            viewModel.gamePlayId.value = dataJson.GameRoundDetails.GamePlayId
                            viewModel.isGameFinished.value = false
                            // check user is firstPlayer or second player
                            when (userPref.getUser()?.id == dataJson.GameDetails.FirstPlayerId) {

                                true -> {
                                    viewModel.firstPlayerPoint.value = dataJson.GameRoundDetails.FirstPlayerPoints.toString()
                                    viewModel.secondPlayerPoint.value = dataJson.GameRoundDetails.SecondPlayerPoints.toString()
                                }

                                false -> {
                                    viewModel.firstPlayerPoint.value = dataJson.GameRoundDetails.SecondPlayerPoints.toString()
                                    viewModel.secondPlayerPoint.value = dataJson.GameRoundDetails.FirstPlayerPoints.toString()
                                }

                            }

                            viewModel.playerTurn.value = dataJson.PlayerTurn
                            viewModel.isTurnPlaying.value = userPref.getUser()?.id == viewModel.playerTurn.value

                            handler.removeCallbacks(myRunnable)
                            handler.removeCallbacks(runnablePlayer)
                            findNavController().navigate(StartGameLobbyFragmentDirections.actionStartGameLobbyFragmentToGameControllerFragment())
                        }
                    }
                }

                MessageGame.IS_STICKERS -> {
                    val dataJson = Json { ignoreUnknownKeys = true }.decodeFromString<JsonDataStickersResponse>(responseArguments[0].jsonData)
                    lifecycleScope.launchWhenStarted {
                        when (dataJson.SenderId == userPref.getUser()?.id) {
                            true -> {
                                setStickersUser(true)
                                viewModel.strikeUrl.value = dataJson.Sticker.ImageUrl
                            }

                            false -> {
                                setStickersUser(false)
                                viewModel.strikeUrlPlayer.value = dataJson.Sticker.ImageUrl
                            }
                        }
                    }
                }

                MessageGame.EXIT_GAME ->{
                    lifecycleScope.launchWhenStarted {
                        val dataJson = Json { ignoreUnknownKeys = true }.decodeFromString<ExitGameResultModel>(responseArguments[0].jsonData)
                        if (dataJson.WinnerId == userPref.getUser()?.id){
                            if (viewModel.gameId.value == dataJson.GameId)
                                requireActivity().runOnUiThread {
                                    requireView().showSnackbar("تم انهاء اللعبة",R.drawable.snackbar)
                                }
                            findNavController().navigate(StartGameLobbyFragmentDirections.actionGlobalHomeFragment())
                        }
                    }
               }
            }

        } catch (e: Exception) {
            Log.i("errorSerializableHubStart", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorHubStart", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageHubStart", message.toString())
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    audioPlayer.stopPlayback()
                    findNavController().navigate(GameLobbyInterfaceFragmentDirections.actionGlobalEndGameFragment())
                }
            })
    }

}