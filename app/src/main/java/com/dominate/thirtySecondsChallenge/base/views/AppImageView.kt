package com.dominate.thirtySecondsChallenge.base.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.dominate.thirtySecondsChallenge.R


class AppImageView @JvmOverloads constructor(
    context: Context,
    private var attrs: AttributeSet,
    private val defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        initAttrs()
    }

    private fun initAttrs() {
        val ivAttrs = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AppImageView,
            defStyleAttr,
            0
        )
        handleImageViewSetSrcFromResources(ivAttrs)
    }

    private fun handleImageViewSetSrcFromResources(ivAttrs: TypedArray) {
        val ivSrcRes = ivAttrs.getResourceId(R.styleable.AppImageView_ivSrcRes, -1)
        if (ivSrcRes != -1) setImageResource(ivSrcRes)
    }
}