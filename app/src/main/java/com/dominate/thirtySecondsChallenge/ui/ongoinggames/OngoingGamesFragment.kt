package com.dominate.thirtySecondsChallenge.ui.ongoinggames

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentOngoingGamesBinding
import com.dominate.thirtySecondsChallenge.ui.ongoinggames.adapter.OnGoingGameAdapter
import com.dominate.thirtySecondsChallenge.ui.ongoinggames.model.OngoingGameModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ongoing_games.iv_back
import kotlinx.android.synthetic.main.fragment_ongoing_games.rvOngoingGame
import javax.inject.Inject

@AndroidEntryPoint
class OngoingGamesFragment : BaseBindingFragment<FragmentOngoingGamesBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_ongoing_games

    private val viewModel by activityViewModels<OngoingGamesViewModel>()

    @Inject
    lateinit var adapter: OnGoingGameAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)


        setUpData()
        setUpViewsListeners()
        setRecycleView()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel
    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

    }

    private fun setRecycleView(){

        requireContext().let {
            adapter = OnGoingGameAdapter()
            rvOngoingGame.adapter = adapter
            adapter.submitItems(
                listOf(
                    OngoingGameModel(
                        R.drawable.ic_test_person_4,
                        R.drawable.ic_test_person_3,
                        true
                    ),
                    OngoingGameModel(
                        R.drawable.ic_test_person_4,
                        R.drawable.ic_test_person_3,
                        true
                    ),
                    OngoingGameModel(
                        R.drawable.ic_test_person_4,
                        R.drawable.ic_test_person_3,
                    ),
                    OngoingGameModel(
                        R.drawable.ic_test_person_4,
                        R.drawable.ic_test_person_3,
                        true
                    ),
                )
            )
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}