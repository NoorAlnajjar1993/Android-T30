package com.dominate.thirtySecondsChallenge.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.databinding.AnswerDialogBinding


fun Context.showDialogAnswer(
    answer: String? = null,
    isCancelable: Boolean = true,
    timer :Long? = null
) {

    var dialog = Dialog(this)
    dialog.apply {
        val binding = AnswerDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels)
        val height = (resources.displayMetrics.heightPixels)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params.width = width
        params.height = height
        params.gravity = Gravity.BOTTOM
        window.attributes = params

        binding.apply {

            if (answer != null) {
                binding.answer = answer
            }

            if (timer != null){
                Handler(Looper.getMainLooper()).postDelayed({
                    dismiss()
                }, timer)
            }
        }

        window.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(isCancelable)

        val animation: Animation =
            AnimationUtils.loadAnimation(binding.clMain.context, R.anim.slide_up_fast)
        binding.clMain.startAnimation(animation)

    }.show()



}

