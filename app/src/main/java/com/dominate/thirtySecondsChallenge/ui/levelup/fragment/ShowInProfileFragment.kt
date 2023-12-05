package com.dominate.thirtySecondsChallenge.ui.levelup.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.buttomsheet.BaseValidationBottomSheetFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentShowInProfileBinding
import com.dominate.thirtySecondsChallenge.ui.auth.AuthFragment
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.sheet.MoreFriendsListFragment
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_show_in_profile.*

@AndroidEntryPoint
class ShowInProfileFragment() : BaseValidationBottomSheetFragment<FragmentShowInProfileBinding>() {

    override val layoutId: Int = R.layout.fragment_show_in_profile

    companion object {
        const val messages = "message"
        const val imageUrls = "imageUrl"

        fun newInstance(message: String?,imageUrl: String?): ShowInProfileFragment {
            val fragment = ShowInProfileFragment()
            val args = Bundle()
            args.putString(messages, message)
            args.putString(imageUrls, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var audioPlayer: AudioPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setOnClick()

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it1 ->
                val behavior = BottomSheetBehavior.from(it1)

                // Set custom height to match the screen height
                val layoutParams = it1.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                it1.layoutParams = layoutParams

                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false
            }
        }

        return dialog
    }


    private fun setUpData() {
        binding?.message = arguments?.getString(messages,"حصلت على مكافأة  جديدة")
        binding?.imageUrl = arguments?.getString(imageUrls)

        audioPlayer = AudioPlayer(requireContext(), R.raw.winning_badge_t30)
        audioPlayer.startPlaybackLoop()
    }

    private fun setOnClick() {

        iv_close.onClick {
            audioPlayer.stopPlayback()
            dismiss()
        }

        btn_SendAsGift.onClick {
            audioPlayer.stopPlayback()
            dismiss()
        }

        btn_showInProfile.onClick {
            audioPlayer.stopPlayback()
            dismiss()
            findNavController().navigate(
                R.id.action_global_mainProfileFragment
            )
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onValidationSucceeded() {

    }

}