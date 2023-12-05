package com.dominate.thirtySecondsChallenge.utils

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogs.iosdialog.iOSDialogBuilder
import com.dominate.thirtySecondsChallenge.data.response.ResponseSubErrorsCodeEnum
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast

object HandleRequestFailedUtil {

    fun handleRequestFailedMessages(
        context: Context,
        fragmentManager: FragmentManager,
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: List<String>?
    ) {

        subErrorCode?.let {
            var errorString = ""
            requestMessage?.forEach {
                errorString += "$it\n "
            }
            when (subErrorCode) {
                ResponseSubErrorsCodeEnum.GENERAL_FAILED ->
                    showDialogMessage(
                        errorString, context, fragmentManager
                    )
                ResponseSubErrorsCodeEnum.InvalidModel -> showDialogMessage(
                    errorString, context, fragmentManager
                )
                ResponseSubErrorsCodeEnum.Unauthorized -> showDialogMessage(
                    errorString, context, fragmentManager
                )
                ResponseSubErrorsCodeEnum.Forbidden -> showDialogMessage(
                    errorString, context, fragmentManager
                )
                ResponseSubErrorsCodeEnum.NotFound -> showDialogMessage(
                    errorString, context, fragmentManager
                )
                else -> showDialogMessage(
                    errorString, context, fragmentManager
                )
            }
        }
    }

    fun showDialogMessage(
        requestMessage: String?,
        context: Context,
        fragmentManager: FragmentManager
    ) {

        iOSDialogBuilder(context)
            .setTitle(context.getString(R.string.alert_dialog_title))
            .setSubtitle(requestMessage)
            .setBoldPositiveLabel(true)
            .setCancelable(true)
            .setPositiveListener(
                context.getString(R.string.ok)
            ) { dialog ->
                dialog.dismiss()
            }
            .build().show()
    }

    fun showDialogMessage(
        requestMessage: List<String>?,
        context: Context,
        fragmentManager: FragmentManager
    ) {
        var errorString = ""
        requestMessage?.forEach {
            errorString += "$it\n "
        }


        iOSDialogBuilder(context)
            .setTitle(context.getString(R.string.alert_dialog_title))
            .setSubtitle(errorString)
            .setBoldPositiveLabel(true)
            .setCancelable(true)
            .setPositiveListener(
                context.getString(R.string.ok)
            ) { dialog ->
                dialog.dismiss()
            }
            .build().show()
    }

    private fun showToastMessage(message: String?, context: Context) {
        message?.let {
            context.shortToast(it)
        }
    }

}