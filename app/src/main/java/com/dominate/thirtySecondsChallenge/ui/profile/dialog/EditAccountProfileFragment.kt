package com.dominate.thirtySecondsChallenge.ui.profile.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentEditAccountProfileBinding
import com.dominate.thirtySecondsChallenge.ui.player.model.GiftListModel
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileViewModel
import com.dominate.thirtySecondsChallenge.ui.setting.FragmentSettingDialogDirections
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogOutline
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_account_profile.*
import javax.inject.Inject


@AndroidEntryPoint
class EditAccountProfileFragment() : BaseValidationDialogFragment<FragmentEditAccountProfileBinding>(),
    OnItemClickListener<GiftListModel> {

    override fun getLayoutId(): Int = R.layout.fragment_edit_account_profile

    private val viewModel by activityViewModels<ProfileViewModel>()

    @Inject
    lateinit var userPref: UserPref

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
            dismiss()
        }

        tv_DeleteAccount.onClick {
            dismiss()
            val deleteAccountFragment = DeleteAccountFragment()
            deleteAccountFragment.show(parentFragmentManager, "delete Account fragment")
        }

        tv_logout.onClick {
            requireContext().showDialogOutline(
                "هل انت متأكد من انك تريد تسجيل الخروج؟",
                onPositiveButtonClick = {
                    it.dismiss()
                    dismiss()
                    val myApplication = requireActivity().application as MyApplication
                    myApplication.connection.disconnect()
                    userPref.logout()
                    findNavController().navigate(FragmentSettingDialogDirections.actionGlobalMainOperationFragment())

                },
                btnText = "تسجيل الخروج"
            )
        }

        btn_save.onClick {

            viewModel.firstName.value = et_FirstName.text.toString()
            viewModel.lastName.value = et_LastName.text.toString()
            viewModel.email.value = et_Email.text.toString()
            viewModel.phone.value = et_Phone.text.toString()

            viewModel.editProfileUserInfo().observe(viewLifecycleOwner, editProfileUserInfo)
        }

    }

    private val editProfileUserInfo =
        Observer<Result<ProfileResponseModel>> {
            when (it) {
                is Result.Success -> {
                    CustomProgressBar.hide(requireContext())
                    dismiss()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onValidationSucceeded() {

    }

    override fun onItemClicked(view: View?, item: GiftListModel, position: Int) {

    }

}