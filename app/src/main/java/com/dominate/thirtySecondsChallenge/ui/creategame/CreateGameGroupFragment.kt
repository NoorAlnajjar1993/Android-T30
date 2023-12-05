package com.dominate.thirtySecondsChallenge.ui.creategame

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.signalR.creategame.Argument
import com.dominate.thirtySecondsChallenge.data.signalR.creategame.GameRequestModel
import com.dominate.thirtySecondsChallenge.data.signalR.creategame.JsonDataCreateGameResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.databinding.FragmentCreateGameGroupBinding
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.home.HomeViewModel
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.anim.zoomOut
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_game_group.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@AndroidEntryPoint
class CreateGameGroupFragment() : BaseValidationDialogFragment<FragmentCreateGameGroupBinding>(),
    HubConnectionListener,
    HubEventListener {

    override fun getLayoutId(): Int = R.layout.fragment_create_game_group

    private val viewModel by activityViewModels<GameLobbyViewModel>()
    private val homeViewModel by activityViewModels<HomeViewModel>()

    var myApplication: MyApplication? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners(view)
    }

    private fun setUpData() {
        binding?.viewModel = viewModel
        binding?.homeViewModel = homeViewModel

        isCancelable = false

        homeViewModel.activeGameTypesLiveData.observe(
            viewLifecycleOwner
        ) { items ->
            homeViewModel.gameTypesEntry.value = items.map { it.title }
            homeViewModel.selectedGameTypePosition.value = 0
        }

        viewModel.typeGame.value = 1
        AudioPlayer(requireContext(), R.raw.create_game_popup_t30).startPlayback()

        myApplication = requireActivity().application as MyApplication
        myApplication?.connection?.addListener(this)

    }

    private fun setUpViewsListeners(view: View) {

        iv_close.onClick {
            requireContext().zoomOut(requireView(), this)
        }

        tv_PublicGame.onClick {
            viewModel.typeGame.value = 1
        }

        tv_PrivateGame.onClick {
            viewModel.typeGame.value = 2
        }

        btn_createGame.onClick {
            createGameHub()
        }

    }

    private fun createGameHub() {
        try {
            val req = GameRequestModel(
                GameTypeId = viewModel.typeGame.value?.toInt() ?: 1
            )
            val jsonString = Json.encodeToString(req)
            val atr = Argument(
                Message = "Create Game",
                MessageType = 13,
                JsonData = jsonString
            )
            Log.i("logs", jsonString)
            myApplication?.connection?.invoke("SendMessage", atr)

        } catch (e: Exception) {
            requireView().showSnackbar( e.message.toString(), R.drawable.snackbar_error)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onValidationSucceeded() {

    }

    override fun onConnected() {
    }

    override fun onDisconnected() {
    }

    override fun onMessage(message: HubMessage?) {
        try {
            val arguments = Gson().toJson(message?.arguments)
            val responseArguments = Json.decodeFromString<List<ArgumentsResponse>>(arguments)

            when (responseArguments[0].messageType) {
                // 8 create game
                MessageGame.CREATE_GAME_ID -> {
                    val dataJson= Json.decodeFromString<JsonDataCreateGameResponse>(responseArguments[0].jsonData)
                    if (dataJson.FirstPlayerId != 0) {
                        lifecycleScope.launchWhenStarted {
                            viewModel.gameId.value = dataJson.Id
                            findNavController().navigate(
                                CreateGameGroupFragmentDirections.actionGlobalGameLobbyFragment(
                                    viewModel.typeGame.value?.toInt() ?: 1
                                )
                            )
                        }
                    } else {
                        requireActivity().runOnUiThread(Runnable {
                            shortToast("Created game not Success!")
                        })
                    }

                }

            }

        } catch (e: Exception) {
            Log.i("errorSerializableCreateGame", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorHubCreateGame", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageCreateGame", message.toString())
    }
}