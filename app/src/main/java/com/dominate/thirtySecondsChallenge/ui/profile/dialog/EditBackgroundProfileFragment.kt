package com.dominate.thirtySecondsChallenge.ui.profile.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentEditBackgroundProfileBinding
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileViewModel
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.EditBackgroundProfileAdapter
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.longToast
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_background_profile.*
import javax.inject.Inject


@AndroidEntryPoint
class EditBackgroundProfileFragment() :
    BaseValidationDialogFragment<FragmentEditBackgroundProfileBinding>(),
    OnItemClickListener<BackgroundImagesProfileResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_edit_background_profile

    private val viewModel by activityViewModels<ProfileViewModel>()

    @Inject
    lateinit var adapter: EditBackgroundProfileAdapter

    var backgroundId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

//        viewModel.getAllBackgroundImages().observe(viewLifecycleOwner, getAllBackgroundImages)
    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        btn_Save.onClick {
            if (backgroundId == -1) {
                longToast("اختر غلاف!")
            } else {
                viewModel.editProfileBackground(backgroundId).observe(viewLifecycleOwner, editProfileBackground)
            }
        }

    }

    private fun setRecycleView() {
        requireContext().let {
            adapter = EditBackgroundProfileAdapter()
            rv_Edit_Background.adapter = adapter
            adapter.onItemClickListener = this
        }
        viewModel.getAllBackgroundImagesLiveData.observe(
            viewLifecycleOwner
        ) { items ->
            adapter.submitItems(items)
        }
    }

    private val getAllBackgroundImages =
        Observer<Result<List<BackgroundImagesProfileResponseModel>>> {
            when (it) {
                is Result.Success -> {
                    CustomProgressBar.hide(requireContext())
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

    private val editProfileBackground = Observer<Result<ProfileResponseModel>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                adapter.items.forEach {
                    it.isSelected = false
                }
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

    override fun onItemClicked(
        view: View?,
        item: BackgroundImagesProfileResponseModel,
        position: Int
    ) {
        backgroundId = item.id
//        viewModel.selectedProfileBackgroundId.value = item.id
    }

}