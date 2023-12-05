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
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import kotlinx.android.synthetic.main.congratulations_dialog.iv_close

    fun Context.showDialogGiftFriends(
        image:Int? = null,
        title:String? = null,
        subTitle:String? = null,
        subTitleTwo:String? = null,
        onPositiveButtonClick: (dialog: Dialog) -> Unit = {},
        isCancelable: Boolean = true,
        btnText : String = "أرسلها كهدية"
    ) {

        var dialog = Dialog(this)
        dialog.apply {
            val binding = DialogGiftFriendsBinding.inflate(LayoutInflater.from(context))
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

                binding.subTitleTwo = subTitleTwo

                binding.btnText = btnText

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
