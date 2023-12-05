package com.dominate.thirtySecondsChallenge.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.databinding.DialogInfoDefaultBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import kotlinx.android.synthetic.main.congratulations_dialog.iv_close

fun Context.showDialogInfoDefault(
    title: String? = null,
    subTitle: String? = null,
    isCancelable: Boolean = true,
) {

    var dialog = Dialog(this)
    dialog.apply {
        val binding = DialogInfoDefaultBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels - 60)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params.width = width
        window.attributes = params

        binding.apply {

            binding.title = title
            binding.subTitle = subTitle

            iv_close.onClick {
                dismiss()
            }
        }

        window.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(isCancelable)

        val animation: Animation =
            AnimationUtils.loadAnimation(binding.clMain.context, R.anim.slide_up_fast)
        binding.clMain.startAnimation(animation)

    }.show()


}

