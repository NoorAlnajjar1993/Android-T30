package com.dominate.thirtySecondsChallenge.ui.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.activity.BaseBindingActivity
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.common.CommonEnums
import com.dominate.thirtySecondsChallenge.databinding.FragmentOnBoardingStepOneBinding
import com.dominate.thirtySecondsChallenge.ui.splash.SplashViewModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_on_boarding_step_one.*

@AndroidEntryPoint
class OnBoardingStepOneFragment : BaseBindingFragment<FragmentOnBoardingStepOneBinding>() {

    companion object{
        const val LANGUAGE_EN = 1
        const val LANGUAGE_AR = 2
    }

    override fun getLayoutId(): Int = R.layout.fragment_on_boarding_step_one

    private val viewModel by activityViewModels<SplashViewModel>()

    var selectedLanguage: CommonEnums.Languages? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        selectedLanguage = CommonEnums.Languages.getLanguageByValue(
            SharedPreferencesUtil.getInstance(requireContext()).getStringPreferences(
                PrefConstants.APP_LANGUAGE_VALUE,
                CommonEnums.Languages.English.value
            )
        )

        when (selectedLanguage?.value ?: "en") {
            "en" -> {
                viewModel.languageName.value = LANGUAGE_EN
            }

            "ar" -> {
                viewModel.languageName.value = LANGUAGE_AR
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViewsListeners() {

        tv_English.onClick {
            when (selectedLanguage?.value ?: "en") {
                "en" -> {

                }

                "ar" -> {
                    updateLanguage(
                        CommonEnums.Languages.English,
                        CommonEnums.Languages.English.value
                    )
                    viewModel.languageName.value = LANGUAGE_EN
                }
            }
        }

        tv_Arabic.onClick {
            when (selectedLanguage?.value ?: "en") {
                "en" -> {
                    updateLanguage(
                        CommonEnums.Languages.Arabic,
                        CommonEnums.Languages.Arabic.value
                    )
                    viewModel.languageName.value = LANGUAGE_AR
                }

                "ar" -> {

                }
            }
        }

        btn_Next.onClick {
            findNavController().navigate(OnBoardingStepOneFragmentDirections.actionOnBoardingStepOneFragmentToOnBoardingStepTwoFragment())
        }

    }

    private fun updateLanguage(selectedLanguage: CommonEnums.Languages, flag: String) {

        SharedPreferencesUtil.getInstance(requireContext()).setStringPreferences(
            PrefConstants.APP_LANGUAGE_VALUE, flag
        )
        requireActivity().let {
            (it as BaseBindingActivity<*>).setLanguage(selectedLanguage.value)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}