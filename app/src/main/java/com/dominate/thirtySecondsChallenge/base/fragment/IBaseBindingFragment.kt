package com.dominate.thirtySecondsChallenge.base.fragment

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.data.response.ResponseSubErrorsCodeEnum


interface IBaseBindingFragment {

    fun getLayoutId(): Int

    /** Called JUST  first time draw view OR when shouldRefreshPageWhenReturn == true**/
    fun onViewVisible() {}

    fun hasNavigation(): Boolean = true

    fun shouldRefreshPageWhenReturn(): Boolean = true

    /**
     * This Method Used TO Show Progress Dialog For Loading
     * Note Should Used JUST when you need to block user interacting with app
     */
    fun showLoadingView(context: Context)

    fun hideLoadingView(context: Context)

    fun handleRequestFailedMessages(
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: List<String>?
    )

    fun showProgressDialog(
        title: String = "",
        message: String,
        isRemovable: Boolean = false
    )

    fun isBindingEnabled(): Boolean = true

    fun addToolbar(
        toolbarView: Toolbar? = null,
        hasToolbar: Boolean,
        hasBackButton: Boolean,
        hasTitle: Boolean = false,
        @StringRes title: Int = R.string.empty_string,
        titleString: String? = null,
        hasSubTitle: Boolean = false,
        @StringRes subTitle: Int = R.string.empty_string,
        showBackArrow: Boolean = false,
        @DrawableRes navigationIcon: Int? = null
    )

    fun updateToolbarTitle(
        hasTitle: Boolean = true,
        @StringRes title: Int = R.string.empty_string,
        titleString: String? = null
    )

    fun updateToolbarTitle(
        hasTitle: Boolean = true,
        @StringRes title: Int = R.string.empty_string,
        titleString: String? = null,
        hasBackButton: Boolean,
        @ColorRes backArrowTint: Int? = null,
        showBackArrow: Boolean = false
    )

    fun updateToolbarSubTitle(
        hasSubTitle: Boolean = true,
        @StringRes subTitle: Int = R.string.empty_string,
        subTitleString: String? = null
    )

    fun onAfterLocaleChanged()
}