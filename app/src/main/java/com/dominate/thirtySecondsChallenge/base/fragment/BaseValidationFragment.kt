package com.dominate.thirtySecondsChallenge.base.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.transition.TransitionManager
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator


abstract class BaseValidationFragment<V : ViewDataBinding> : BaseBindingFragment<V>(),
    Validator.ValidationListener {

    protected val validator: Validator by lazy {
        val validator = Validator(this)
        validator.setValidationListener(this)
        validator
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        if (rootView is ConstraintLayout) {
            rootView?.let {
                TransitionManager.beginDelayedTransition(it as ViewGroup)
            }
        }

//        errors?.let {
//            if (it.size > 0) {
//                if (it[0].view is HintSpinner) {
//                    showDataDialog((it[0].view as HintSpinner).prompt.toString())
//                } else if (it[0].view is DropDownEditText) {
//                    it[0].view.setBackgroundResource(R.drawable.spinner_error_background)
//
//                    (it[0].view as DropDownEditText).doBeforeTextChanged { text, start, count, after ->
//                        it[0].view.setBackgroundResource(R.drawable.spinner_background)
//                    }
//                    showDataDialog(it[0].failedRules.get(0).getMessage(requireContext()))
//                } else if (it[0].view is DropDownTextView) {
//                    it[0].view.setBackgroundResource(R.drawable.spinner_error_background)
//
//                    (it[0].view as DropDownTextView).doBeforeTextChanged { text, start, count, after ->
//                        it[0].view.setBackgroundResource(R.drawable.spinner_background)
//                    }
//                    showDataDialog(it[0].failedRules.get(0).getMessage(requireContext()))
//                } else if (it[0].view is EditText) {
//                    it[0].view.setBackgroundResource(R.drawable.edit_text_error_background)
//
//                    (it[0].view as EditText).doBeforeTextChanged { text, start, count, after ->
//                        it[0].view.setBackgroundResource(R.drawable.edit_text_background)
//                    }
//                    showDataDialog(it[0].failedRules.get(0).getMessage(requireContext()))
//                } else {
//                    showDataDialog(it[0].failedRules.get(0).getMessage(requireContext()))
//                }
//            }
//        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)


    }


}