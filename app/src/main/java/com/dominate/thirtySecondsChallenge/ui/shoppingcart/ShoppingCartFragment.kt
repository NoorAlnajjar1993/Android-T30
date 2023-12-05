package com.dominate.thirtySecondsChallenge.ui.shoppingcart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.store.MainCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentShoppingCartBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.action.ActionListener
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter.ItemCartAdapter
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_shopping_cart.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingCartFragment : BaseBindingFragment<FragmentShoppingCartBinding>(),
    BaseBindingRecyclerViewAdapterAny.OnItemClickListener, ActionListener {

    override fun getLayoutId(): Int = R.layout.fragment_shopping_cart

    private val viewModel by activityViewModels<ShoppingCartViewModel>()

    companion object {
        const val DIAMONDS_ID = 6
        const val ITEM_CART_ID = 2222
        const val CURRENCIES_ID = 1
        const val SUBSCRIPTIONS_ID = 4444
        const val ACCESSORIES_ID = 3
        const val OFFERS_ID = 4
        const val PROMOTIONS_ID = 6666
        const val GIFT_ID = 5

    }

    lateinit var itemCartAdapter: ItemCartAdapter

    lateinit var navController : NavController

    private val args: ShoppingCartFragmentArgs by navArgs()

    var animation: LayoutAnimationController? = null

    var actionListener: ActionListener = this@ShoppingCartFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setRecycleView()
        setUpData()
        setupNavigation()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        if (viewModel.mainCategoryListLiveData.value.isNullOrEmpty()) {
            viewModel.isShow.value = false
            viewModel.getMainCategory().observe(viewLifecycleOwner, getMainCategory)
        } else {
            viewModel.mainCategoryListLiveData.observe(
                viewLifecycleOwner
            ) { items ->
                itemCartAdapter.submitItems(items ?: arrayListOf())

                if (args.typeScreen != -1) {
                    items.forEach {
                        it.isSelected = false
                        if (it.id == args.typeScreen) {
                            it.isSelected = true
                        }
                    }
                    startNav(args.typeScreen)
                } else {
                    items.forEach {
                        it.isSelected = false
                    }
                    items[0].isSelected = true
                    startNav(items[0].id)
                }
            }

        }

        viewModel.isSendFriendsLiveData.observe(
            viewLifecycleOwner
        ) { items ->
            if (items == true){
                findNavController().navigate(R.id.action_global_selectFriendsListFragment)
                viewModel._isSendFriendsLiveData.value = false
            }
        }

    }

    private fun setUpViewsListeners() {

    }

    private fun setupNavigation() {
        requireActivity().findNavController(R.id.cartNavContainer)
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.currenciesCartFragment -> {
                    }

                    R.id.diamondsCartFragment -> {
                    }

                    R.id.accessoriesCartFragment -> {
                    }

                    R.id.giftCartFragment -> {
                    }

                    R.id.offersCartFragment -> {
                    }

                    R.id.subscriptionsCartFragment -> {
                    }

                    else -> {
                        Log.i("errors", destination.label.toString())
                    }
                }
            }
    }


    private fun setRecycleView() {
        animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fade_in)

        context?.let {
            itemCartAdapter = ItemCartAdapter(it, ITEM_CART_ID)
            rv_ItemCart.adapter = itemCartAdapter
            itemCartAdapter.itemClickListener = this
        }

    }

    private fun setStartDestination(fragment: Int) {

        navController = requireActivity().findNavController(R.id.cartNavContainer)
        val navGraph = navController.navInflater.inflate(R.navigation.cart_nav_graph)
        navGraph.startDestination = fragment
        navController.graph = navGraph

    }

    private val getMainCategory = Observer<Result<List<MainCategoryResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                it.data?.forEach {
                    it.isSelected = false
                }
                it.data?.get(0)?.isSelected = true
                itemCartAdapter.submitItems(it.data ?: arrayListOf())
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
                if (viewModel.mainCategoryListLiveData.value.isNullOrEmpty()) {
                    CustomProgressBar.show(requireContext())
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    private fun startNav(id: Int) {
        when (id) {

            DIAMONDS_ID -> {
                setStartDestination(R.id.diamondsCartFragment)
            }

            CURRENCIES_ID -> {
                setStartDestination(R.id.currenciesCartFragment)
            }

            SUBSCRIPTIONS_ID -> {
                setStartDestination(R.id.subscriptionsCartFragment)
            }

            ACCESSORIES_ID -> {
                setStartDestination(R.id.accessoriesCartFragment)
            }

            OFFERS_ID -> {
                setStartDestination(R.id.offersCartFragment)
            }

            PROMOTIONS_ID -> {

            }

            GIFT_ID -> {
                setStartDestination(R.id.giftCartFragment)
            }

            9900 -> {
                try {
                    requireActivity().findNavController(R.id.cartNavContainer).navigate(R.id.action_from_included_graph)
                }catch (e:Exception){
                    Log.i("error", e.message.toString())
                }
            }
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any, flag: Int) {

        when (flag) {
            ITEM_CART_ID -> {
                val items = item as MainCategoryResponseModel
                startNav(items.id)
            }
        }

    }

    override fun onClick(status: Boolean) {
        lifecycleScope.launch {
            startNav(9900)
        }
    }

}