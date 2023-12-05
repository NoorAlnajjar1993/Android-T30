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
import com.dominate.thirtySecondsChallenge.databinding.DialogStratGameBinding


fun Context.showDialogLottie(
    image: Int? = null,
    title: String? = null,
    onPositiveButtonClick: (dialog: Dialog) -> Unit = {},
    isCancelable: Boolean = true,
    isShowBtn: Boolean = false,
    btnText: String = "",
    isTimer :Long? = null
) {

    var dialog = Dialog(this)
    dialog.apply {
        val binding = DialogStratGameBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels - 60)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params.width = width
        window.attributes = params

        binding.apply {

            if (image != null) {
                ivImage.setAnimation(image)
                binding.image = image
            }

            if (title != null) {
                binding.title = title
            }

            binding.isShowBtn = isShowBtn
            binding.btnText = btnText

            if (isShowing) {
                btnAction.visibility = View.VISIBLE
            } else {
                btnAction.visibility = View.GONE
            }

            onClickListener = View.OnClickListener {
                when (it) {
                    btnAction -> onPositiveButtonClick(dialog)
                }
            }

            if (isTimer != null){
                Handler(Looper.getMainLooper()).postDelayed({
                    dismiss()
                }, isTimer)
            }
        }

        window.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(isCancelable)

        val animation: Animation =
            AnimationUtils.loadAnimation(binding.clMain.context, R.anim.zoom_in)
        binding.clMain.startAnimation(animation)

    }.show()



}

