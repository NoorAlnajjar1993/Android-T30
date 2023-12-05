package com.dominate.thirtySecondsChallenge.ui.friends.friendgroup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentFriendsGroupBinding
import com.dominate.thirtySecondsChallenge.ui.friends.FriendViewModel
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.adapter.ItemFriendsAdapter
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.model.ItemFriendsModel
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_friends_group.rv_ItemFriends
import kotlinx.android.synthetic.main.fragment_loyalty.*
import javax.inject.Inject

@AndroidEntryPoint
class FriendsGroupFragment : BaseBindingFragment<FragmentFriendsGroupBinding>(),
    OnItemClickListener<ItemFriendsModel> {

    override fun getLayoutId(): Int = R.layout.fragment_friends_group

    private val viewModel by activityViewModels<FriendViewModel>()

    @Inject
    lateinit var adapter: ItemFriendsAdapter

    lateinit var navController: NavController

    val CREATE_PRIVATE_GAME_ID = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setupNavigation()
        setUpViewsListeners()
        setRecycleView()
    }

    private fun setUpData() {
        binding?.viewModel = viewModel

//        viewModel.getAllUserFriends()
        viewModel.getAllRequest()

        viewModel.isInvite.observe(
            viewLifecycleOwner
        ) { items ->
            if (items == true) {
                findNavController().navigate(
                    FriendsGroupFragmentDirections.actionGlobalGameLobbyFragment(
                        CREATE_PRIVATE_GAME_ID
                    )
                )
                viewModel._isInvite.value = false
            }
        }
    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

    }

    private fun setupNavigation() {
        requireActivity().findNavController(R.id.friendsNavContainer)
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.friendsListFragment -> {
                    }

                    R.id.friendsRequestFragment -> {
                    }

                    else -> {
                    }
                }
            }
    }


    private fun setRecycleView() {
        requireContext().let {
            adapter = ItemFriendsAdapter()
            rv_ItemFriends.adapter = adapter
            adapter.onItemClickListener = this
            adapter.clear()
            adapter.submitItems(
                listOf(
                    ItemFriendsModel(
                        "الأصدقاء",
                        "",
                        true
                    ),
                    ItemFriendsModel(
                        "طلبات الصداقة",
                        "",
                        false
                    ),
                )
            )
        }
    }

    private val getAllRequest = Observer<Result<List<UserFriendsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
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


    private fun setStartDestination(Fragment: Int) {

        navController = requireActivity().findNavController(R.id.friendsNavContainer)
        val navGraph = navController.navInflater.inflate(R.navigation.friends_nav_graph)
        navGraph.startDestination = Fragment
        navController.graph = navGraph

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: ItemFriendsModel, position: Int) {

        when (item.itemName) {
            "الأصدقاء" -> {
                setStartDestination(R.id.friendsListFragment)
            }

            "طلبات الصداقة" -> {
                setStartDestination(R.id.friendsRequestFragment)
            }
        }

    }

}