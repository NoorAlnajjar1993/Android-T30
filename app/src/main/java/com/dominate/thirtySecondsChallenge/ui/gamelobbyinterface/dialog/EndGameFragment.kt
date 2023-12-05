package com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.signalR.exitgame.ExitGameResultModel
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.databinding.FragmentEndGameBinding
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_end_game.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@AndroidEntryPoint
class EndGameFragment() : BaseValidationDialogFragment<FragmentEndGameBinding>(),
    HubConnectionListener,
    HubEventListener {

    override fun getLayoutId(): Int = R.layout.fragment_end_game

    private val viewModel by activityViewModels<GameLobbyViewModel>()

    @Inject
    lateinit var userPref: UserPref

    var myApplication: MyApplication? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel
        this.isCancelable = false

        startConnection()
        sound()
        exitGameHub()
    }

    private fun startConnection(){
        myApplication = requireActivity().application as MyApplication
        myApplication?.connection?.addListener(this)
    }

    private fun sound(){
        AudioPlayer(requireContext(), R.raw.exit_the_game_t30).startPlayback()
    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        btn_Back.onClick {
            dismiss()
            findNavController().navigate(EndGameFragmentDirections.actionGlobalHomeFragment())
        }

    }

    private fun exitGameHub() {
        try {
            //Todo: Send Message -> Exit Game Hub
            myApplication?.connection?.invoke("SendMessage", viewModel.exitGame())
        } catch (e: Exception) {
            requireView().showSnackbar(e.message.toString(), R.drawable.snackbar_error)
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
        requireActivity().runOnUiThread {
            view?.showSnackbar("disconnected from server", R.drawable.snackbar)
        }
    }

    override fun onMessage(message: HubMessage?) {
        try {
            val arguments = Gson().toJson(message?.arguments)
            val responseArguments = Json.decodeFromString<List<ArgumentsResponse>>(arguments)

            when (responseArguments[0].messageType) {

                MessageGame.EXIT_GAME ->{
                    val dataJson = Json.decodeFromString<ExitGameResultModel>(responseArguments[0].jsonData)
                }
            }
        } catch (e: Exception) {
            Log.i("errorSerializableExitGameHub", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorExitGameHub", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageExitGameHub", message.toString())
    }

}