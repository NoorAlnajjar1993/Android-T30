package com.dominate.thirtySecondsChallenge.ui.privacy

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentPrivacyPolicyBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_privacy_policy.*

@AndroidEntryPoint
class PrivacyPolicyFragment : BaseBindingFragment<FragmentPrivacyPolicyBinding>() {


    override fun getLayoutId(): Int = R.layout.fragment_privacy_policy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)


        setUpData()
        setUpViewsListeners()

    }


    private fun setUpData() {

    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}