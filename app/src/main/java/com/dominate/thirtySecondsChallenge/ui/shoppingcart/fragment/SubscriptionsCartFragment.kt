package com.dominate.thirtySecondsChallenge.ui.shoppingcart.fragment

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentSubscriptionsCartBinding
import com.dominate.thirtySecondsChallenge.ui.profile.dialog.DeleteAccountFragment
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter.SubscriptionsAdapter
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.dialog.BuyNowFragment
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.model.SubscriptionsModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_subscriptions_cart.*
import javax.inject.Inject

@AndroidEntryPoint
class SubscriptionsCartFragment : BaseBindingFragment<FragmentSubscriptionsCartBinding>(),
    OnItemClickListener<SubscriptionsModel> {

    override fun getLayoutId(): Int = R.layout.fragment_subscriptions_cart

    @Inject
    lateinit var adapter: SubscriptionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }

    private fun setUpData() {

    }

    private fun setUpViewsListeners() {

    }

    private fun setRecycleView() {

        context?.let {
            adapter = SubscriptionsAdapter()
            rvSubscriptions.adapter = adapter
            adapter.onItemClickListener = this
        }

        adapter.submitItems(
            arrayListOf(
                SubscriptionsModel(
                    R.drawable.ic_jawaher,
                    "الاشتراك الذهبي",
                    "5 يوم",
                    "10.000KWD",
                ),
                SubscriptionsModel(
                    R.drawable.ic_jawaher,
                    "الاشتراك الفضي",
                    "5 يوم",
                    "10.000KWD",
                ),
                SubscriptionsModel(
                    R.drawable.ic_jawaher,
                    "الاشتراك البرونزي",
                    "5 يوم",
                    "10.000KWD",
                ),
                SubscriptionsModel(
                    R.drawable.ic_jawaher,
                    "الاشتراك الأوفر",
                    "5 يوم",
                    "10.000KWD",
                ),
            )
        )

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onItemClicked(view: View?, item: SubscriptionsModel, position: Int) {

        val buyNowFragment = BuyNowFragment()
        buyNowFragment.show(parentFragmentManager, "buy Now")

    }


}