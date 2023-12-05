package com.dominate.thirtySecondsChallenge.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.databinding.DialogCollectRewardBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import kotlinx.android.synthetic.main.dialog_collect_reward.cl_main


fun Context.showDialogCollectReward(
    userCoins: Int? = null,
    userXp: Int? = null,
    userLevel: Int? = null,
    isCancelable: Boolean = true,
    isTimer :Long? = null
) {

    var dialog = Dialog(this)
    dialog.apply {
        val binding = DialogCollectRewardBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels - 60)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params.width = width
        window.attributes = params

        binding.apply {

            if (userCoins != null) {
                 binding.userCoins = userCoins
            }

            if (userXp != null) {
                binding.userXp = userXp
            }

            if (userLevel != null) {
                binding.userLevel = userLevel
            }

            if (isTimer != null){
                Handler(Looper.getMainLooper()).postDelayed({
                    dismiss()
                }, isTimer)
            }

            cl_main.onClick {
                dismiss()
            }
        }

        window.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(true)

        val animation: Animation =
            AnimationUtils.loadAnimation(binding.clMain.context, R.anim.zoom_in)
        binding.clMain.startAnimation(animation)

    }.show()

}

