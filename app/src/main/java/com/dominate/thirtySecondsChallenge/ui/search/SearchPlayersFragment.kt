package com.dominate.thirtySecondsChallenge.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.friends.PaginationResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentSearchPlayersBinding
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.ui.search.adapter.SearchPlayersAdapter
import com.dominate.thirtySecondsChallenge.ui.selectfriendlist.model.ChooseFriendsModel
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notification.rv_Notification
import kotlinx.android.synthetic.main.fragment_search_players.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchPlayersFragment : BaseBindingFragment<FragmentSearchPlayersBinding>(),
    OnItemClickListener<UserInfoModel> {

    override fun getLayoutId(): Int = R.layout.fragment_search_players

    private val viewModel by activityViewModels<SearchPlayersViewModel>()

    @Inject
    lateinit var adapter: SearchPlayersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel
        viewModel.isEmptyList.value = true
    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

        var isTyping = false
        val handler = Handler(Looper.getMainLooper())

        et_filters.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isTyping = true
                adapter.clear()
                viewModel.isEmptyList.value = false
            }

            override fun afterTextChanged(s: Editable?) {
                if (isTyping) {
                    // User has stopped entering text
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({
                        // Perform actions after the delay (when the user stops typing)
                        // You can customize your action here
                        if (s == null || s.length < 3) {
                            // User has entered less than 3 characters or cleared the text
                            adapter.clear()
                            viewModel.isEmptyList.value = true
                        } else if (s.length == 3) {
                            // User has entered 3 or more characters
                            viewModel.isEmptyList.value = false
                             viewModel.getPagination(1, s.toString()).observe(viewLifecycleOwner, getPagination)
                        }else{
                            viewModel.isEmptyList.value = false
                            viewModel.getPagination(1, s.toString()).observe(viewLifecycleOwner, getPagination)
                        }
                        isTyping = false
                    }, 1000)
                }
            }
        })

    }

    private fun setRecycleView() {
        requireContext().let {
            adapter = SearchPlayersAdapter()
            rv_SearchFriends.adapter = adapter
            adapter.onItemClickListener = this
        }

    }

    private val getPagination = Observer<Result<PaginationResponseModel>> {
        when (it) {
            is Result.Success -> {
                hideLoadingView(requireContext())
                if (!it.data?.userList.isNullOrEmpty()) {
                    adapter.submitItems(it.data?.userList)
                } else {
                    adapter.clear()
                    viewModel.isEmptyList.value = true
                }

            }

            is Result.Error -> {
                hideLoadingView(requireContext())
                handleError(it.throwable)
            }

            is Result.CustomError -> {
                hideLoadingView(requireContext())
                HandleRequestFailedUtil.showDialogMessage(
                    it.message, requireContext(), childFragmentManager
                )
            }

            is Result.Loading -> {
                showLoadingView(requireContext())
//                CustomProgressBar.show(requireContext())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: UserInfoModel, position: Int) {
        val playerProfileFragment = PlayerProfileFragment.newInstance(item.id)
        playerProfileFragment.show(parentFragmentManager, "player Profile Fragment")
    }

}