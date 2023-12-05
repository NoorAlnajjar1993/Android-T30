package com.dominate.thirtySecondsChallenge.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.ui.main.MainActivity
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveInterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentOnBoardingStepThreeBinding
import com.dominate.thirtySecondsChallenge.ui.auth.AuthViewModel
import com.dominate.thirtySecondsChallenge.ui.onboarding.adapter.ChooseInterestsAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileViewModel
import com.dominate.thirtySecondsChallenge.ui.splash.SplashViewModel
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_on_boarding_step_three.*
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingStepThreeFragment : BaseBindingFragment<FragmentOnBoardingStepThreeBinding>(),
    OnItemClickListener<ActiveInterestsResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_on_boarding_step_three

    companion object {
        const val ISBORDING = "onBoarding"
        const val ISPROFILE = "onProfile"
    }

    private val args: OnBoardingStepThreeFragmentArgs by navArgs()

    private val viewModel by activityViewModels<SplashViewModel>()
    private val profileViewModel by activityViewModels<ProfileViewModel>()
    private val authViewModel by activityViewModels<AuthViewModel>()

    @Inject
    lateinit var adapter: ChooseInterestsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycle()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        when (args.screenName) {
            ISBORDING -> {
                viewModel.btntitle.value =  getString(R.string.start) //"ابدأ"
            }

            ISPROFILE -> {
                viewModel.btntitle.value = getString(R.string.save)//"حفظ"
            }

        }

        viewModel.getActiveInterests().observe(viewLifecycleOwner , getActiveInterests)

    }

    private fun setUpViewsListeners() {

        ivBack.onClick {
            findNavController().popBackStack()
        }

        btn_Next.onClick {

            when (args.screenName) {
                ISBORDING -> {
                    SharedPreferencesUtil.getInstance(requireContext()).setBooleanPreferences(
                        PrefConstants.IS_ONBOARDING, false
                    )
                    MainActivity().setStartDestination(requireActivity(),R.id.mainOperationFragment)
                }

                ISPROFILE -> {
                    authViewModel.listOfInterested.clear()
                    adapter.items.forEach {
                        if (it.isCheck){
                            authViewModel.listOfInterested.addAll(listOf(it.id))
                        }
                    }
                    profileViewModel.editUserIntereste(authViewModel.listOfInterested).observe(viewLifecycleOwner , editProfile)
                }
            }


        }

    }

    private fun setRecycle() {

        context?.let {
            binding?.rvChooseInterests?.layoutManager = LinearLayoutManager(it)
            binding?.rvChooseInterests?.adapter = adapter
            adapter.onItemClickListener = this
        }


    }

    private val getActiveInterests = Observer<Result<List<ActiveInterestsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                it.data?.forEach {
                    it.isCheck = it.isVisible
                }
                adapter.submitItems(it.data)
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

    private val editProfile = Observer<Result<List<ActiveInterestsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                requireView().showSnackbar(it.message.toString(),R.drawable.snackbar)
                findNavController().popBackStack()
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

    override fun onItemClicked(view: View?, item: ActiveInterestsResponseModel, position: Int) {

//        val checkItem = authViewModel.listOfInterested.find {
//            it == item.id
//        }
//        if (item.isCheck) {
//            if (checkItem != item.id) {
//                authViewModel.listOfInterested.addAll(listOf(item.id))
//            } else {
//                authViewModel.listOfInterested.removeAt(position)
//            }
//        }else{
//            if (checkItem == item.id) {
//                authViewModel.listOfInterested.removeAt(position)
//            }
//        }


    }

}