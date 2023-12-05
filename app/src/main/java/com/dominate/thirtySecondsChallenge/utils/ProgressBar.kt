package com.dominate.thirtySecondsChallenge.utils

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Window
import android.widget.TextView
import com.dominate.thirtySecondsChallenge.R

object ProgressBar {

    private var loadingDialog: Dialog? = null

    fun getLoadingDialog(context: Context): Dialog {
        if (loadingDialog == null) {
            loadingDialog = createLoadingDialog(context)
        }

        return loadingDialog!!
    }

    fun showLoadingView(context: Context) {
        val dialog = getLoadingDialog(context)
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun hideLoadingView(context: Context) {
        val dialog = getLoadingDialog(context)
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    private fun createLoadingDialog(context: Context): Dialog {
        val dialog = Dialog(context, R.style.CustomProgressBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun showProgressDialog(
        context: Context?,
        title: String = "",
        message: String,
        isRemovable: Boolean = false
    ) {
        if (context == null) return

        val dialog = getLoadingDialog(context)
        val messageText = dialog.findViewById<TextView>(R.id.tv_Title)

        messageText.text = message
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title)
        }

        dialog.setCancelable(isRemovable)
        dialog.setCanceledOnTouchOutside(isRemovable)

        if (!dialog.isShowing) {
            dialog.show()
        }
    }
}