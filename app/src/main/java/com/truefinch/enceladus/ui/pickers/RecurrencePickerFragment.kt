package com.truefinch.enceladus.ui.pickers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.truefinch.enceladus.R
import kotlinx.android.synthetic.main.fragment_recurrence_picker.*

class RecurrencePickerFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    private val recurrenceTypes by lazy {
        arrayOf(resources.getStringArray(R.array.recurrence_types))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recurrence_picker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        val spinnerAdapter = ArrayAdapter(
//            this.context!!,
//            R.layout.support_simple_spinner_dropdown_item,
//            recurrenceTypes
//        ).apply {
//            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        }
//
//        spinner_rec_type.adapter = spinnerAdapter
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}
