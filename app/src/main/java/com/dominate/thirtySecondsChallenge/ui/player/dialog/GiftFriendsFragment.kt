package com.dominate.thirtySecondsChallenge.ui.player.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewParent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.dialogfragment.BaseValidationDialogFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentGiftFriendsBinding
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.sheet.MoreFriendsListFragment
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.ui.player.PlayerViewModel
import com.dominate.thirtySecondsChallenge.ui.player.adapter.GiftListAdapter
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gift_friends.*
import javax.inject.Inject


@AndroidEntryPoint
class GiftFriendsFragment() : BaseValidationDialogFragment<FragmentGiftFriendsBinding>(),
    OnItemClickListener<GiftsResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_gift_friends

    private val viewModel by activityViewModels<PlayerViewModel>()

    @Inject
    lateinit var adapter: GiftListAdapter

    companion object {
        const val playerId = "PlayerId"

        fun newInstance(argValue: Int): GiftFriendsFragment {
            val fragment = GiftFriendsFragment()
            val args = Bundle()
            args.putInt(playerId, argValue)
            fragment.arguments = args
            return fragment
        }
    }

    var parent: ViewParent? = null
    var myApplication: MyApplication? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setRecycleView()
        setUpViewsListeners()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel
        viewModel.getAllGifts().observe(viewLifecycleOwner, getAllGifts)

        myApplication = requireActivity().application as MyApplication
//        myApplication?.connection?.addListener(this)
    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        btn_Send.onClick {

            if (viewModel.giftSelectedId.value == -1) {
                requireView().showSnackbar("قم بإختيار هدية أولاً", R.drawable.snackbar)
            } else {
                if (myApplication?.isConnected== true){
                    myApplication?.connection?.invoke("SendMessage", viewModel.sendGiftHub(arguments?.getInt(playerId, 0)?:0))
                    viewModel.giftSelectedId.value = -1
                    dismiss()
                }else{
                    shortToast("No connection")
                    dismiss()
                }
//                viewModel.sendGifts(arguments?.getInt(
//                    playerId, 0) ?: 0)
//                    .observe(viewLifecycleOwner, sendGifts)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun setRecycleView() {

        val layoutManager = GridLayoutManager(requireContext(), 3)

        requireContext().let {
            adapter = GiftListAdapter()
            rv_GiftList.layoutManager = layoutManager
            rv_GiftList.adapter = adapter
            adapter.onItemClickListener = this
        }

    }

    private val getAllGifts = Observer<Result<List<GiftsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                adapter.submitItems(it.data)
            }

            is Result.Error -> {
                CustomProgressBar.hide(requireContext())
                handleError(it.throwable)
            }

            is Result.CustomError -> {
                CustomProgressBar.hide(requireContext())
                HandleRequestFailedUtil.showDialogMessage(
                    it.message, requireContext(), childFragmentManager
                )
            }

            is Result.Loading -> {
                CustomProgressBar.show(requireContext())
            }
        }
    }

    private val sendGifts = Observer<Result<List<GiftsResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                AudioPlayer(requireContext(), R.raw.sending_a_gift_t30).startPlayback()
                dismiss()
                viewModel._isSendGiftLiveData.value = true
            }

            is Result.Error -> {
                CustomProgressBar.hide(requireContext())
                handleError(it.throwable)
            }

            is Result.CustomError -> {
                CustomProgressBar.hide(requireContext())
                HandleRequestFailedUtil.showDialogMessage(
                    it.message, requireContext(), childFragmentManager
                )
            }

            is Result.Loading -> {
                CustomProgressBar.show(requireContext())
            }
        }
    }

    override fun onValidationSucceeded() {

    }

    override fun onItemClicked(view: View?, item: GiftsResponseModel, position: Int) {
        viewModel.giftSelectedId.value = item.id
    }

}