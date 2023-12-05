package com.dominate.thirtySecondsChallenge.base.activity

import android.os.Bundle
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.dominate.thirtySecondsChallenge.utils.extensions.refreshLocal

abstract class BaseActivity : LocalizationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        this.refreshLocal()
        super.onCreate(savedInstanceState)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.decorView.layoutDirection =
            if (super.getCurrentLanguage()
                    .toString() == "ar"
            ) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}