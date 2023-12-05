package com.dominate.thirtySecondsChallenge.utils.anim

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer

@SuppressLint("ClickableViewAccessibility")
class AnimationHelper(view: View, context: Context) {

    private val scaleDownDuration = 100L
    private val scaleUpDuration = 100L

    private val animatorScaleX =
        ObjectAnimator.ofFloat(view, View.SCALE_X, view.scaleX, view.scaleX * 0.9f)
    private val animatorScaleY =
        ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, view.scaleY * 0.9f)

    init {
        animatorScaleX.duration = scaleDownDuration
        animatorScaleX.repeatCount = 0
        animatorScaleX.repeatMode = ObjectAnimator.REVERSE

        animatorScaleY.duration = scaleUpDuration
        animatorScaleY.repeatCount = 0
        animatorScaleY.repeatMode = ObjectAnimator.REVERSE

        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> startAnimation(scaleDownDuration, false, context)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> startAnimation(
                    scaleUpDuration,
                    true,
                    context
                )
            }

            false
        }
    }

    private fun startAnimation(duration: Long, reverse: Boolean = false, context: Context) {
        animatorScaleX.duration = duration
        animatorScaleY.duration = duration

        if (reverse) {
            animatorScaleX.reverse()
            animatorScaleY.reverse()

        } else {
            animatorScaleX.start()
            animatorScaleY.start()
//            AudioPlayer(context, R.raw.click_t30).startPlayback()
        }

    }
}