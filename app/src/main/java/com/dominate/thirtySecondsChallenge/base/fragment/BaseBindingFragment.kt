package com.dominate.thirtySecondsChallenge.base.fragment

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.bumptech.glide.load.HttpException
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.activity.IBaseBindingActivity
import com.dominate.thirtySecondsChallenge.base.dialogs.iosdialog.iOSDialogBuilder
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.refreshLocal
import com.dominate.thirtySecondsChallenge.base.dialogs.progressdialog.ProgressDialogUtil
import com.dominate.thirtySecondsChallenge.data.response.ResponseSubErrorsCodeEnum
import com.dominate.thirtySecondsChallenge.utils.ProgressBar
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseBindingFragment<BINDING : ViewDataBinding> : Fragment(),
    IBaseBindingFragment, OnLocaleChangedListener {

    protected var binding: BINDING? = null
    protected lateinit var navigationController: NavController

    var rootView: View? = null
    private var shouldCallVisibleView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.refreshLocal()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (rootView == null || shouldRefreshPageWhenReturn()) {
            when {
                binding != null -> {
                    rootView = binding?.root

                }

                isBindingEnabled() -> {
                    binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

                    //To use a LiveData object with your binding class,
                    // you need to specify a lifecycle owner to define the scope of the LiveData object.
                    binding?.lifecycleOwner = this

                    rootView = binding?.root
                }

                else -> {
                    rootView = inflater.inflate(getLayoutId(), container, false)
                }
            }

            shouldCallVisibleView = true

        } else {
            shouldCallVisibleView = false
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (shouldCallVisibleView) {
            onViewVisible()
        }

        if (hasNavigation())
            navigationController = Navigation.findNavController(view)
    }


    override fun startActivity(intent: Intent?) =
        super.startActivity(
            intent, ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right, R.anim.slide_out_left
            ).toBundle()
        )


    override fun startActivityForResult(intent: Intent?, requestCode: Int) =
        super.startActivityForResult(
            intent, requestCode, ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right, R.anim.slide_out_left
            ).toBundle()
        )

    override fun showLoadingView(context: Context) {
//        CustomProgressBar.show(context)
        ProgressBar.showLoadingView(context)
    }

    override fun hideLoadingView(context: Context) {
//        CustomProgressBar.hide(context)
        ProgressBar.hideLoadingView(context)

    }

    override fun showProgressDialog(title: String, message: String, isRemovable: Boolean) {
        activity?.let {
            ProgressDialogUtil.showProgressDialog(it, title, message, isRemovable)
        }
    }

    override fun handleRequestFailedMessages(
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: List<String>?
    ) {
        context?.let {
            HandleRequestFailedUtil.handleRequestFailedMessages(
                it,
                childFragmentManager,
                errorCode,
                subErrorCode,
                requestMessage
            )
        }

    }

    override fun addToolbar(
        toolbarView: Toolbar?,
        hasToolbar: Boolean,
        hasBackButton: Boolean,
        hasTitle: Boolean,
        title: Int,
        titleString: String?,
        hasSubTitle: Boolean,
        subTitle: Int,
        showBackArrow: Boolean,
        navigationIcon: Int?
    ) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).addToolbar(
                toolbarView = toolbarView,
                hasToolbar = hasToolbar,
                hasBackButton = hasBackButton,
                hasTitle = hasTitle,
                title = title,
                titleString = titleString,
                hasSubTitle = hasSubTitle,
                subTitle = subTitle,
                showBackArrow = showBackArrow,
                navigationIcon = navigationIcon
            )
        }
    }

    override fun updateToolbarTitle(hasTitle: Boolean, title: Int, titleString: String?) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarTitle(
                hasTitle, title, titleString
            )
        }
    }

    override fun updateToolbarTitle(
        hasTitle: Boolean,
        title: Int,
        titleString: String?,
        hasBackButton: Boolean,
        backArrowTint: Int?,
        showBackArrow: Boolean
    ) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarTitle(
                hasTitle, title, titleString, hasBackButton, backArrowTint, showBackArrow
            )
        }
    }

    override fun updateToolbarSubTitle(
        hasSubTitle: Boolean,
        subTitle: Int,
        subTitleString: String?
    ) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarSubTitle(
                hasSubTitle, subTitle, subTitleString
            )
        }
    }

    override fun onAfterLocaleChanged() {

    }

    override fun onBeforeLocaleChanged() {

    }

    fun handleError(throwable: Throwable?) {
        when (throwable) {
            is IOException -> {
                requireView().showSnackbar(getString(R.string.error_no_internet),R.drawable.snackbar_error)
            }

            is SocketTimeoutException -> {
                requireView().showSnackbar(getString(R.string.error_server),R.drawable.snackbar_error)
            }

            is HttpException -> {

            }
            else -> {
                requireView().showSnackbar(throwable?.message.toString(),R.drawable.snackbar_error)
                Log.i("eeeeeeeeee", throwable?.message.toString())
//                longToast(getString(R.string.error_msg))
            }
        }
    }

    fun showDataDialog(message: String?) {
        iOSDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setSubtitle(message)
            .setBoldPositiveLabel(true)
            .setCancelable(true)
            .setPositiveListener(
                getString(R.string.ok)
            ) { dialog ->
                dialog.dismiss()
            }
            .build().show()
    }

}