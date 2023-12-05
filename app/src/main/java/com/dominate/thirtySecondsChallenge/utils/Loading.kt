package com.dominate.thirtySecondsChallenge.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.dominate.thirtySecondsChallenge.R

object Loading {
    private var alertDialog: AlertDialog? = null

    fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.progress_bar, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog?.show()
    }

    fun hide() {
        alertDialog?.dismiss()
    }
}