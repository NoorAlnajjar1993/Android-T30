package com.dominate.thirtySecondsChallenge.ui.friends.leader

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.leader.LeaderboardFiltersModel
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentLeaderBoardsBinding
import com.dominate.thirtySecondsChallenge.ui.friends.FriendViewModel
import com.dominate.thirtySecondsChallenge.ui.friends.leader.adapter.LeaderBoardAdapter
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_leader_boards.*
import javax.inject.Inject


@AndroidEntryPoint
class LeaderBoardsFragment : BaseBindingFragment<FragmentLeaderBoardsBinding>(),
    OnItemClickListener<UserInfoModel> {

    override fun getLayoutId(): Int = R.layout.fragment_leader_boards

    private val viewModel by activityViewModels<FriendViewModel>()

    @Inject
    lateinit var leaderBoardAdapter: LeaderBoardAdapter

    var animation: LayoutAnimationController? = null
    var checkTime = true
    var leaderboardId = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setRecycleView()
        setUpViewsListeners()
    }

    override fun onViewVisible() {
        super.onViewVisible()

        setUpData()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getLeaderboardFilters().observe(viewLifecycleOwner, getLeaderboardFilters)
    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

    }

    private fun setUpSpinner(map: List<String>) {

        var arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            map
        )

        hs_leaderFilter.adapter = arrayAdapter
        hs_leaderFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                item: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    if (!checkTime) {
                        AudioPlayer(requireContext(), R.raw.minor_t30).startPlayback()
                    } else {
                        checkTime = false
                    }
                    leaderboardId = viewModel.leaderboardFilters[position].id
                    viewModel.getLeaderboard(viewModel.leaderboardFilters[position].id)
                        .observe(viewLifecycleOwner, getLeaderboard)
                } catch (e: Exception) {
                    Log.i("error", e.message.toString())
                }
            }
        }
    }


    private fun setRecycleView() {
        animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_down)

        requireContext().let {
            leaderBoardAdapter = LeaderBoardAdapter()
            rvLeaderBoard.adapter = leaderBoardAdapter
            leaderBoardAdapter.onItemClickListener = this
        }

    }

    private val getLeaderboardFilters = Observer<Result<List<LeaderboardFiltersModel>>> {
        when (it) {
            is Result.Success -> {
                if (!it.data.isNullOrEmpty()) {
                    viewModel.leaderboardFiltersEntry.addAll(it.data.map { it.title })
                    viewModel.leaderboardFilters.addAll(it.data ?: arrayListOf())
                    viewModel.selectedPosition.value = 0
                    setUpSpinner(it.data.map { it.title })
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

    private val getLeaderboard = Observer<Result<List<UserInfoModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                if (!it.data.isNullOrEmpty()) {
                    it.data.forEachIndexed { index, userInfoModel ->

                        userInfoModel.idBoard = leaderboardId
                        userInfoModel.countNumber = (index + 1).toString()

                        when (index) {

                            0 -> {
                                userInfoModel.isTop = true
                                userInfoModel.color = "#FFD94A"
                            }

                            1 -> {
                                userInfoModel.isTop = true
                                userInfoModel.color = "#CECDD2"
                            }

                            2 -> {
                                userInfoModel.isTop = true
                                userInfoModel.color = "#F4AD6F"
                            }

                        }

                    }
                    leaderBoardAdapter.submitItems(it.data)
//                    rvLeaderBoard.layoutAnimation = animation
//                    rvLeaderBoard.scheduleLayoutAnimation()
                } else {
                    leaderBoardAdapter.submitItems(arrayListOf())
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