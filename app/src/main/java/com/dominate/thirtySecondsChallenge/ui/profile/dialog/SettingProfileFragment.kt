package com.dominate.thirtySecondsChallenge.ui.profile.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentSettingProfileBinding
import com.dominate.thirtySecondsChallenge.ui.player.model.GiftListModel
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileViewModel
import com.dominate.thirtySecondsChallenge.ui.sound.MusicPlayer
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.openUrl
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setting_profile.*


@AndroidEntryPoint
class SettingProfileFragment() : BaseValidationDialogFragment<FragmentSettingProfileBinding>(),
    OnItemClickListener<GiftListModel> {

    override fun getLayoutId(): Int = R.layout.fragment_setting_profile

    private val viewModel by activityViewModels<ProfileViewModel>()

    lateinit var musicPlayer: MusicPlayer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getSocialLinks()

        musicPlayer = MusicPlayer.getInstance(requireContext(), R.raw.music_running_t30)

        viewModel.isSound.value = SharedPreferencesUtil.getInstance(requireContext()).getBooleanPreferences(
            PrefConstants.APP_IS_SOUND,
            true
        )

        viewModel.isMusic.value = SharedPreferencesUtil.getInstance(requireContext()).getBooleanPreferences(
            PrefConstants.APP_IS_MUSIC,
            true
        )

    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        tv_support.onClick {
            dismiss()
            findNavController().navigate(R.id.supportFragment)
        }

        tv_DeleteAccount.onClick {
            dismiss()
            val deleteAccountFragment = DeleteAccountFragment()
            deleteAccountFragment.show(parentFragmentManager, "delete Account")
        }

        iv_youtube.onClick {
            if (!viewModel.youtubeUrl.value.isNullOrEmpty() && viewModel.youtubeUrl.value.toString()
                    .startsWith("http")
            ) {
                requireContext().openUrl(
                    viewModel.youtubeUrl.value.toString()
                )
            }
        }

        iv_facebook.onClick {
            if (!viewModel.facebookUrl.value.isNullOrEmpty() && viewModel.facebookUrl.value.toString()
                    .startsWith("http")
            ) {
                requireContext().openUrl(
                    viewModel.facebookUrl.value.toString()
                )
            }
        }

        iv_instagram.onClick {
            if (!viewModel.instagramUrl.value.isNullOrEmpty() && viewModel.instagramUrl.value.toString()
                    .startsWith("http")
            ) {
                requireContext().openUrl(
                    viewModel.instagramUrl.value.toString()
                )
            }
        }

        Swh_Sound.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.isSound.value = isChecked

        }

        Swh_Music.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.isMusic.value = isChecked

        }

        btn_save.onClick {

            SharedPreferencesUtil.getInstance(requireContext()).setBooleanPreferences(
                PrefConstants.APP_IS_SOUND, viewModel.isSound.value?:false
            )

            SharedPreferencesUtil.getInstance(requireContext()).setBooleanPreferences(
                PrefConstants.APP_IS_MUSIC, viewModel.isMusic.value?:false
            )
            when (viewModel.isMusic.value) {
                true -> {
                }

                false -> {
                    musicPlayer.stopPlayback()
                }
            }

            dismiss()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onValidationSucceeded() {

    }

    override fun onItemClicked(view: View?, item: GiftListModel, position: Int) {

    }

}