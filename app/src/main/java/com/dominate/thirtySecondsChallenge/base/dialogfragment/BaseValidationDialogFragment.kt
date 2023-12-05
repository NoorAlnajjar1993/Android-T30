package com.dominate.thirtySecondsChallenge.base.dialogfragment

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doBeforeTextChanged
import androidx.databinding.ViewDataBinding
import androidx.transition.TransitionManager
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.dialogs.iosdialog.iOSDialogBuilder
import com.dominate.thirtySecondsChallenge.base.views.DropDownEditText
import com.dominate.thirtySecondsChallenge.base.views.DropDownTextView
import com.dominate.thirtySecondsChallenge.base.views.HintSpinner
import com.dominate.thirtySecondsChallenge.utils.anim.zoomOut
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator

abstract class BaseValidationDialogFragment<V : ViewDataBinding> : BaseBindingDialogFragment<V>(),
    Validator.ValidationListener {

    protected val validator: Validator by lazy {
        val validator = Validator(this)
        validator.setValidationListener(this)
        validator
    }

    private lateinit var animation: Animation
    private lateinit var animationDismis: Animation


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animation = AnimationUtils.loadAnimation(
            requireContext(), R.anim.zoom_in_dialog
        )

        binding?.apply {
            root.startAnimation(animation)
        }

        this.isCancelable = true

    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        if (mView is ConstraintLayout) {
            mView?.let {
                TransitionManager.beginDelayedTransition(it as ViewGroup)
            }
        }

        errors?.let {
            if (it.size > 0) {
                if (it[0].view is HintSpinner) {
                    showInvalidDataDialog((it[0].view as HintSpinner).prompt.toString())
                } else if (it[0].view is DropDownEditText) {
                    it[0].view.setBackgroundResource(R.drawable.spinner_error_background)

                    (it[0].view as DropDownEditText).doBeforeTextChanged { text, start, count, after ->
                        it[0].view.setBackgroundResource(R.drawable.spinner_background)
                    }
                    showInvalidDataDialog(it[0].failedRules[0].getMessage(requireContext()))
                } else if (it[0].view is DropDownTextView) {
                    it[0].view.setBackgroundResource(R.drawable.spinner_error_background)

                    (it[0].view as DropDownTextView).doBeforeTextChanged { text, start, count, after ->
                        it[0].view.setBackgroundResource(R.drawable.spinner_background)
                    }
                    showInvalidDataDialog(it[0].failedRules[0].getMessage(requireContext()))
                } else if (it[0].view is EditText) {
                    it[0].view.setBackgroundResource(R.drawable.edit_text_error_background)

                    (it[0].view as EditText).doBeforeTextChanged { text, start, count, after ->
                        it[0].view.setBackgroundResource(R.drawable.edit_text_background)
                    }
                    showInvalidDataDialog(it[0].failedRules[0].getMessage(requireContext()))
                } else {
                    showInvalidDataDialog(it[0].failedRules[0].getMessage(requireContext()))
                }
            }
        }


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

    override fun onResume() {
        super.onResume()
        if (dialog == null) return
        val window: Window = dialog?.window ?: return
        val width = (resources.displayMetrics.widthPixels - 60)
        val height =  (resources.displayMetrics.heightPixels)
        val params: WindowManager.LayoutParams = window.attributes
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params.width = width
        params.height = height
        window.attributes = params
    }

    override fun onCancel(dialog: DialogInterface) {
        requireContext().zoomOut(requireView(), this)
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
    }

    private fun setObservers() {
        dismiss()
    }

}