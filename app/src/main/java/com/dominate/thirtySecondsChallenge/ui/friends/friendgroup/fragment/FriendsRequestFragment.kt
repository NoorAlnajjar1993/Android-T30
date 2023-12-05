package com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentFriendsRequestBinding
import com.dominate.thirtySecondsChallenge.ui.friends.FriendViewModel
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.adapter.FriendsRequestAdapter
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.sheet.MoreFriendsRequestFragment
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_friends_list.rv_FriendsList
import javax.inject.Inject

@AndroidEntryPoint
class FriendsRequestFragment : BaseBindingFragment<FragmentFriendsRequestBinding>(),
    OnItemClickListener<UserFriendsResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_friends_request

    private val viewModel by activityViewModels<FriendViewModel>()

    @Inject
    lateinit var adapter: FriendsRequestAdapter

    @Inject
    lateinit var userPref: UserPref

    var oldRequestLiveData: List<UserFriendsResponseModel> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpViewsListeners()
        setUpData()
        setRecycleView()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getAllRequest().observe(viewLifecycleOwner,getAllRequest)

    }

    private fun setUpViewsListeners() {

    }

    private fun setRecycleView() {
        requireContext().let {
            adapter = FriendsRequestAdapter()
            rv_FriendsList.adapter = adapter
            adapter.onItemClickListener = this

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val getAllRequest = Observer<Result<List<UserFriendsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                if (it.data.isNullOrEmpty()) {
                    adapter.clear()
                    viewModel.isEmptyRequestFriends.value = true
                } else {
                    viewModel.isEmptyRequestFriends.value = false
                    adapter.submitItems(it.data)
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
                if (viewModel.allRequestLiveData.value.isNullOrEmpty())
                    CustomProgressBar.show(requireContext())
            }
        }
    }

    private val acceptRequest = Observer<Result<List<UserFriendsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                requireView().showSnackbar(it.message.toString(),R.drawable.snackbar)
                if (it.data.isNullOrEmpty()) {
                    adapter.clear()
                    viewModel.isEmptyRequestFriends.value = true
                } else {
                    viewModel.isEmptyRequestFriends.value = false
                    adapter.submitItems(it.data)
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
                if (viewModel.allRequestLiveData.value.isNullOrEmpty())
                    CustomProgressBar.show(requireContext())
            }
        }
    }

    private val rejectRequest = Observer<Result<List<UserFriendsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                requireView().showSnackbar(it.message.toString(),R.drawable.snackbar)
                if (it.data.isNullOrEmpty()) {
                    adapter.clear()
                    viewModel.isEmptyRequestFriends.value = true
                } else {
                    viewModel.isEmptyRequestFriends.value = false
                    adapter.submitItems(it.data)
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
                if (viewModel.allRequestLiveData.value.isNullOrEmpty())
                    CustomProgressBar.show(requireContext())
            }
        }
    }

    override fun onItemClicked(view: View?, item: UserFriendsResponseModel, position: Int) {
        val userFriendsId = if (userPref.getUser()?.id == item.userPlayerId){
            item.friendUserPlayerId
        }else{
            item.userPlayerId
        }

        when (view?.id) {

            R.id.iv_more_request -> {

                val moreFriendsRequestFragment = MoreFriendsRequestFragment.newInstance(userFriendsId)
                moreFriendsRequestFragment.show(parentFragmentManager, "more Friends List Fragment")
            }

            R.id.cv_Accept ->{
                viewModel.acceptRequest(userFriendsId).observe(viewLifecycleOwner, acceptRequest)
            }

            R.id.cv_Reject ->{
                viewModel.rejectRequest(userFriendsId).observe(viewLifecycleOwner, rejectRequest)
            }

            else ->{
                val playerProfileFragment = PlayerProfileFragment.newInstance(userFriendsId)
                playerProfileFragment.show(parentFragmentManager, "player Profile Fragment")
            }

        }

    }

}