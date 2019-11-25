package com.truefinch.enceladus.ui.eventFragment


import android.os.Bundle
import android.text.Editable
import android.text.InputType
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
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.truefinch.enceladus.ui.pickers.RecurrencePickerFragment
import com.truefinch.enceladus.utils.DateFormatterUtil.Companion.formatToPattern
import com.truefinch.enceladus.utils.EventMode
import com.truefinch.enceladus.utils.LogD
import com.truefinch.enceladus.utils.threadId
import okhttp3.internal.format
import org.w3c.dom.Text

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
                RecurrencePickerFragment.newInstance().show(fragmentManager!!, "dialog")
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
            LogD(threadId(), "EventFragment.goBack", "Drop all data")
            viewModel.eventMode.value = EventMode.NONE
            activity?.onBackPressed()
        }

        startDateTimePicker.setOnDateTimeChangeListener { zonedDateTime: ZonedDateTime, id: Int ->
            LogD(
                threadId(), "EventFragment.startDateTimePicker",
                formatToPattern("eee, dd LLL yyyy, hh:mm", zonedDateTime)
            )
            onDateTimeListener(zonedDateTime, id)
        }
        endDateTimePicker.setOnDateTimeChangeListener { zonedDateTime: ZonedDateTime, id: Int ->
            LogD(
                threadId(), "EventFragment.endDateTimePicker",
                formatToPattern("eee, dd LLL yyyy, hh:mm", zonedDateTime)
            )
            onDateTimeListener(zonedDateTime, id)
        }

        viewModel.loadedData.observe(this, Observer {
            if (it == true) {
                LogD(threadId(), "EventFragment.loadedDataObserver", "True")
                etTitle.setText(viewModel.eventTitle.value)
                etDescription.setText(viewModel.eventDescription.value)
                etLocation.setText(viewModel.eventLocation.value)
                startDateTimePicker.dateTime = viewModel.startDateTime.value!!
                endDateTimePicker.dateTime = viewModel.endDateTime.value!!
                //TODO: get recurrence picker value
            }
        })

        viewModel.eventMode.observe(this, Observer {
            it!!
            LogD(threadId(), "EventFragment.eventModeObserver", "to mode $it")
            updateToolBar(it)
            when (it) {
                EventMode.CREATE -> {
                    enableUI()
                }
                EventMode.EDIT -> {
                    enableUI()
                }
                EventMode.SHOW -> {
                    disableUI()
                }
                EventMode.NONE -> {
                    disableUI()
                    setDefault()
                }
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
        LogD(threadId(), "EventFragment.setDefault")
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
        LogD(threadId(), "EventFragment.disableUI")
        changeUIState(false)
    }

    private fun enableUI() {
        LogD(threadId(), "EventFragment.enableUI")
        changeUIState(true)
    }

    private fun changeUIState(state: Boolean) {
        etTitle.inputType = if (state) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL
        etDescription.inputType = if (state) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL
        etLocation.inputType = if (state) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL
        startDateTimePicker.isEnabled = state
        endDateTimePicker.isEnabled = state
        recurrencePickerView5.isEnabled = state
        //TODO: enable recurrence picker
    }

    private lateinit var toolbar: Toolbar

    private fun updateToolBar(mode: EventMode) {
        LogD(threadId(), "EventFragment.updateToolBar", "to mode $mode")
        tbEventInstance.menu.findItem(R.id.action_submit_event).isEnabled = false
        tbEventInstance.menu.findItem(R.id.action_edit_event).isEnabled = false
        when (mode) {
            EventMode.CREATE -> tbEventInstance.menu.findItem(R.id.action_submit_event).isEnabled =
                true
            EventMode.EDIT -> tbEventInstance.menu.findItem(R.id.action_submit_event).isEnabled =
                true
            EventMode.SHOW -> tbEventInstance.menu.findItem(R.id.action_edit_event).isEnabled = true
            EventMode.NONE -> {
            }
        }
    }
}
