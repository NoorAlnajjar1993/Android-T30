package com.dominate.thirtySecondsChallenge.ui.createtournament

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentCreateTournamentBinding
import com.dominate.thirtySecondsChallenge.databinding.FragmentOnBoardingStepOneBinding
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTournamentFragment : BaseBindingFragment<FragmentCreateTournamentBinding>() {


    override fun getLayoutId(): Int = R.layout.fragment_create_tournament

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