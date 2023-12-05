package com.dominate.thirtySecondsChallenge.ui.endgame.wingame

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentWinGameBinding
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.ui.sound.MusicPlayer
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_win_game.*

@AndroidEntryPoint
class WinGameFragment() : BaseValidationDialogFragment<FragmentWinGameBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_win_game

    private val viewModel by activityViewModels<GameLobbyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        isCancelable = false
        AudioPlayer(requireContext(), R.raw.winning_game_t30).startPlayback()

    }

    private fun setUpViewsListeners() {

        btn_CollectRewards.onClick {
            requireActivity().onBackPressed()
            dismiss()
        }

        tv_player.onClick {
            val playerProfileFragment =
                PlayerProfileFragment.newInstance(viewModel.playerId.value ?: 0)
            playerProfileFragment.show(parentFragmentManager, "player Profile Fragment")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onValidationSucceeded() {

    }


}