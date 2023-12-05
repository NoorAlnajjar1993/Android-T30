package com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentFriendsListBinding
import com.dominate.thirtySecondsChallenge.ui.friends.FriendViewModel
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.adapter.FriendsListAdapter
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.sheet.MoreFriendsListFragment
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_friends_list.*
import javax.inject.Inject

@AndroidEntryPoint
class FriendsListFragment : BaseBindingFragment<FragmentFriendsListBinding>(),
    OnItemClickListener<UserFriendsResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_friends_list

    private val viewModel by activityViewModels<FriendViewModel>()

    @Inject
    lateinit var adapter: FriendsListAdapter

    @Inject
    lateinit var userPref: UserPref

    var oldRequestLiveData: List<UserFriendsResponseModel> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setRecycleView()
        setUpData()
        setUpViewsListeners()


    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getAllUserFriends().observe(viewLifecycleOwner,getAllUserFriends)

        viewModel.isRemoveFriendsLiveData.observe(
            viewLifecycleOwner
        ) { items ->
            if (items == true){
                viewModel.getAllUserFriends().observe(viewLifecycleOwner,getAllUserFriends)
                viewModel._isRemoveFriendsLiveData.value = false
            }
        }

    }

    private val getAllUserFriends = Observer<Result<List<UserFriendsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                if (it.data.isNullOrEmpty()) {
                    adapter.clear()
                    viewModel.isEmptyListFriends.value = true
                } else {
                    viewModel.isEmptyListFriends.value = false
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

    private fun setUpViewsListeners() {

    }

    private fun setRecycleView() {
        requireContext().let {
            adapter = FriendsListAdapter()
            rv_FriendsList.adapter = adapter
            adapter.onItemClickListener = this
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: UserFriendsResponseModel, position: Int) {

        val userFriendsId = if (userPref.getUser()?.id == item.userPlayerId){
            item.friendUserPlayerId
        }else{
            item.userPlayerId
        }

        when(view?.id){

            R.id.iv_more ->{
                val moreFriendsListFragment = MoreFriendsListFragment.newInstance(userFriendsId)
                moreFriendsListFragment.show(parentFragmentManager, "more Friends List Fragment")
            }

            else ->{
                val playerProfileFragment = PlayerProfileFragment.newInstance(userFriendsId)
                playerProfileFragment.show(parentFragmentManager, "player Profile Fragment")
            }

        }


    }

}