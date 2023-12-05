package com.dominate.thirtySecondsChallenge.ui.profile.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.profile.FaqCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GetAllFaqsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentSupportBinding
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileViewModel
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.ItemSupportAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.ItemSupportDataAdapter
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_support.*

@AndroidEntryPoint
class SupportFragment : BaseBindingFragment<FragmentSupportBinding>(),
    BaseBindingRecyclerViewAdapterAny.OnItemClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_support

    companion object {

        const val ITEM_SUPPORT_ID = 1
        const val ITEM_SUPPORT_DATA_ID = 2

    }

    private val viewModel by activityViewModels<ProfileViewModel>()

    lateinit var itemSupportAdapter: ItemSupportAdapter
    lateinit var itemSupportDataAdapter: ItemSupportDataAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel
    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

        viewModel.getFaqCategory().observe(viewLifecycleOwner, getFaqCategory)

    }

    private fun setRecycleView() {

        context?.let {
            itemSupportAdapter = ItemSupportAdapter(it, ITEM_SUPPORT_ID)
            rv_ItemSupport.adapter = itemSupportAdapter
            itemSupportAdapter.itemClickListener = this

        }


        context?.let {
            itemSupportDataAdapter = ItemSupportDataAdapter(it, ITEM_SUPPORT_DATA_ID)
            rv_ItemDataSupport.adapter = itemSupportDataAdapter
            itemSupportDataAdapter.itemClickListener = this
        }
        itemSupportDataAdapter.submitItems(viewModel.allFaqsModel.value ?: arrayListOf())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val getFaqCategory = Observer<Result<List<FaqCategoryResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                if (!it.data.isNullOrEmpty()) {
                    val items = it.data
                    items[0].isSelected = true
                    itemSupportAdapter.submitItems(
                        items
                    )
                    viewModel.getAllFaqs(it.data[0].id).observe(viewLifecycleOwner, getAllFaqs)
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

    private val getAllFaqs = Observer<Result<List<GetAllFaqsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                if (!it.data.isNullOrEmpty()) {
                    itemSupportDataAdapter.submitItems(
                        it.data
                    )
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

    override fun onItemClick(view: View?, position: Int, item: Any, flag: Int) {

        when (flag) {
            ITEM_SUPPORT_ID -> {
                val items = item as FaqCategoryResponseModel
                itemSupportDataAdapter.clear()
                viewModel.getAllFaqs(items.id).observe(viewLifecycleOwner, getAllFaqs)
            }
        }
    }

}