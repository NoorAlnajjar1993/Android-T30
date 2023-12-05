package com.dominate.thirtySecondsChallenge.ui.player.sheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.buttomsheet.BaseValidationBottomSheetFragment
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.MorePlayerSheetBinding
import com.dominate.thirtySecondsChallenge.ui.player.PlayerViewModel
import com.dominate.thirtySecondsChallenge.ui.player.dialog.RemoveFriendsFragment
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.more_player_sheet.*

@AndroidEntryPoint
class MorePlayerFragment() : BaseValidationBottomSheetFragment<MorePlayerSheetBinding>() {

    override val layoutId: Int = R.layout.more_player_sheet

    private val viewModel by activityViewModels<PlayerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        if (viewModel.reportListLiveData.value.isNullOrEmpty()){
            viewModel.getReportReason().observe(viewLifecycleOwner, getReportReason)
        }

    }

    private fun setUpViewsListeners() {

        tv_RemoveFriends.onClick {
            try {
                dismiss()
                val removeFriendsFragment = RemoveFriendsFragment()
                removeFriendsFragment.show(parentFragmentManager, "remove Friends")

            } catch (e: Exception) {
                Log.i("errors", e.message.toString())
            }
        }

        tv_report.onClick {
            val reportPlayerFragment = ReportPlayerFragment()
            reportPlayerFragment.show(parentFragmentManager, "report Player Fragment")
        }

    }

    private val getReportReason = Observer<Result<List<ReportReasonResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                viewModel._reportListLiveData.postValue(it.data)
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


}