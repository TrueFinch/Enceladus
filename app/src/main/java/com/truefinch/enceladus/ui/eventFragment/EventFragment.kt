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

class EventFragment : Fragment() {
    companion object {
        fun newInstance() = EventFragment()
    }

    private val viewModel: EventViewModel by lazy {
        ViewModelProviders.of(this).get(EventViewModel::class.java)
    }

    private val sharedViewModel: SharedViewModel by lazy {
        activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_submit_event) {
            //todo: send command to viewmodel to send event on server
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        setDefault()
        val recPicker = recurrencePickerView5
        recPicker.switch_recurrence.setOnClickListener {
            if (switch_recurrence.isChecked) {
                EnceladusApp.instance.tabManager.currentNavController!!.navigate(R.id.action_eventFragment_to_recurrencePickerFragment)
            }
        }

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.eventTitle = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.eventTitle = s.toString()
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
    }

    private fun onDateTimeListener(zonedDateTime: ZonedDateTime, id: Int) {
        if (id == R.id.startDateTimePicker) {
            viewModel.startDateTime = zonedDateTime
        } else {
            viewModel.endDateTime = zonedDateTime
        }
    }

    private fun setDefault() {
        viewModel.clear()
        val selectedDate = sharedViewModel.selectedDateTime
        startDateTimePicker.dateTime = selectedDate
        endDateTimePicker.dateTime = selectedDate.plusHours(1)
        val recPicker = recurrencePickerView5
        recPicker.dateTime = selectedDate
        recPicker.switch_recurrence.isChecked = false
        titleEditText.setText("")
        descriptionEditText.setText("")
    }
}
