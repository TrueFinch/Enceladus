package com.truefinch.enceladus.ui.eventFragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.truefinch.enceladus.EnceladusApp
import com.truefinch.enceladus.R
import com.truefinch.enceladus.SharedViewModel
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.recurrence_picker_view.*
import kotlinx.android.synthetic.main.recurrence_picker_view.view.*
import java.time.ZonedDateTime
import android.view.MenuInflater
import androidx.lifecycle.Observer

class EventFragment : Fragment() {
    companion object {
        fun newInstance() = EventFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private val viewModel: EventViewModel by lazy {
        activity?.run {
            ViewModelProviders.of(this)[EventViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val sharedViewModel: SharedViewModel by lazy {
        activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_submit_event) {
            viewModel.apply {
                //TODO: implement send event to the server
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_event_create, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        setDefault()
        val recPicker = recurrencePickerView5
        recPicker.switch_recurrence.setOnClickListener {
            if (switch_recurrence.isChecked) {
                EnceladusApp.instance.tabManager.currentNavController!!.navigate(R.id.action_eventFragment_to_recurrencePickerFragment)
            }
        }

        etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.eventTitle.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.eventDescription.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.eventDescription.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tbEventInstance.setNavigationOnClickListener {
            setDefault()
            activity?.onBackPressed()
        }

        startDateTimePicker.setOnDateTimeChangeListener { zonedDateTime: ZonedDateTime, id: Int ->
            onDateTimeListener(zonedDateTime, id)
        }
        endDateTimePicker.setOnDateTimeChangeListener { zonedDateTime: ZonedDateTime, id: Int ->
            onDateTimeListener(zonedDateTime, id)
        }

        viewModel.loadedData.observe(this, Observer {
            if (it == true) {
            }
        })
    }

    private fun onDateTimeListener(zonedDateTime: ZonedDateTime, id: Int) {
        if (id == R.id.startDateTimePicker) {
            viewModel.startDateTime.value = zonedDateTime
        } else {
            viewModel.endDateTime.value = zonedDateTime
        }
    }

    private fun setDefault() {
        val selectedDate = sharedViewModel.selectedDateTime
        startDateTimePicker.dateTime = selectedDate
        endDateTimePicker.dateTime = selectedDate.plusHours(1)
        val recPicker = recurrencePickerView5
        recPicker.dateTime = selectedDate
        recPicker.switch_recurrence.isChecked = false
        etTitle.setText("")
        etDescription.setText("")
        viewModel.clear()
    }

    private fun disableUI() {
        etTitle.isEnabled = false
        etDescription.isEnabled = false
        etLocation.isEnabled = false
        startDateTimePicker.isEnabled = false
        endDateTimePicker.isEnabled = false
        //TODO: disable recurrence picker
    }

    private fun enableUI() {
        etTitle.isEnabled = true
        etDescription.isEnabled = true
        etLocation.isEnabled = true
        startDateTimePicker.isEnabled = true
        endDateTimePicker.isEnabled = true
        //TODO: enable recurrence picker
    }
}
