package com.dominate.thirtySecondsChallenge.ui.shoppingcart.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.store.CoinsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentCurrenciesCartBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.ShoppingCartViewModel
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter.CurrenciesAdapter
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.dialog.GiftCartDialog
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_currencies_cart.*
import javax.inject.Inject

@AndroidEntryPoint
class CurrenciesCartFragment : BaseBindingFragment<FragmentCurrenciesCartBinding>(),
    OnItemClickListener<CoinsResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_currencies_cart

    private val viewModel by activityViewModels<ShoppingCartViewModel>()

    @Inject
    lateinit var adapter: CurrenciesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)


        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }


    private fun setUpData() {
        binding?.viewModel = viewModel
        viewModel.getItemsCoins().observe(viewLifecycleOwner, getItemsCoins)
    }

    private fun setUpViewsListeners() {

    }

    private fun setRecycleView() {

        context?.let {
            adapter = CurrenciesAdapter()
            rvCurrencies.adapter = adapter
            adapter.onItemClickListener = this
        }

    }

    private val getItemsCoins = Observer<Result<List<CoinsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                if (!it.data.isNullOrEmpty()) {
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
//                CustomProgressBar.show(requireContext())
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: CoinsResponseModel, position: Int) {
        viewModel.typeBtn.value = true
        viewModel.titleBtn.value = "اشترِ الآن"
        viewModel.imageDialog.value = item.imageUrl
        viewModel.titleDialog.value = item.title
        viewModel.descriptionDialog.value =  item.description
        viewModel.priceDialog.value =  item. priceAfterDiscount
        viewModel.priceIsoDialog.value = item.currencyId
        val giftCartDialog = GiftCartDialog()
        giftCartDialog.show(parentFragmentManager, "gift Cart Dialog")

    }


}