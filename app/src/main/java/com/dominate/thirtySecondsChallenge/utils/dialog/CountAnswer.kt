package com.dominate.thirtySecondsChallenge.utils.dialog

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class CountAnswer : DialogFragment() {
    private var listener: OnDateSetListener? = null

    fun setListener(listener: OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater: LayoutInflater = activity!!.layoutInflater
        val cal: Calendar = Calendar.getInstance()
        val dialog: View = inflater.inflate(com.dominate.thirtySecondsChallenge.R.layout.fragment_count_answer, null)
        val pickerCount = dialog.findViewById<View>(com.dominate.thirtySecondsChallenge.R.id.picker_count) as NumberPicker
        pickerCount.minValue = 1
        pickerCount.maxValue = 30
        pickerCount.value = cal.get(Calendar.MONTH)
        builder.setView(dialog) // Add action buttons
            .setPositiveButton(R.string.ok,
                DialogInterface.OnClickListener { dialog, id ->
                    listener!!.onDateSet(
                        null,
                        0,
                        pickerCount.value,
                        0
                    )
                })

        return builder.create()
    }

    companion object {
        private const val MAX_YEAR = 2099
    }
}