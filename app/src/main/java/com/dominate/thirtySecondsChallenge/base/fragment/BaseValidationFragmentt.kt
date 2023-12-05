package com.dominate.thirtySecondsChallenge.base.fragment


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.transition.TransitionManager
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogs.iosdialog.iOSDialogBuilder
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator

abstract class BaseValidationFragmentt<V : ViewDataBinding> : BaseBindingFragmentt<V>(),
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
        if (mView is ConstraintLayout) {
            mView?.let {
                TransitionManager.beginDelayedTransition(it as ViewGroup)
            }
        }

//        errors?.let {
//            if (it.size > 0) {
//                if (it[0].view is HintSpinner) {
//                    showInvalidDataDialog((it[0].view as HintSpinner).prompt.toString())
//                } else if (it[0].view is DropDownEditText) {
//                    it[0].view.setBackgroundResource(R.drawable.spinner_error_background)
//
//                    (it[0].view as DropDownEditText).doBeforeTextChanged { text, start, count, after ->
//                        it[0].view.setBackgroundResource(R.drawable.spinner_background)
//                    }
//                    showInvalidDataDialog(it[0].failedRules.get(0).getMessage(requireContext()))
//                } else if (it[0].view is DropDownTextView) {
//                    it[0].view.setBackgroundResource(R.drawable.spinner_error_background)
//
//                    (it[0].view as DropDownTextView).doBeforeTextChanged { text, start, count, after ->
//                        it[0].view.setBackgroundResource(R.drawable.spinner_background)
//                    }
//                    showInvalidDataDialog(it[0].failedRules.get(0).getMessage(requireContext()))
//                } else if (it[0].view is EditText) {
//                    it[0].view.setBackgroundResource(R.drawable.edit_text_error_background)
//
//                    (it[0].view as EditText).doBeforeTextChanged { text, start, count, after ->
//                        it[0].view.setBackgroundResource(R.drawable.edit_text_background)
//                    }
//                    showInvalidDataDialog(it[0].failedRules.get(0).getMessage(requireContext()))
//                } else {
//                    showInvalidDataDialog(it[0].failedRules.get(0).getMessage(requireContext()))
//                }
//            }
//        }
    }

    fun showInvalidDataDialog(message: String?) {
        iOSDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setSubtitle(message)
            .setBoldPositiveLabel(true)
            .setCancelable(true)
            .setPositiveListener(
                getString(R.string.ok)
            ) { dialog ->
                dialog.dismiss()
            }
            .build().show()
    }

}