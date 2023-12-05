package com.dominate.thirtySecondsChallenge

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentOnBoardingStepOneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TFragment : BaseBindingFragment<FragmentOnBoardingStepOneBinding>() {


    override fun getLayoutId(): Int = R.layout.fragment_on_boarding_step_one

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)


        setUpData()
        setUpViewsListeners()

    }


    private fun setUpData() {

    }

    private fun setUpViewsListeners() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}