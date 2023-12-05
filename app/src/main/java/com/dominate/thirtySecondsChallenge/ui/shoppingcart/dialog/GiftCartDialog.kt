package com.dominate.thirtySecondsChallenge.ui.shoppingcart.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.databinding.GiftCartDialogBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.ShoppingCartViewModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.gift_cart_dialog.*


@AndroidEntryPoint
class GiftCartDialog() : BaseValidationDialogFragment<GiftCartDialogBinding>() {

    override fun getLayoutId(): Int = R.layout.gift_cart_dialog

    private val viewModel by activityViewModels<ShoppingCartViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
    }

    private fun setUpData() {
        binding?.viewModel = viewModel
    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            viewModel.clearDataGiftDialog()
            dismiss()
        }

        btn_SendAsGift.onClick {

            when (viewModel.typeBtn.value) {

                // pay now
                true -> {

                }

                // send as gift
                false -> {
                    viewModel._isSendFriendsLiveData.value = true
//                    ShoppingCartFragment().actionListener.onClick(
//                        status = true
//                    )
                }

            }
            viewModel.clearDataGiftDialog()
            dismiss()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onValidationSucceeded() {

    }


}