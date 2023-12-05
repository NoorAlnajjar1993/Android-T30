package com.dominate.thirtySecondsChallenge.ui.auctionround.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentCountAnswerBinding
import com.dominate.thirtySecondsChallenge.ui.auctionround.AuctionRoundViewModel
import com.dominate.thirtySecondsChallenge.ui.auctionround.adapter.CountAnswerAdapter
import com.dominate.thirtySecondsChallenge.ui.auctionround.model.CountAnswerModel
import com.dominate.thirtySecondsChallenge.utils.extensions.longToast
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_count_answer.*
import javax.inject.Inject

@AndroidEntryPoint
class CountAnswerFragment() : BaseValidationDialogFragment<FragmentCountAnswerBinding>(),
    OnItemClickListener<CountAnswerModel> {

    override fun getLayoutId(): Int = R.layout.fragment_count_answer

    @Inject
    lateinit var adapter: CountAnswerAdapter

    private val viewModel by activityViewModels<AuctionRoundViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycle()

    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setOnShowListener {
//
//            val bottomSheetDialog = it as BottomSheetDialog
//            val parentLayout =
//                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            parentLayout?.let { it1 ->
//                val behaviour = BottomSheetBehavior.from(it1)
//                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
//                behaviour.isDraggable = true
//            }
//        }
//
//        return dialog
//    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        picker_count.minValue = 1
        picker_count.maxValue = 30

    }

    private fun setUpViewsListeners() {

        btn_GetValueCount.onClick {
            viewModel.countAnswer.value = picker_count.value.toString()
            dismiss()
        }

    }

    private fun setRecycle() {

        var itemArray = ArrayList<CountAnswerModel>()

        context?.let {
            rvChooseCountAnswer.adapter = adapter
            adapter.onItemClickListener = this

            for (item in 1..30) {
                itemArray.add(CountAnswerModel(item, false))
            }
            adapter.submitItems(itemArray)
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onValidationSucceeded() {

    }

    override fun onItemClicked(view: View?, item: CountAnswerModel, position: Int) {

    }


}