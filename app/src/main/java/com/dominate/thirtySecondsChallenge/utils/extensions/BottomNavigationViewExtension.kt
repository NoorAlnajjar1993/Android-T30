package com.dominate.thirtySecondsChallenge.utils.extensions

import android.animation.Animator
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView


fun BottomNavigationView?.changeVisibilityWithAnimation(visible: Boolean) {
    this?.let {
        it.animate().translationY(if (visible) (0f) else it.height.toFloat()).setListener(
            object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (!visible)
                        it.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationStart(animation: Animator) {
                    if (visible)
                        it.visibility = View.VISIBLE
                }

            }
        )
    }
}