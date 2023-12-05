package com.dominate.thirtySecondsChallenge.base.dialogfragment

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.activity.IBaseBindingActivity
import com.dominate.thirtySecondsChallenge.base.dialogs.progressdialog.ProgressDialogUtil
import com.dominate.thirtySecondsChallenge.data.response.ResponseSubErrorsCodeEnum
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.refreshLocal
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Project: Portal CD
 *
 * @author Hassan Hasanat
 */

abstract class BaseDialogFragment : DialogFragment(), IBaseBindingDialogFragment,OnLocaleChangedListener {


    var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.refreshLocal()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false)
            onViewVisible(mView!!)
        }
        initToolbar()
        observee()
        return mView
    }


    override fun startActivity(intent: Intent?) =
        super.startActivity(
            intent, ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right, R.anim.slide_out_left
            ).toBundle()
        )

    protected open fun onViewVisible(view: View) {
    }

    protected open fun initToolbar() {
    }

    protected open fun observee() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val toolbar:Toolbar = view.findViewById(R.id.toolbar)
//        toolbar.setNavigationOnClickListener {
//            activity?.onBackPressed()
//        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) =
        super.startActivityForResult(
            intent, requestCode, ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right, R.anim.slide_out_left
            ).toBundle()
        )

    override fun showProgressDialog(title: String, message: String, isRemovable: Boolean) {
        activity?.let {
            ProgressDialogUtil.showProgressDialog(it, title, message, isRemovable)
        }
    }

    override fun handleRequestFailedMessages(
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage:List<String>?
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

    override fun showLoadingView(context: Context) {
        ProgressDialogUtil.showLoadingView(context)
    }

    override fun hideLoadingView(context: Context) {
        ProgressDialogUtil.hideLoadingView(context)
    }

    fun handleError(throwable: Throwable?) {
        when (throwable) {
            is IOException -> {
                HandleRequestFailedUtil.showDialogMessage(
                    listOf(getString(R.string.error_no_internet)),
                    requireContext(),
                    childFragmentManager
                )
            }
            is SocketTimeoutException -> {
                HandleRequestFailedUtil.showDialogMessage(
                    listOf(getString(R.string.error_server)),
                    requireContext(),
                    childFragmentManager
                )
            }
            is HttpException -> {

            }
            else -> {
                HandleRequestFailedUtil.showDialogMessage(
                    listOf(throwable?.message.toString()), //getString(R.string.error_msg)
                    requireContext(),
                    childFragmentManager
                )
            }
        }
    }

    override fun onAfterLocaleChanged() {
    }

    override fun onBeforeLocaleChanged() {

    }

}