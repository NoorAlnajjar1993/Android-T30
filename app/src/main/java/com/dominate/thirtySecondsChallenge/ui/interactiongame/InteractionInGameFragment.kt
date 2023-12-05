package com.dominate.thirtySecondsChallenge.ui.interactiongame

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.model.gift.StickersResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.data.signalR.joingame.ArgumentJoinGame
import com.dominate.thirtySecondsChallenge.data.signalR.stickers.JsonDataStickersResponse
import com.dominate.thirtySecondsChallenge.data.signalR.stickers.SelectStickersDataRequest
import com.dominate.thirtySecondsChallenge.databinding.FragmentInteractionInGameBinding
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.interactiongame.adapter.InteractionInGameAdapter
import com.dominate.thirtySecondsChallenge.ui.interactiongame.onclickListener.DialogClickListener
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_interaction_in_game.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@AndroidEntryPoint
class InteractionInGameFragment() :
    BaseValidationDialogFragment<FragmentInteractionInGameBinding>(),
    OnItemClickListener<StickersResponseModel>,
    HubConnectionListener,
    HubEventListener {

    override fun getLayoutId(): Int = R.layout.fragment_interaction_in_game

    @Inject
    lateinit var adapter: InteractionInGameAdapter

    @Inject
    lateinit var userPref: UserPref

    private var dialogClickListener: DialogClickListener? = null

    private val viewModel by activityViewModels<GameLobbyViewModel>()

    var myApplication: MyApplication? = null

    var stickerId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getStickers().observe(viewLifecycleOwner, getStickers)

        myApplication = requireActivity().application as MyApplication
//        myApplication?.connection?.addListener(this)
    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        btn_Send.onClick {
            if (stickerId != -1) {
                setSendStickersHub(stickerId = stickerId)
                dismiss()
            } else {
                dismiss()
            }
//            dialogClickListener?.onDialogButtonClick()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (targetFragment is DialogClickListener) {
            dialogClickListener = targetFragment as DialogClickListener
        }
    }

    private fun setRecycleView() {

        val layoutManager = GridLayoutManager(requireContext(), 3)

        requireContext().let {
            adapter = InteractionInGameAdapter()
            rv_InteractionGame.layoutManager = layoutManager
            rv_InteractionGame.adapter = adapter
            adapter.onItemClickListener = this
        }
    }

    private val getStickers = Observer<Result<List<StickersResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                adapter.submitItems(it.data)
            }

            is Result.Error -> {
                CustomProgressBar.hide(requireContext())
                handleError(it.throwable)
            }

            is Result.CustomError -> {
                CustomProgressBar.hide(requireContext())
                HandleRequestFailedUtil.showDialogMessage(
                    it.message, requireContext(), childFragmentManager
                )
            }

            is Result.Loading -> {
                CustomProgressBar.show(requireContext())
            }
        }
    }

    private fun setSendStickersHub(
        stickerId: Int
    ) {
        try {
            val selectStickersDataRequest = SelectStickersDataRequest(
                GameId = viewModel.gameId.value ?: 0,
                StickerId = stickerId,
            )
            val jsonString = Json.encodeToString(selectStickersDataRequest)
            val atr = ArgumentJoinGame(
                MessageType = 20,
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


    override fun onValidationSucceeded() {

    }

    override fun onItemClicked(view: View?, item: StickersResponseModel, position: Int) {
        stickerId = item.id
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
            lifecycleScope.launchWhenStarted {
                when (responseArguments[0].messageType) {
                    MessageGame.IS_STICKERS -> {
                        val dataJson = Json {
                            ignoreUnknownKeys = true
                        }.decodeFromString<JsonDataStickersResponse>(responseArguments[0].jsonData)

                        when (dataJson.SenderId == userPref.getUser()?.id) {
                            true -> {
                                viewModel.strikeUrl.value = dataJson.Sticker.ImageUrl
                            }

                            false -> {
                                viewModel.strikeUrlPlayer.value = dataJson.Sticker.ImageUrl
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.i("errorSerializableController", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorHubController", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageController", message.toString())
    }


}