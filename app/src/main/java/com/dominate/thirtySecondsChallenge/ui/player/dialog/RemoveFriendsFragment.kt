package com.dominate.thirtySecondsChallenge.ui.player.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentRemoveFriendsBinding
import com.dominate.thirtySecondsChallenge.ui.player.PlayerViewModel
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.dialog.showInfoDialog
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_remove_friends.*


@AndroidEntryPoint
class RemoveFriendsFragment() : BaseValidationDialogFragment<FragmentRemoveFriendsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_remove_friends

    private val viewModel by activityViewModels<PlayerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
    }

    private fun setUpData() {
       binding?.viewModel = viewModel

    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        tv_RemoveRequestFriends.onClick {
            viewModel.unfriendRequest().observe(viewLifecycleOwner, unfriendRequest)
        }

    }

    private val unfriendRequest = Observer<Result<List<UserFriendsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                shortToast(it.message.toString())
                dismiss()
                viewModel._isSendGiftLiveData.value = true
            }

            is Result.Error -> {
                requireContext().showInfoDialog(it.throwable.toString())
            }

            is Result.CustomError -> {
                HandleRequestFailedUtil.showDialogMessage(
                    it.message, requireContext(), childFragmentManager
                )
            }

            is Result.Loading -> {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onValidationSucceeded() {

    }

}