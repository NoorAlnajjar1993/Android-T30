package com.dominate.thirtySecondsChallenge.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import com.dominate.thirtySecondsChallenge.databinding.DialogGiftFriendsBinding
import com.dominate.thirtySecondsChallenge.databinding.DialogInfoGiftBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import kotlinx.android.synthetic.main.congratulations_dialog.iv_close

fun Context.showDialogInfoGift(
    image: String? = null,
    title: String? = null,
    coins: String? = null,
    isCancelable: Boolean = true,
) {

    var dialog = Dialog(this)
    dialog.apply {
        val binding = DialogInfoGiftBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels)
        val height = (resources.displayMetrics.heightPixels)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        params.width = width
        params.height = height
        window.attributes = params

        binding.apply {

            if (image != null) {
                binding.image = image
            }

            if (title != null) {
                binding.title = title
            }
            if (coins != null) {
                binding.coins = coins
            }

            iv_close.onClick {
                dismiss()
            }

        }

        window.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(isCancelable)

        binding.root.apply {
            scaleX = 0f
            scaleY = 0f
            pivotX = width.toFloat() / 2
            pivotY = height.toFloat() / 2
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .start()
        }
    }.show()
}
