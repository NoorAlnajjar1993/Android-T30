package com.dominate.thirtySecondsChallenge.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentOnBoardingStepTwoBinding
import com.dominate.thirtySecondsChallenge.ui.auth.AuthViewModel
import com.dominate.thirtySecondsChallenge.ui.onboarding.OnBoardingStepThreeFragment.Companion.ISBORDING
import com.dominate.thirtySecondsChallenge.ui.splash.SplashViewModel
import com.dominate.thirtySecondsChallenge.utils.anim.slideUp
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_on_boarding_step_two.*
import kotlinx.android.synthetic.main.fragment_splash.ll_language

@AndroidEntryPoint
class OnBoardingStepTwoFragment : BaseBindingFragment<FragmentOnBoardingStepTwoBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_on_boarding_step_two

    private val viewModel by activityViewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)


        setUpData()
        setUpViewsListeners()

    }


    private fun setUpData() {

//        requireContext().slideUp(btn_Next)

    }

    private fun setUpViewsListeners() {

        ivBack.onClick {
            findNavController().popBackStack()
        }

        tvSkip.onClick {
            this.findNavController().navigate(OnBoardingStepTwoFragmentDirections.actionOnBoardingStepTwoFragmentToOnBoardingStepThreeFragment(ISBORDING))
        }

        btn_Next.onClick {
            this.findNavController().navigate(OnBoardingStepTwoFragmentDirections.actionOnBoardingStepTwoFragmentToOnBoardingStepThreeFragment(ISBORDING))
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}