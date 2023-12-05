package com.dominate.thirtySecondsChallenge.ui.shoppingcart.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.store.DiamondsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentDiamondsCartBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.ShoppingCartViewModel
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter.DiamondsCartAdapter
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.dialog.GiftCartDialog
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_diamonds_cart.*
import javax.inject.Inject

@AndroidEntryPoint
class DiamondsCartFragment : BaseBindingFragment<FragmentDiamondsCartBinding>(),
    OnItemClickListener<DiamondsResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_diamonds_cart

    private val viewModel by activityViewModels<ShoppingCartViewModel>()

    @Inject
    lateinit var adapter: DiamondsCartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }


    fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getItemsDiamonds().observe(viewLifecycleOwner, getItemsDiamonds)

    }

    private fun setUpViewsListeners() {

    }

    private fun setRecycleView() {

        context?.let {
            adapter = DiamondsCartAdapter()
            rvDiamondsCart.adapter = adapter
            adapter.onItemClickListener = this
        }
    }

    private val getItemsDiamonds = Observer<Result<List<DiamondsResponseModel>>> {
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

    override fun onItemClicked(view: View?, item: DiamondsResponseModel, position: Int) {
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