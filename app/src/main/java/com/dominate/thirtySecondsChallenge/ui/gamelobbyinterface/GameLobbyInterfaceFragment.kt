package com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.databinding.FragmentGameLobbyInterfaceBinding
import com.dominate.thirtySecondsChallenge.data.signalR.creategame.JsonDataCreateGameResponse
import com.dominate.thirtySecondsChallenge.data.signalR.exitgame.ExitGameResultModel
import com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest.JsonResponseError
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame.CREATE_GAME_ID
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogCircular
import com.dominate.thirtySecondsChallenge.utils.extensions.addPlaySound
import com.dominate.thirtySecondsChallenge.utils.extensions.copyText
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.openShareView
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbarFragment
import com.dominate.thirtySecondsChallenge.utils.extensions.vibrate
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_game_lobby_interface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

private val json = Json { ignoreUnknownKeys = true }

@AndroidEntryPoint
class GameLobbyInterfaceFragment : BaseBindingFragment<FragmentGameLobbyInterfaceBinding>(),
    HubConnectionListener,
    HubEventListener {

    override fun getLayoutId(): Int = R.layout.fragment_game_lobby_interface

    companion object {
        const val PUBLIC_GAME_ID = 1
        const val PRIVATE_GAME_ID = 2
        const val DIRECT_GAME_ID = 3
        const val IS_INVITE_GAME_ID = 4

    }

    private val viewModel by activityViewModels<GameLobbyViewModel>()

    private val args: GameLobbyInterfaceFragmentArgs by navArgs()

    var myApplication: MyApplication? = null

    lateinit var audioPlayer: AudioPlayer

    @Inject
    lateinit var userPref: UserPref

    var inviteLink = ""
    var isInviteLink = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        onBackPressed()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        audioPlayer = AudioPlayer(requireContext(), R.raw.waiting_in_the_lobby_t30)
        audioPlayer.startPlaybackLoop()

        myApplication = requireActivity().application as MyApplication
        myApplication?.connection?.addListener(this)

        when (args.typeGame) {

            PUBLIC_GAME_ID -> {
                viewModel.titleFound.value = "جاري البحث عن لاعب ،\n" +
                        "انتظر قليلا من فضلك.."
                viewModel.typeGame.value = args.typeGame

            }

            PRIVATE_GAME_ID -> {
                viewModel.titleFound.value = "شارك رابط اللعب  ، وانتظر قليلا حتى ينضم خصمك"
                viewModel.typeGame.value = args.typeGame

            }

            DIRECT_GAME_ID -> {
                joinGameHub()
                viewModel.titleFound.value = "جاري البحث عن لاعب ،\n" +
                        "انتظر قليلا من فضلك.."
                viewModel.typeGame.value = 1
            }

            IS_INVITE_GAME_ID -> {
                isInviteLink = true
                inviteJoinGameHub()
                viewModel.titleFound.value = "جاري البحث عن اللاعب،\n" +
                        "انتظر قليلا من فضلك.."
                viewModel.typeGame.value = 1
            }


        }

    }

    private fun setUpViewsListeners() {

        tv_endGame.onClick {
            audioPlayer.stopPlayback()
            findNavController().navigate(GameLobbyInterfaceFragmentDirections.actionGlobalEndGameFragment())
        }

        tv_copyLink.onClick {
            vibrate(requireContext())
            generateShortDynamicLink(viewModel.gameId.value.toString()) { shortLink ->
                inviteLink = "$shortLink"
                requireContext().copyText(
                    requireView(),
                    inviteLink
                )
            }
        }

        btn_share.onClick {
            vibrate(requireContext())
            generateShortDynamicLink(viewModel.gameId.value.toString()) { shortLink  ->
                inviteLink = "$shortLink"
                requireContext().openShareView(
                    inviteLink
                )
            }
        }

    }

    private fun joinGameHub() {
        try {
            myApplication?.connection?.invoke("SendMessage", viewModel.joinGameHub())
        } catch (e: Exception) {
            audioPlayer.stopPlayback()
            requireView().showSnackbar(e.message.toString(), R.drawable.snackbar_error)
            findNavController().popBackStack()
        }
    }

    private fun inviteJoinGameHub() {
        try {
            myApplication?.connection?.invoke("SendMessage", viewModel.inviteJoinGameHub())
        } catch (e: Exception) {
            audioPlayer.stopPlayback()
            requireView().showSnackbar(e.message.toString(), R.drawable.snackbar_error)
            findNavController().popBackStack()
        }
    }

    fun generateShortDynamicLink(
        inviteGame: String,
        callback: (String) -> Unit
    ) {
        val deepLink = "https://www.ta7adi30second.com/?inviteGame=$inviteGame"

        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(deepLink))
            .setDomainUriPrefix("https://thirtysecondschallenge.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("com.dominate.thirtySecondsChallenge")
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder("dominate.dev.Tahady-30")
                    .build()
            )
            .buildShortDynamicLink()

        dynamicLink.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val shortLink = task.result?.shortLink.toString()
                callback(shortLink)
            } else {
                val exception = task.exception
                if (exception != null) {
                    Log.e("Dynamic Link Error", "Failed to create short link: ${exception.message}")
                }
                callback("") // Notify the callback about the failure
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onConnected() {
    }

    override fun onDisconnected() {
        requireActivity().runOnUiThread {
            try {
                requireView().showSnackbar("disconnected from server", R.drawable.snackbar_error)
            } catch (e: Exception) {
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onMessage(message: HubMessage?) {
        val arguments = Gson().toJson(message?.arguments)
        try {
            val responseArguments = Json.decodeFromString<List<ArgumentsResponse>>(arguments)

            lifecycleScope.launchWhenStarted {
                try {
                    val dataJson =
                        Json.decodeFromString<JsonDataCreateGameResponse>(responseArguments[0].jsonData)
                    viewModel.gameId.value = dataJson.Id
                } catch (e: Exception) {
                    Log.i("exception", e.message.toString())
                }
            }

            when (responseArguments[0].messageType) {

                CREATE_GAME_ID -> {
                    val dataJson =
                        Json.decodeFromString<JsonDataCreateGameResponse>(responseArguments[0].jsonData)

                    if (dataJson.SecondPlayerId != 0) {
                        requireActivity().runOnUiThread(Runnable {
                            lifecycleScope.launchWhenStarted {
                                audioPlayer.stopPlayback()

                                viewModel.gameId.value = dataJson.Id
                                viewModel.enablePass.value = true

                                if (userPref.getUser()?.id.toString() == dataJson.FirstPlayerId.toString()) {
                                    viewModel.userName.value = dataJson.FirstPlayerName.toString()
                                    viewModel.userImage.value = dataJson.FirstPlayerImage.toString()
                                    viewModel.userId.value = dataJson.FirstPlayerId
                                    viewModel.playerName.value =
                                        dataJson.SecondPlayerName.toString()
                                    viewModel.playerImage.value =
                                        dataJson.SecondPlayerImage.toString()
                                    viewModel.playerId.value = dataJson.SecondPlayerId
                                } else {
                                    viewModel.userName.value = dataJson.SecondPlayerName.toString()
                                    viewModel.userImage.value =
                                        dataJson.SecondPlayerImage.toString()
                                    viewModel.userId.value = dataJson.SecondPlayerId
                                    viewModel.playerName.value = dataJson.FirstPlayerName.toString()
                                    viewModel.playerImage.value =
                                        dataJson.FirstPlayerImage.toString()
                                    viewModel.playerId.value = dataJson.FirstPlayerId
                                }

                                try {
                                    findNavController().navigate(
                                        GameLobbyInterfaceFragmentDirections.actionGameLobbyInterfaceFragmentToStartGameLobbyFragment()
                                    )
                                } catch (e: Exception) {
                                    Log.i("errorController", e.message.toString())
                                }
                            }
                        })
                    } else {

//                        requireActivity().runOnUiThread(Runnable {
//                            requireView().showSnackbar(
//                                "لم يتم العثور على لاعب",
//                                R.drawable.snackbar
//                            )
//                        })
                    }

                }

                MessageGame.EXIT_GAME -> {
                    lifecycleScope.launchWhenStarted {
                        val dataJson =
                            Json.decodeFromString<ExitGameResultModel>(responseArguments[0].jsonData)
                        if (dataJson.WinnerId == userPref.getUser()?.id) {
                            if (viewModel.gameId.value == dataJson.GameId) {
                                findNavController().navigate(GameLobbyInterfaceFragmentDirections.actionGlobalHomeFragment())
                            }
                        }
                    }
                }

                MessageGame.ERROR_HUB -> {
                    lifecycleScope.launchWhenCreated {
                        val dataJson = Json { ignoreUnknownKeys = true }.decodeFromString<JsonResponseError>(responseArguments[0].jsonData)
                        if (isInviteLink){
                            isInviteLink = false
                                showSnackbarFragment(
                                    dataJson.Message,
                                    R.drawable.snackbar_error
                                )
//                            CoroutineScope(Dispatchers.Main).launch {
//                                delay(2000)
//                                findNavController().popBackStack()
//                            }
                        }
                    }
                }
            }


        } catch (e: Exception) {
            Log.i("errorSerializableLobby", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorHubLobby", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageLobby", message.toString())
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