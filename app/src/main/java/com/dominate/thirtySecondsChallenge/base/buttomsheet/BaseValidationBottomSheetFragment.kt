package com.dominate.thirtySecondsChallenge.base.buttomsheet

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doBeforeTextChanged
import androidx.databinding.ViewDataBinding
import androidx.transition.TransitionManager
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogs.iosdialog.iOSDialogBuilder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator


abstract class BaseValidationBottomSheetFragment<V : ViewDataBinding> : BaseBindingBottomSheetFragment<V>(),
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

        errors?.let {

            if (it.size > 0) {
                if (it[0].view is EditText) {
                    val textInputLayout = it[0].view.parent.parent as TextInputLayout

                    (it[0].view as EditText).doBeforeTextChanged { text, start, count, after ->
                        textInputLayout.error = null
                        textInputLayout.isErrorEnabled = false
                    }
                    it[0].view.isFocusable
                    textInputLayout.error = it[0].failedRules[0].getMessage(requireContext())
                    textInputLayout.isErrorEnabled = true

                } else if (it[0].view is AutoCompleteTextView) {
                    val textInputLayout = it[0].view.parent.parent as TextInputLayout

                    textInputLayout.error = it[0].failedRules[0].getMessage(requireContext())
                    textInputLayout.isErrorEnabled = true

                } else {
                    showDataDialog(it[0].failedRules[0].getMessage(requireContext()))
                }
            }
        }

    }

    fun showDataDialog(message: String?) {
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

    override fun onResume() {
        super.onResume()
        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels)
        val height = (resources.displayMetrics.heightPixels)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params.width = width
        params.height = height
        window.attributes = params
    }

    //    override fun onResume() {
//        super.onResume()
//        if (dialog == null) return
//        val window: Window = dialog?.window ?: return
//        val width = (resources.displayMetrics.widthPixels)
//        val height = (resources.displayMetrics.heightPixels)
//        val params: WindowManager.LayoutParams = window.attributes
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//        params.width = width - 100
////        params.height = height
//        window.attributes = params
//    }

    private fun setObservers() {
        dismiss()
    }

}