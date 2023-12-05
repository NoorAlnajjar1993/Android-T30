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
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogs.iosdialog.iOSDialogBuilder
import com.dominate.thirtySecondsChallenge.databinding.CongratulationsDialogBinding
import com.dominate.thirtySecondsChallenge.databinding.ShowDialogAnimationBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import kotlinx.android.synthetic.main.congratulations_dialog.iv_close
import kotlinx.android.synthetic.main.show_dialog_animation.cl_main
import kotlinx.android.synthetic.main.show_dialog_animation.lav_Image

fun Context.showInfoDialog(message: String) {
    iOSDialogBuilder(this)
        .setTitle(getString(R.string.alert_dialog_title))
        .setSubtitle(message)
        .setBoldPositiveLabel(true)
        .setCancelable(true)
        .setPositiveListener(
            getString(R.string.ok)
        ) { dialog ->
            dialog.dismiss()
        }
        .build().show()
}

fun Context.showDialog(
    image:Int? = null,
    title:String? = null,
    subTitle:String? = null,
    onPositiveButtonClick: (dialog: Dialog) -> Unit = {},
    isCancelable: Boolean = true,
    isShowBtn : Boolean = false,
    btnText : String = ""
) {

    var dialog = Dialog(this)
    dialog.apply {
        val binding = CongratulationsDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels - 120)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        params.width = width
        window.attributes = params

        binding.apply {

            if (image != null){
                binding.image = image
            }

            if (title != null){
                binding.title = title
            }

            if (title != null){
                binding.subTitle = subTitle
            }

                binding.isShowBtn = isShowBtn
                binding.btnText = btnText

            if (isShowing){
                btnAction.visibility = View.VISIBLE
            }else{
                btnAction.visibility = View.GONE
            }

            iv_close.onClick {
                dismiss()
            }

            onClickListener = View.OnClickListener {
                when (it) {
                    btnAction -> onPositiveButtonClick(dialog)
                }
            }
        }

        window.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(isCancelable)

        binding.root.apply {
            scaleX = 0f
            scaleY = 0f
            pivotX = width.toFloat() / 2
            pivotY = 0f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .start()
        }
    }.show()
}

fun Context.showDialogAnimation(
    onPositiveButtonClick: (dialog: Dialog) -> Unit = {},
    isCancelable: Boolean = true,
    isShowBtn : Boolean = false,
) {

    var dialog = Dialog(this)
    dialog.apply {
        val binding = ShowDialogAnimationBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels - 60)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        params.width = width
        window.attributes = params

        binding.apply {

            lav_Image.onClick {
                dismiss()
            }

            cl_main.onClick {
                dismiss()
            }

            onClickListener = View.OnClickListener {
                when (it) {
//                    btnAction -> onPositiveButtonClick(dialog)
                }
            }
        }

        window.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(isCancelable)

        binding.root.apply {
            scaleX = 0f
            scaleY = 0f
            pivotX = width.toFloat() / 2
            pivotY = 0f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .start()
        }
    }.show()
}