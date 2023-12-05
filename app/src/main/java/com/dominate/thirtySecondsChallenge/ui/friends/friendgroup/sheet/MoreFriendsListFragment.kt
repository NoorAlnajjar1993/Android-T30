package com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.sheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBottomSheetDialogFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.model.friends.UserBlockResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.data.signalR.creategame.Argument
import com.dominate.thirtySecondsChallenge.data.signalR.creategame.GameRequestModel
import com.dominate.thirtySecondsChallenge.data.signalR.creategame.JsonDataCreateGameResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.ui.friends.FriendViewModel
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.ui.player.dialog.GiftFriendsFragment
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.dialog.showInfoDialog
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.more_friends_list_fragment.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MoreFriendsListFragment() : BaseBottomSheetDialogFragment(),
    HubConnectionListener,
    HubEventListener {

    override val layoutId: Int = R.layout.more_friends_list_fragment

    companion object {
        const val playerId = "PlayerId"
        const val CREATE_PRIVATE_GAME_ID = 2

        fun newInstance(argValue: Int): MoreFriendsListFragment {
            val fragment = MoreFriendsListFragment()
            val args = Bundle()
            args.putInt(playerId, argValue)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by activityViewModels<FriendViewModel>()
    private val gameLobbyViewModel by activityViewModels<GameLobbyViewModel>()

    var myApplication: MyApplication? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {

        myApplication = requireActivity().application as MyApplication
        myApplication?.connection?.addListener(this)

    }

    private fun setUpViewsListeners() {

        iv_SendGift.onClick {
            val giftFriendsFragment = GiftFriendsFragment.newInstance(
                arguments?.getInt(
                    playerId, 0
                ) ?: 0
            )
            giftFriendsFragment.show(parentFragmentManager, "gift Friends")
        }

        tv_SendAGame.onClick {
            createGameHub()
        }

        tv_RemoveAFriends.onClick {
            viewModel.unfriendRequest(arguments?.getInt(PlayerProfileFragment.playerId, 0) ?: 0)
                .observe(viewLifecycleOwner, unfriendRequest)

        }

        tv_BlockUser.onClick {
            viewModel.userBlock(arguments?.getInt(PlayerProfileFragment.playerId, 0) ?: 0)
                .observe(viewLifecycleOwner, userBlock)

        }

    }

    private val unfriendRequest = Observer<Result<List<UserFriendsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                dismiss()
                viewModel._isRemoveFriendsLiveData.value = true
            }

            is Result.Error -> {
                CustomProgressBar.hide(requireContext())
                requireContext().showInfoDialog(it.throwable.toString())
            }

            is Result.CustomError -> {
                CustomProgressBar.hide(requireContext())
                HandleRequestFailedUtil.showDialogMessage(
                    it.message, requireContext(), childFragmentManager
                )
            }

            is Result.Loading -> {
                if (viewModel.allRequestLiveData.value.isNullOrEmpty())
                    CustomProgressBar.show(requireContext())
            }
        }
    }

    private val userBlock = Observer<Result<List<UserBlockResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                dismiss()
                viewModel._isRemoveFriendsLiveData.value = true
            }

            is Result.Error -> {
                CustomProgressBar.hide(requireContext())
                requireContext().showInfoDialog(it.throwable.toString())
            }

            is Result.CustomError -> {
                CustomProgressBar.hide(requireContext())
                HandleRequestFailedUtil.showDialogMessage(
                    it.message, requireContext(), childFragmentManager
                )
            }

            is Result.Loading -> {
                if (viewModel.allRequestLiveData.value.isNullOrEmpty())
                    CustomProgressBar.show(requireContext())
            }
        }
    }

    private fun createGameHub() {
        try {
            val req = GameRequestModel(
                GameTypeId = CREATE_PRIVATE_GAME_ID // id 2 private game
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
            requireView().showSnackbar(e.message.toString(), R.drawable.snackbar_error)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                MessageGame.CREATE_GAME_ID -> {
                    val dataJson =
                        Json.decodeFromString<JsonDataCreateGameResponse>(responseArguments[0].jsonData)
                    if (dataJson.FirstPlayerId != 0) {
                        lifecycleScope.launchWhenStarted {
                            gameLobbyViewModel.gameId.value = dataJson.Id
                            viewModel._isInvite.value = true
                        }
                    } else {
                        requireActivity().runOnUiThread(Runnable {
                            shortToast("Created game Private not Success!")
                        })
                    }
                }
            }

        } catch (e: Exception) {
            Log.i("errorSerializableCreateGame", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorHubCreateGamePrivate", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageCreateGamePrivate", message.toString())
    }

}