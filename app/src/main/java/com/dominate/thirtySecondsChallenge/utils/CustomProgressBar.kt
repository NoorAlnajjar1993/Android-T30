package com.dominate.thirtySecondsChallenge.utils

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.R.*
import kotlinx.android.synthetic.main.progress_bar.view.iv_custom


object CustomProgressBar {
    private var dialog: Dialog? = null
    private lateinit var animationImage: Animation

    fun show(context: Context): Dialog? {
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?): Dialog? {
        return show(context, title, false)
    }

    fun show(context: Context, title: CharSequence?, cancelable: Boolean): Dialog? {
        // Dismiss any previously shown dialog
        dismiss()

        return show(context, title, cancelable, null)
    }

    fun hide(context: Context) {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    private fun dismiss() {
        // Dismiss the currently shown dialog, if any
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    fun show(
        context: Context, title: CharSequence?, cancelable: Boolean,
        cancelListener: SearchManager.OnCancelListener?
    ): Dialog? {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflator.inflate(R.layout.progress_bar, null)
        if (title != null) {
            val tv_Title = view.findViewById(R.id.tv_Title) as TextView
            tv_Title.text = title
        }

        animationImage = AnimationUtils.loadAnimation(context, R.anim.pulse)
        animationImage.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Animation ended, replay the animation
                view.iv_custom.startAnimation(animationImage)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.iv_custom.startAnimation(animationImage)

        dialog = Dialog(context, R.style.CustomProgressBar)
        dialog!!.setContentView(view)
        dialog!!.setCancelable(cancelable)
        dialog!!.window

        if (dialog!!.isShowing) {
            dialog!!.dismiss()
        }
        dialog!!.show()
        return dialog
    }

    fun getDialog(): Dialog? {
        return dialog
    }
}
