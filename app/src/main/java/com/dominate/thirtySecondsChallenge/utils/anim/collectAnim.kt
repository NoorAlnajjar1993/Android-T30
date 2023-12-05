package com.dominate.thirtySecondsChallenge.utils.anim

import android.view.View
import android.view.animation.AlphaAnimation

fun setFadeAnimation(view: View) {
    val anim = AlphaAnimation(0.0f, 1.0f)
    anim.duration = 100
    view.startAnimation(anim)
}


