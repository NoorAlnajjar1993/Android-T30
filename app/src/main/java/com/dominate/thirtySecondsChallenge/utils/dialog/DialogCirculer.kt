package com.dominate.thirtySecondsChallenge.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.databinding.CircularDialogBinding


fun Context.showDialogCircular(
    message: String? = null,
    image: Int? = null,
    timer: Long? = null,
    subMessage: String? = null,
) {

    var dialog = Dialog(this)
    dialog.apply {
        val binding = CircularDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params.width = width
        window.attributes = params

        binding.apply {

            if (message != null) {
                binding.message = message
            }
            binding.subMessage = subMessage

            if (image != null) {
                binding.image = image
            }

            if (timer != null) {
                Handler(Looper.getMainLooper()).postDelayed({
                    dismiss()
                }, timer)
            }
        }

        window.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(true)

        val animation: Animation =
            AnimationUtils.loadAnimation(binding.clMain.context, R.anim.zoom_in)
        binding.clMain.startAnimation(animation)

    }.show()

}

