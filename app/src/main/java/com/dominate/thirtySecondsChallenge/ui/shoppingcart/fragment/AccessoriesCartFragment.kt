package com.dominate.thirtySecondsChallenge.ui.shoppingcart.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.store.AccessoriesResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentAccessoriesCartBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.ShoppingCartViewModel
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter.AccessoriesAdapter
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.dialog.GiftCartDialog
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogInfoDefault
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_accessories_cart.*

@AndroidEntryPoint
class AccessoriesCartFragment : BaseBindingFragment<FragmentAccessoriesCartBinding>(),
    BaseBindingRecyclerViewAdapterAny.OnItemClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_accessories_cart

    private val viewModel by activityViewModels<ShoppingCartViewModel>()

    lateinit var adapter: AccessoriesAdapter

    companion object{
        const val STICKERS_ID = 11111
        const val BACKGROUNDS_ID = 22222
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getStickers().observe(viewLifecycleOwner,getStickers)
    }

    private fun setUpViewsListeners() {

        iv_posters.onClick {
            val layoutManager = GridLayoutManager(context, 3)
            viewModel.isPoster.value = true
            context?.let {
                adapter = AccessoriesAdapter(it, STICKERS_ID)
                rvPosters.layoutManager = layoutManager
                rvPosters.adapter = adapter
                adapter.itemClickListener = this
            }
            adapter.clear()
            viewModel.getStickers().observe(viewLifecycleOwner,getStickers)
        }

        iv_background.onClick {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            viewModel.isPoster.value = false
            context?.let {
                adapter = AccessoriesAdapter(it, BACKGROUNDS_ID)
                rvPosters.layoutManager = layoutManager
                rvPosters.adapter = adapter
                adapter.itemClickListener = this
            }
            adapter.clear()
            viewModel.getBackgrounds().observe(viewLifecycleOwner,getBackgrounds)

        }

        iv_MoreDetailsPoster.onClick {
            requireContext().showDialogInfoDefault(
                "الملصقات",
                "تستطيع اقتناء الملصقات المميزة واستخدامها اثناء اللعب او المحادثات ، يمكنك ايضا الحصول عليها من خلال الهدايا أو ان ترسلها انت لاصدقاءك"
            )
        }

        iv_MoreDetailsBackground.onClick {
            requireContext().showDialogInfoDefault(
                "خلفيات الملف الشخصي",
                "تستطيع اقتناء خلفيات الملف الشخصي المميزة واستخدامها اثناء اللعب او المحادثات ، يمكنك ايضا الحصول عليها من خلال الهدايا أو ان ترسلها انت لاصدقاءك"
            )
        }

    }

    private fun setRecycleView() {

        val layoutManager = GridLayoutManager(context, 3)
        viewModel.isPoster.value = true
        context?.let {
            adapter = AccessoriesAdapter(it, STICKERS_ID)
            rvPosters.layoutManager = layoutManager
            rvPosters.adapter = adapter
            adapter.itemClickListener = this
        }

    }

    private val getStickers = Observer<Result<List<AccessoriesResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                if (!it.data.isNullOrEmpty()) {
//                    val item : ArrayList<AccessoriesResponseModel> = ArrayList()
//                    for ( i in 0..8){
//                        item.addAll(it.data)
//                    }
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

    private val getBackgrounds = Observer<Result<List<AccessoriesResponseModel>>> {
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

    override fun onItemClick(view: View?, position: Int, item: Any, flag: Int) {

        when(flag){

            STICKERS_ID ->{
                item as AccessoriesResponseModel
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

            BACKGROUNDS_ID ->{
                item as AccessoriesResponseModel
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
    }

}