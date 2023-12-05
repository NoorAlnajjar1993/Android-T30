package com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBottomSheetDialogFragment
import com.dominate.thirtySecondsChallenge.data.model.friends.UserBlockResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.ui.friends.FriendViewModel
import com.dominate.thirtySecondsChallenge.ui.player.dialog.GiftFriendsFragment
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.dialog.showInfoDialog
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.more_friends_request_fragment.*

@AndroidEntryPoint
class MoreFriendsRequestFragment : BaseBottomSheetDialogFragment() {

    override val layoutId: Int = R.layout.more_friends_request_fragment

    companion object {
        const val playerId = "PlayerId"

        fun newInstance(argValue: Int): MoreFriendsRequestFragment {
            val fragment = MoreFriendsRequestFragment()
            val args = Bundle()
            args.putInt(playerId, argValue)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by activityViewModels<FriendViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {

    }

    private fun setUpViewsListeners() {

        tv_SendAGift.onClick {
            val giftFriendsFragment = GiftFriendsFragment.newInstance(arguments?.getInt(
                playerId, 0) ?: 0)
            giftFriendsFragment.show(parentFragmentManager, "gift Friends")
        }

        tv_SendAGame.onClick {

        }

        tv_BlockUser.onClick {
            viewModel.userBlock(arguments?.getInt(playerId,0)?:0).observe(viewLifecycleOwner,userBlock)
        }
    }

    private val userBlock = Observer<Result<List<UserBlockResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                dismiss()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}