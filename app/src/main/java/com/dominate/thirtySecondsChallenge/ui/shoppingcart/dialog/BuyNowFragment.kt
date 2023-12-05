package com.dominate.thirtySecondsChallenge.ui.shoppingcart.dialog

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentBuyNowBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_buy_now.*


@AndroidEntryPoint
class BuyNowFragment() : BaseValidationDialogFragment<FragmentBuyNowBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_buy_now


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {

    }

    private fun setUpViewsListeners() {

        btn_PuyNow.onClick {
            dismiss()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onValidationSucceeded() {

    }


}