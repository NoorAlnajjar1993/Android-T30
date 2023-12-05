package com.dominate.thirtySecondsChallenge.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.databinding.FragmentSettingDialogBinding
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.ui.sound.MusicPlayer
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogOutline
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setting_dialog.*

@AndroidEntryPoint
class FragmentSettingDialog : BaseValidationDialogFragment<FragmentSettingDialogBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_setting_dialog

    private val viewModel by activityViewModels<GameLobbyViewModel>()

    var myApplication: MyApplication? = null

    lateinit var audioPlayer: AudioPlayer
    lateinit var musicPlayer: MusicPlayer

    var isFirstTime = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        musicPlayer = MusicPlayer.getInstance(requireContext(), R.raw.music_running_t30)

        startConnection()
        checkSoundAndMusic()

    }

    private fun startConnection(){
        myApplication = requireActivity().application as MyApplication
    }

    private fun checkSoundAndMusic(){
        viewModel.isSound.value =
            SharedPreferencesUtil.getInstance(requireContext()).getBooleanPreferences(
                PrefConstants.APP_IS_SOUND,
                true
            )

        viewModel.isMusic.value =
            SharedPreferencesUtil.getInstance(requireContext()).getBooleanPreferences(
                PrefConstants.APP_IS_MUSIC,
                true
            )
    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        btn_save.onClick {

            SharedPreferencesUtil.getInstance(requireContext()).setBooleanPreferences(
                PrefConstants.APP_IS_SOUND, viewModel.isSound.value?:false
            )

            when (viewModel.isSound.value) {
                true -> {
                }

                false -> {
                    stopSound()
                }
            }

            SharedPreferencesUtil.getInstance(requireContext()).setBooleanPreferences(
                PrefConstants.APP_IS_MUSIC, viewModel.isMusic.value?:false)

            when (viewModel.isMusic.value) {
                true -> {
                        musicPlayer.startPlaybackLoop()
                }

                false -> {
                    stopMusic()
                }

            }

            dismiss()
        }

        tv_ExitGame.onClick {
            AudioPlayer(requireContext(), R.raw.exit_the_game_t30).startPlayback()
            requireContext().showDialogOutline(
                "هل انت متأكد من انك تريد الخروج؟",
                onPositiveButtonClick = {
                    it.dismiss()
                    dismiss()
                    stopMusic()
                    myApplication?.connection?.invoke("SendMessage", viewModel.exitGame())
                   findNavController().navigate(FragmentSettingDialogDirections.actionGlobalHomeFragment())
                },
                btnText = "خروج"
            )
        }

        Swh_Sound.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.isSound.value = isChecked
//            when (isChecked) {
//                true -> {
//                }
//
//                false -> {
//                    stopSound()
//                }
//            }
        }

        Swh_Music.setOnCheckedChangeListener { buttonView, isChecked ->
//            viewModel.isMusic.value = isChecked
//            SharedPreferencesUtil.getInstance(requireContext()).setBooleanPreferences(
//                PrefConstants.APP_IS_MUSIC, isChecked
//            )
            viewModel.isMusic.value = isChecked
//            when (isChecked) {
//                true -> {
//                    if (!isFirstTime){
//                        musicPlayer.startPlaybackLoop()
//                    }else{
//                        isFirstTime = false
//                    }
//                }
//
//                false -> {
//                    stopMusic()
//                    isFirstTime = false
//                }
//
//            }

        }
    }

    private fun stopSound() {
        audioPlayer = AudioPlayer.getInstance(requireContext(), R.raw.sound_shift)
        audioPlayer.stopPlayback()
        audioPlayer.stopPlaybackWithLoop()
    }

    private fun stopMusic() {
        musicPlayer.stopPlayback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onValidationSucceeded() {

    }

}