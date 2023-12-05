package com.dominate.thirtySecondsChallenge.ui.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.notification.UserNotificationResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentNotificationBinding
import com.dominate.thirtySecondsChallenge.ui.notification.adapter.NotificationAdapter
import com.dominate.thirtySecondsChallenge.ui.player.PlayerProfileFragment
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notification.iv_back
import kotlinx.android.synthetic.main.fragment_notification.rv_Notification
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : BaseBindingFragment<FragmentNotificationBinding>(),
    OnItemClickListener<UserNotificationResponseModel> {

    override fun getLayoutId(): Int = R.layout.fragment_notification

    private val viewModel by activityViewModels<NotificationViewModel>()

    @Inject
    lateinit var notificationAdapter: NotificationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpViewsListeners()
        setRecycleView()
    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getAllUserNotification().observe(viewLifecycleOwner, getAllUserNotification)
    }

    private fun setUpViewsListeners() {
        iv_back.onClick {
            findNavController().popBackStack()
        }

    }

    private fun setRecycleView() {
        requireContext().let {
            notificationAdapter = NotificationAdapter()
            rv_Notification.adapter = notificationAdapter
            notificationAdapter.onItemClickListener = this
        }


    }

    private val getAllUserNotification = Observer<Result<List<UserNotificationResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                if (!it.data.isNullOrEmpty()){
                    notificationAdapter.submitItems(it.data)
                    viewModel.readNotification()
                    viewModel.isEmptyNotification.value = false
                }else{
                    viewModel.isEmptyNotification.value = true
                }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: UserNotificationResponseModel, position: Int) {
        if (item.notificationTypeId == 20) {
            val playerProfileFragment =
                PlayerProfileFragment.newInstance(item.userFriendId ?: -1)
            playerProfileFragment.show(parentFragmentManager, "player Profile Fragment")
        }

    }

}