package com.dominate.thirtySecondsChallenge.common.validation

import android.content.Context
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.views.HintSpinner
import com.mobsandgeeks.saripaar.QuickRule
import javax.inject.Inject

class HintSpinnerRule @Inject constructor() : QuickRule<HintSpinner>() {

    override fun getMessage(context: Context?): String? {
        return context?.getString(R.string.select)
    }

    override fun isValid(view: HintSpinner?): Boolean {
        return view?.selectedItemPosition ?: -1 > -1
    }
}
