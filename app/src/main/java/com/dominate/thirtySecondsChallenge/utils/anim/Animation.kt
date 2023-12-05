package com.dominate.thirtySecondsChallenge.utils.anim

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.ui.creategame.CreateGameGroupFragment
import kotlinx.android.synthetic.main.fragment_home.shine

fun Context?.slideDown(view: View) {
    var animSlideDown = AnimationUtils.loadAnimation(
        this, R.anim.slide_down
    )
    view.startAnimation(animSlideDown)
}

fun Context?.slideDownFast(view: View) {
    var animSlideDownFast = AnimationUtils.loadAnimation(
        this, R.anim.slide_down_fast
    )
    view.startAnimation(animSlideDownFast)
}

fun Context?.Shaking(view: View) {
    var animShaking = AnimationUtils.loadAnimation(
        this, R.anim.shaking
    )
    view.startAnimation(animShaking)
}

fun Context?.slideUp(view: View) {
    var animSslideUp = AnimationUtils.loadAnimation(
        this, R.anim.slide_up
    )
    view.startAnimation(animSslideUp)
}

fun Context?.zoomIn(view: View) {
    var anim = AnimationUtils.loadAnimation(
        this, R.anim.zoom_in
    )
    view.startAnimation(anim)
}

fun Context?.zoomOut(view: View, dialog: DialogFragment) {
    var anim = AnimationUtils.loadAnimation(
        this, R.anim.zoom_out_dialog
    )

    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(p0: Animation?) {
            dialog.dismiss()
        }

        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationRepeat(p0: Animation?) {
        }

    })

    view.startAnimation(anim)
}
