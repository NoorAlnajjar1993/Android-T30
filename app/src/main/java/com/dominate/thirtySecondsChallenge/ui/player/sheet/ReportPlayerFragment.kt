package com.dominate.thirtySecondsChallenge.ui.player.sheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.buttomsheet.BaseValidationBottomSheetFragment
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveInterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentReportPlayerBinding
import com.dominate.thirtySecondsChallenge.ui.player.PlayerViewModel
import com.dominate.thirtySecondsChallenge.ui.player.adapter.ReportAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.BadgesAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.model.BadgesModel
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_player_profile.rv_Badges
import kotlinx.android.synthetic.main.fragment_player_profile.rv_Gifts
import kotlinx.android.synthetic.main.fragment_report_player.btn_Report
import kotlinx.android.synthetic.main.fragment_report_player.rv_Report
import javax.inject.Inject

@AndroidEntryPoint
class ReportPlayerFragment() : BaseValidationBottomSheetFragment<FragmentReportPlayerBinding>(),
    OnItemClickListener<ReportReasonResponseModel> {

    override val layoutId: Int = R.layout.fragment_report_player

    private val viewModel by activityViewModels<PlayerViewModel>()

    @Inject
    lateinit var adapter: ReportAdapter

    var reportId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()
    }

    private fun setUpData() {
        binding?.viewModel = viewModel


        if (viewModel.reportListLiveData.value.isNullOrEmpty()) {

        } else {
            viewModel.reportListLiveData.observe(
                viewLifecycleOwner
            ) { items ->
                adapter.submitItems(items)
            }
        }

    }

    private fun setUpViewsListeners() {

        btn_Report.onClick {
            if (reportId == -1){
                shortToast("يرجى تحديد المشكلة من فضلك!")
            }else{
                viewModel.reportPlayerAdd().observe(viewLifecycleOwner, reportPlayerAdd)
            }
        }

    }

    private fun setRecycleView() {

        requireContext().let {
            adapter = ReportAdapter()
            rv_Report.adapter = adapter
            adapter.onItemClickListener = this
        }

    }

    private val reportPlayerAdd = Observer<Result<List<ReportResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                dismiss()
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

    override fun onItemClicked(view: View?, item: ReportReasonResponseModel, position: Int) {
        reportId = item.id
    }


}