package com.dominate.thirtySecondsChallenge.ui.profile.dialog

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.BindViews
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentDeleteAccountBinding
import com.dominate.thirtySecondsChallenge.ui.player.model.GiftListModel
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileViewModel
import com.dominate.thirtySecondsChallenge.utils.extensions.longToast
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Order
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_delete_account.*


@AndroidEntryPoint
class DeleteAccountFragment() : BaseValidationDialogFragment<FragmentDeleteAccountBinding>(),
    OnItemClickListener<GiftListModel> {

    override fun getLayoutId(): Int = R.layout.fragment_delete_account

    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    @NotEmpty
    private val et_ConfirmDelete: EditText? = null

    private fun setUpData() {
        binding?.viewModel = viewModel
        viewModel.isConfirmDelete.value = false
        viewModel.titleConfirmDelete.value = "هل تريد تعطيل حسابك؟"
    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        tv_DeleteAccount.onClick {
            if (viewModel.isConfirmDelete.value == false){
                viewModel.titleConfirmDelete.value = "لتأكيد عملية تعطيل الحساب يرجي كتابة كلمة “تعطيل”"
                viewModel.isConfirmDelete.value = true
            }else{
                if (viewModel.confirmDeleteAccount.value.isNullOrEmpty() ){
                    requireView().showSnackbar(getString(R.string.msg_confirm_delete_err),R.drawable.snackbar_error)
//                    shortToast(getString(R.string.msg_confirm_delete_err))
                }else{
                    longToast("confirm")
                }
            }
        }

        tv_privacyPolicy.onClick {
            dismiss()
            findNavController().navigate(R.id.action_global_privacyPolicyFragment)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onValidationSucceeded() {

    }

    override fun onItemClicked(view: View?, item: GiftListModel, position: Int) {

        shortToast("45120")
//        dismiss()

    }

}