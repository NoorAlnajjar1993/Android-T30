package com.dominate.thirtySecondsChallenge.ui.player

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.buttomsheet.BaseValidationBottomSheetFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.FriendshipStatusResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BadgesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest.JsonDataFriendsRequestResponse
import com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest.JsonResponseError
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame.ERROR_HUB
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame.SEND_FRIEND_REQUEST
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame.SEND_GIFT
import com.dominate.thirtySecondsChallenge.databinding.FragmentPlayerProfileBinding
import com.dominate.thirtySecondsChallenge.ui.player.dialog.GiftFriendsFragment
import com.dominate.thirtySecondsChallenge.ui.player.sheet.MorePlayerFragment
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.AchievementAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.BadgesAdapter
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbarFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_player_profile.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

@AndroidEntryPoint
class PlayerProfileFragment() : BaseValidationBottomSheetFragment<FragmentPlayerProfileBinding>(),
    HubConnectionListener,
    HubEventListener {

    override val layoutId: Int = R.layout.fragment_player_profile

    companion object {
        const val playerId = "PlayerId"

        fun newInstance(argValue: Int): PlayerProfileFragment {
            val fragment = PlayerProfileFragment()
            val args = Bundle()
            args.putInt(playerId, argValue)
            fragment.arguments = args
            return fragment
        }

        const val FRIEND_STATUS_ID_PENDING = 16
        const val FRIEND_STATUS_ID_ACCEPTED = 17
        const val FRIEND_STATUS_ID_REJECTED = 18
    }

    private val viewModel by activityViewModels<PlayerViewModel>()

    lateinit var badgesAdapter: AchievementAdapter

    lateinit var giftAdapter: BadgesAdapter

    var myApplication: MyApplication? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setRecycleView()
        setOnClick()
    }

    override fun onViewVisible(view: View) {
        super.onViewVisible(view)
        setUpData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it1 ->
                val behaviour = BottomSheetBehavior.from(it1)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                behaviour.isDraggable = true
            }
        }
        return dialog
    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.clearData()

        viewModel.getPlayerInfo(arguments?.getInt(playerId, 0) ?: 0)
            .observe(viewLifecycleOwner, getPlayerInfo)

        viewModel.isSendGiftLiveData.observe(
            viewLifecycleOwner
        ) { items ->
            if (items == true) {
                viewModel.getPlayerInfo(arguments?.getInt(playerId, 0) ?: 0)
                    .observe(viewLifecycleOwner, getPlayerInfo)
                viewModel._isSendGiftLiveData.value = false
                CustomProgressBar.hide(requireContext())
            }
        }

        myApplication = requireActivity().application as MyApplication
        myApplication?.connection?.addListener(this)
    }

    private fun setOnClick() {

        iv_close.onClick {
            dismiss()
        }

        iv_more.onClick {
            val morePlayerFragment = MorePlayerFragment()
            morePlayerFragment.show(parentFragmentManager, "more Player Fragment")
        }

        cv_giftPlayer.onClick {
            val giftFriendsFragment =
                GiftFriendsFragment.newInstance(arguments?.getInt(playerId, 0) ?: 0)
            giftFriendsFragment.show(parentFragmentManager, "gift Friends")
        }

        cv_statusPlayer.onClick {

            when (viewModel.statusPlayerId.value) {
                1 -> {
                    sendRequestFriendsHub()
//                    viewModel.sendRequest()
//                        .observe(viewLifecycleOwner, sendRequest)
                }

                2 -> {
                    viewModel.deleteFriendRequest()
                        .observe(viewLifecycleOwner, deleteFriendRequest)
                }

                3 -> {
                }

                else -> {
                }

            }

        }

//        ivPerson.onClick {
//            requireContext().showDialog(
//                R.drawable.ic_jawaher,
//                "هدية من",
//                "Hassan Hasanat",
//                isShowBtn = true,
//                onPositiveButtonClick = {
//                    it.dismiss()
//                    val giftFriendsFragment = GiftFriendsFragment()
//                    giftFriendsFragment.show(parentFragmentManager, "gift Friends")
//                },
//                btnText = "ارسال هدية"
//            )
//        }

    }

    private fun sendRequestFriendsHub() {
        try {
            Log.i("xcxcxcxc", viewModel.sendRequestFriendsHub().toString())
            myApplication?.connection?.invoke("SendMessage", viewModel.sendRequestFriendsHub())
        } catch (e: Exception) {
            Log.i("errorRequestFriendsHub", e.message.toString())
        }
    }

    private fun setRecycleView() {

        val layoutManager = GridLayoutManager(requireContext(), 4)

        requireContext().let {
            badgesAdapter = AchievementAdapter(it, 1, 111)
            rv_Badges.layoutManager = layoutManager
            rv_Badges.adapter = badgesAdapter
            val badgesList = mutableListOf<BadgesResponseModel>()
            repeat(4) {
                badgesList.add(
                    BadgesResponseModel(
                        id = -1,
                        userPlayerId = -1,
                        isVisible = false,
                        badgeId = -1,
                        image = null,
                        imageUrl = null,
                        language = "ar"
                    )
                )
            }
            badgesAdapter.submitItems(badgesList)
        }

        requireContext().let {
            giftAdapter = BadgesAdapter(requireContext(), 1, 111)
            rv_Gifts.adapter = giftAdapter
            val giftList = mutableListOf<GiftProfileResponseModel>()
            repeat(4) {
                giftList.add(
                    GiftProfileResponseModel(
                        id = -1,
                        userPlayerId = -1,
                        isVisible = false,
                        giftId = -1,
                        image = null,
                        imageUrl = null,
                        title = "",
                        description = "",
                        isFree = false,
                        price = 1.1f,
                        count = 0,
                        language = "ar"
                    )
                )
            }
            giftAdapter.submitItems(giftList)
        }


    }

    private val getPlayerInfo = Observer<Result<ProfileResponseModel>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                viewModel.playerNameId.value = it.data?.userInfo?.id ?: 0
                viewModel.friendshipStatus()
                    .observe(viewLifecycleOwner, friendshipStatus)

                if (!it.data?.badges.isNullOrEmpty()) {
                    badgesAdapter.clear()
                    badgesAdapter.submitItems(it.data?.badges ?: arrayListOf())
                }

                if (!it.data?.gifts.isNullOrEmpty()) {
                    giftAdapter.clear()
                    giftAdapter.submitItems(it.data?.gifts ?: arrayListOf())
                }

            }

            is Result.Error -> {
                dismiss()
                CustomProgressBar.hide(requireContext())
                handleError(it.throwable)
            }

            is Result.CustomError -> {
                dismiss()
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

    private val sendRequest = Observer<Result<UserFriendsResponseModel>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                shortToast(it.message)
                viewModel.getPlayerInfo(arguments?.getInt(playerId, 0) ?: 0)
                    .observe(viewLifecycleOwner, getPlayerInfo)
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

    private val friendshipStatus = Observer<Result<FriendshipStatusResponseModel>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                when (it.data?.friendStatusId) {
                    FRIEND_STATUS_ID_PENDING -> {
                        viewModel.statusPlayerId.value = 2
                        viewModel.statusPlayer.value = "أُرسل طلب الصداقة"
                    }

                    FRIEND_STATUS_ID_ACCEPTED -> {
                        viewModel.statusPlayerId.value = 3
                        viewModel.statusPlayer.value = "صديقك"
                    }

                    FRIEND_STATUS_ID_REJECTED -> {
                        viewModel.statusPlayerId.value = 1
                        viewModel.statusPlayer.value = "اضافة صديق"
                    }

                    else -> {
                        viewModel.statusPlayerId.value = 1
                        viewModel.statusPlayer.value = "اضافة صديق"
                    }

                }

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
            }
        }
    }

    private val deleteFriendRequest = Observer<Result<List<UserFriendsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                showSnackbarFragment(
                    it.message,
                    R.drawable.snackbar_success
                )
                viewModel.getPlayerInfo(arguments?.getInt(playerId, 0) ?: 0)
                    .observe(viewLifecycleOwner, getPlayerInfo)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onValidationSucceeded() {

    }

    override fun onConnected() {
    }

    override fun onDisconnected() {
        requireActivity().runOnUiThread {
            requireView().showSnackbar("disconnected from server", R.drawable.snackbar_error)
        }
    }

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override fun onMessage(message: HubMessage?) {
        val argumentsResponse = Gson().toJson(message?.arguments)
        val responseArguments = json.decodeFromString<List<ArgumentsResponse>>(argumentsResponse)
        try {
            when (responseArguments[0].messageType) {
                SEND_FRIEND_REQUEST -> {
                    val dataJson =
                        json.decodeFromString<JsonDataFriendsRequestResponse>(responseArguments[0].jsonData)

                    lifecycleScope.launchWhenCreated {
                        viewModel.getPlayerInfo(arguments?.getInt(playerId, 0) ?: 0)
                            .observe(viewLifecycleOwner, getPlayerInfo)
                        showSnackbarFragment(
                            dataJson.Message.toString(),
                            R.drawable.snackbar_success
                        )
                    }
                }

                SEND_GIFT -> {
                    val dataJson = json.decodeFromString<JsonDataFriendsRequestResponse>(responseArguments[0].jsonData)
                    lifecycleScope.launchWhenCreated {
                        AudioPlayer(requireContext(), R.raw.sending_a_gift_t30).startPlayback()
                        showSnackbarFragment(
                            dataJson.Message.toString(),
                            R.drawable.snackbar_success
                        )
                    }
                }

                ERROR_HUB -> {
                    lifecycleScope.launchWhenCreated {
                        val dataJson = Json {
                            ignoreUnknownKeys = true
                        }.decodeFromString<JsonResponseError>(responseArguments[0].jsonData)
                        if (responseArguments[0].messageType == 0) {
                            showSnackbarFragment(
                                dataJson.Message,
                                R.drawable.snackbar_error
                            )
                        }
                    }

                }
            }
        } catch (e: Exception) {
            Log.i("errorSerializableFriends", e.message.toString())
        }
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorFriendsHub", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageFriendsHub", message.toString())
    }

}