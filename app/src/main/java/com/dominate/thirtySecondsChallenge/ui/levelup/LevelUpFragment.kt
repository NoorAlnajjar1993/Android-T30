package com.dominate.thirtySecondsChallenge.ui.levelup

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentLevelUpBinding
import com.dominate.thirtySecondsChallenge.ui.levelup.fragment.ShowInProfileFragment
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.extensions.addPlaySound
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_level_up.btn_CollectRewards

@AndroidEntryPoint
class LevelUpFragment() : BaseValidationDialogFragment<FragmentLevelUpBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_level_up

    lateinit var audioPlayer: AudioPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {
        audioPlayer = AudioPlayer(requireContext(), R.raw.raising_level_t30)
        audioPlayer.startPlaybackLoop()
    }

    private fun setUpViewsListeners() {

        btn_CollectRewards.onClick {
            audioPlayer.stopPlayback()
            dismiss()
            val showInProfileFragment = ShowInProfileFragment()
            showInProfileFragment.show(parentFragmentManager, "show In Profile Fragment")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onValidationSucceeded() {

    }

}