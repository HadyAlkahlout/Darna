package com.raiyansoft.darnaapp.dialog

import android.app.Dialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerDialog(val listener: DatePickerDialog.OnDateSetListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val day = c[Calendar.DAY_OF_MONTH]
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        return DatePickerDialog(requireContext(), listener, year, month, day)
    }

}