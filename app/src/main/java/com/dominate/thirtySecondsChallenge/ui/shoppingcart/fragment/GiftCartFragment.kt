package com.dominate.thirtySecondsChallenge.ui.shoppingcart.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.store.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentGiftCartBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.ShoppingCartViewModel
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter.GiftCartAdapter
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.dialog.GiftCartDialog
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gift_cart.*

@AndroidEntryPoint
class GiftCartFragment : BaseBindingFragment<FragmentGiftCartBinding>(),
    OnItemClickListener<GiftsResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_gift_cart

    private val viewModel by activityViewModels<ShoppingCartViewModel>()

    lateinit var adapter: GiftCartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getItemsGifts().observe(viewLifecycleOwner, getItemsGifts)
    }

    private fun setUpViewsListeners() {

    }

    private fun setRecycleView() {

        context?.let {
            adapter = GiftCartAdapter()
            rv_gift_cart.adapter = adapter
            adapter.onItemClickListener = this
        }
    }

    private val getItemsGifts = Observer<Result<List<GiftsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                if (!it.data.isNullOrEmpty()) {
                    adapter.submitItems(it.data)
                }else{
                    adapter.clear()
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

    override fun onItemClicked(view: View?, item: GiftsResponseModel, position: Int) {
        viewModel.typeBtn.value = false
        viewModel.titleBtn.value = "ارسلها كهدية"
        viewModel.imageDialog.value = item.imageUrl
        viewModel.titleDialog.value = item.title
        viewModel.descriptionDialog.value =  item.description
        viewModel.priceDialog.value =  item. priceAfterDiscount
        viewModel.priceIsoDialog.value = item.currencyId
        val giftCartDialog = GiftCartDialog()
        giftCartDialog.show(parentFragmentManager, "gift Cart Dialog")
    }


}