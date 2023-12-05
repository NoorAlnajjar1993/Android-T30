package com.dominate.thirtySecondsChallenge.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.data.signalR.stickers.JsonDataStickersResponse
import com.dominate.thirtySecondsChallenge.databinding.FragmentGameControllerBinding
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.home.HomeFragment
import com.dominate.thirtySecondsChallenge.ui.interactiongame.InteractionInGameFragment
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.ui.sound.MusicPlayer
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogOutline
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_game_controller.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class GameControllerFragment : BaseBindingFragment<FragmentGameControllerBinding>(),
    HubConnectionListener,
    HubEventListener {

    override fun getLayoutId(): Int = R.layout.fragment_game_controller

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    private val viewModel by activityViewModels<GameLobbyViewModel>()

    @Inject
    lateinit var userPref: UserPref

    var handler = Handler()

    var myApplication: MyApplication? = null

    lateinit var audioPlayer: AudioPlayer

    lateinit var musicPlayer: MusicPlayer

    var isMusic = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        onBackPressed()
        setupNavigation()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        setUpController()
        startConnection()

        audioPlayer = AudioPlayer(requireContext(), R.raw.sending_sticker_t30)
        musicPlayer = MusicPlayer.getInstance(requireContext(), R.raw.music_running_t30)

    }

    private fun startConnection() {
        myApplication = requireActivity().application as MyApplication
        myApplication?.connection?.addListener(this)
    }

    private fun setUpController() {
        navController = requireActivity().findNavController(R.id.GameNavContainer)
        navGraph = navController.navInflater.inflate(R.navigation.game_nav_graph)
    }

    private fun setUpViewsListeners() {

        iv_personPlayer.onClick {
            val playerProfileFragment =
                PlayerProfileFragment.newInstance(viewModel.playerId.value ?: 0)
            playerProfileFragment.show(parentFragmentManager, "player Profile Fragment")
        }

        iv_setting_mode.onClick {
            try {
                findNavController().navigate(GameControllerFragmentDirections.actionGameControllerFragmentToFragmentSettingDialog())
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }

        iv_interaction_image.onClick {
            val interactionInGameFragment = InteractionInGameFragment()
            interactionInGameFragment.setTargetFragment(this, 0)
            interactionInGameFragment.show(parentFragmentManager, "interaction In Game Fragment")
        }

    }

    private fun setStartDestination(fragment: Int) {
        navGraph.startDestination = fragment
        navController.graph = navGraph
    }

    private fun setupNavigation() {
        requireActivity().findNavController(R.id.GameNavContainer)
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.whatDoYouKnowFragment -> {
                        onBackPressed()
                        if (!isMusic) {
                            musicPlayer.startPlaybackLoop()
                            isMusic = true
                        }
                    }

                    R.id.auctionRoundFragment -> {
                        onBackPressed()
                        viewModel.gamePlayTitle.value = "جولة 2:  المزاد"
                        viewModel.timerToAnswer.value = "30:00"
                    }

                    R.id.winGameFragment -> {
                        stopSound()
                        onExitPressed()

                    }

                    R.id.lossGameFragment -> {
                        stopSound()
                        onExitPressed()
                    }

                    else -> {
                        stopSound()
                        Log.i("errors", destination.label.toString())
                    }
                }
            }
    }

    private fun stopSound() {
        musicPlayer.stopPlayback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun setStickersUser(userPlay: Boolean) {
        try {
            if (userPlay) {
                audioPlayer.startPlayback()
                viewModel.isSickerUser.value = true
                handler.postDelayed(myRunnable, 3000)
            } else {
                viewModel.isSickerPlayer.value = true
                handler.postDelayed(runnablePlayer, 3000)
            }

        } catch (e: Exception) {
            Log.i("errors", e.message.toString())
        }
    }

    private val myRunnable = Runnable {
        viewModel.isSickerUser.value = false
    }

    private val runnablePlayer = Runnable {
        viewModel.isSickerPlayer.value = false
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireContext().showDialogOutline(
                        "هل انت متأكد من انك تريد الخروج؟", onPositiveButtonClick = {
                            AudioPlayer(requireContext(), R.raw.exit_the_game_t30).startPlayback()
                            musicPlayer =
                                MusicPlayer.getInstance(requireContext(), R.raw.music_running_t30)
                            musicPlayer.stopPlayback()
                            it.dismiss()
                            findNavController().navigate(GameControllerFragmentDirections.actionGlobalEndGameFragment())
                        }, btnText = "خروج"
                    )
                }
            })
    }

    private fun onExitPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                @SuppressLint("SuspiciousIndentation")
                override fun handleOnBackPressed() {
                    musicPlayer = MusicPlayer.getInstance(requireContext(), R.raw.music_running_t30)
                    musicPlayer.stopPlayback()
                    findNavController().navigate(
                        GameControllerFragmentDirections.actionGlobalHomeFragment().setEndGame(
                            HomeFragment.COLLECT_WIN_ID
                        )
                    )
                }
            })
    }

    override fun onConnected() {
    }

    override fun onDisconnected() {
        requireActivity().runOnUiThread {
            requireView().showSnackbar("disconnected from server", R.drawable.snackbar_error)
        }
    }

    override fun onMessage(message: HubMessage?) {
        val arguments = Gson().toJson(message?.arguments)
        try {
            val responseArguments = Json.decodeFromString<List<ArgumentsResponse>>(arguments)

            when (responseArguments[0].messageType) {
                MessageGame.IS_STICKERS -> {
                    lifecycleScope.launchWhenStarted {
                        val dataJson = Json {
                            ignoreUnknownKeys = true
                        }.decodeFromString<JsonDataStickersResponse>(responseArguments[0].jsonData)
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
            }


        } catch (e: Exception) {
            Log.i("errorSerializableControllerHub", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorControllerHub", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageControllerHub", message.toString())
    }


}