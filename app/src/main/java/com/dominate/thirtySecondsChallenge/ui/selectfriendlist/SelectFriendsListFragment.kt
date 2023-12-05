package com.dominate.thirtySecondsChallenge.ui.selectfriendlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentSelectFriendsListBinding
import com.dominate.thirtySecondsChallenge.ui.player.PlayerViewModel
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileViewModel
import com.dominate.thirtySecondsChallenge.ui.selectfriendlist.adapter.ChooseFriendsAdapter
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_players.et_filters
import kotlinx.android.synthetic.main.fragment_select_friends_list.*
import javax.inject.Inject

@AndroidEntryPoint
class SelectFriendsListFragment : BaseBindingFragment<FragmentSelectFriendsListBinding>(),
    OnItemClickListener<UserFriendsResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_select_friends_list

    private val viewModel by activityViewModels<ProfileViewModel>()
    private val playerViewModel by activityViewModels<PlayerViewModel>()

    @Inject
    lateinit var userPref: UserPref

    lateinit var adapter: ChooseFriendsAdapter
    var myApplication: MyApplication? = null
    var userFriendsId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel
        viewModel.getAllUserFriends().observe(viewLifecycleOwner, getAllUserFriends)
        myApplication = requireActivity().application as MyApplication

    }

    private fun setUpViewsListeners() {

        et_filters.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                try {
                    adapter.filter.filter(s)
                } catch (e: Exception) {
                    Log.i("error", e.message!!)
                }
            }
        })

        iv_back.onClick {
            findNavController().popBackStack()
        }

        cb_SelectAll.onClick {

            when (cb_SelectAll.isChecked) {

                true -> {
                    adapter.items.forEach {
                        it.isCheck = true
                    }
                }

                false -> {
                    adapter.items.forEach {
                        it.isCheck = false
                    }
                }

            }
            adapter.notifyDataSetChanged()
        }

        btn_SendGift.onClick {
            if (userFriendsId == -1) {
                requireView().showSnackbar("قم بإختيار صديق", R.drawable.snackbar)
            } else {
                if (myApplication?.isConnected == true) {
                    myApplication?.connection?.invoke(
                        "SendMessage", playerViewModel.sendGiftHub(
                            userFriendsId
                        )
                    )
                    userFriendsId = -1
                    adapter.items.forEach {
                        it.isCheck = false
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    shortToast("No connection")
                }
            }

        }

    }

    private fun setRecycleView() {

        requireContext().let {
            adapter = ChooseFriendsAdapter(arrayListOf())
            rv_SelectFriendsList.adapter = adapter
            adapter.onItemClickListener = this
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
                    adapter = ChooseFriendsAdapter(it.data as ArrayList<UserFriendsResponseModel>)
                    rv_SelectFriendsList.adapter = adapter
                    adapter.onItemClickListener = this
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
                CustomProgressBar.show(requireContext())
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: UserFriendsResponseModel, position: Int) {
//        cb_SelectAll.isChecked = false
//        item.isCheck = item.isCheck != true
        userFriendsId = if (userPref.getUser()?.id == item.userPlayerId) {
            item.friendUserPlayerId
        } else {
            item.userPlayerId
        }
        adapter.notifyItemChanged(position)
    }

}