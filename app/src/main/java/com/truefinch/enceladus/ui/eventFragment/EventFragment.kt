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
        private val name: String = "EventFragment"
        private val pattern: String = "eee, dd LLL yyyy, hh:mm"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_event_create, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val recPicker = recurrencePickerView5
        recPicker.switch_recurrence.setOnClickListener {
            if (switch_recurrence.isChecked) {
                RecurrencePickerFragment.newInstance().show(fragmentManager!!, "dialog")
            }
        }

        toolbar = tbEventInstance

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
                viewModel.eventLocation.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tbEventInstance.setNavigationOnClickListener { exitEventFragment() }

        startDateTimePicker.setOnDateTimeChangeListener { zonedDateTime: ZonedDateTime, id: Int ->
            LogD(threadId(), "$name.startDateTimePicker", formatToPattern(pattern, zonedDateTime))
            onDateTimeListener(zonedDateTime, id)
        }
        endDateTimePicker.setOnDateTimeChangeListener { zonedDateTime: ZonedDateTime, id: Int ->
            LogD(threadId(), "$name.endDateTimePicker", formatToPattern(pattern, zonedDateTime))
            onDateTimeListener(zonedDateTime, id)
        }

        viewModel.loadedData.observe(this, Observer {
            if (it == true) {
                LogD(threadId(), "$name.loadedDataObserver", "True")
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
            LogD(threadId(), "$name.eventModeObserver", "to mode $it")
            updateToolBar(it)
            when (it) {
                EventMode.CREATE -> enableUI()
                EventMode.EDIT -> enableUI()
                EventMode.SHOW -> disableUI()
                EventMode.NONE -> {
                    disableUI()
                    setDefault()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            (R.id.action_submit_event) -> {
                viewModel.sendEvent()
                exitEventFragment()
            }
            (R.id.action_edit_event) -> {
                viewModel.eventMode.value = EventMode.EDIT
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onDateTimeListener(zonedDateTime: ZonedDateTime, id: Int) {
        if (id == R.id.startDateTimePicker) {
            viewModel.startDateTime.value = zonedDateTime
        } else {
            viewModel.endDateTime.value = zonedDateTime
        }
    }

    private fun setDefault() {
        LogD(threadId(), "$name.setDefault")
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
        LogD(threadId(), "$name.disableUI")
        changeUIState(false)
    }

    private fun enableUI() {
        LogD(threadId(), "$name.enableUI")
        changeUIState(true)
    }

    private fun changeUIState(state: Boolean) {
        etTitle.inputType = if (state) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL
        etDescription.inputType = if (state) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL
        etLocation.inputType = if (state) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL
        startDateTimePicker.isEnabled = state
        endDateTimePicker.isEnabled = state
        recurrencePickerView5.switch_recurrence.isEnabled = state
        //TODO: enable recurrence picker
    }

    private fun updateToolBar(mode: EventMode) {
        LogD(threadId(), "$name.updateToolBar", "to mode $mode")
        tbEventInstance.menu.findItem(R.id.action_submit_event).isVisible = false
        tbEventInstance.menu.findItem(R.id.action_edit_event).isVisible = false
        when (mode) {
            EventMode.CREATE -> toolbar.menu.findItem(R.id.action_submit_event).isVisible = true
            EventMode.EDIT -> toolbar.menu.findItem(R.id.action_submit_event).isVisible = true
            EventMode.SHOW -> toolbar.menu.findItem(R.id.action_edit_event).isVisible = true
            EventMode.NONE -> {
            }
        }
    }

    private fun exitEventFragment() {
        LogD(threadId(), "$name.goBack", "Drop all data")
        viewModel.eventMode.value = EventMode.NONE
        activity?.onBackPressed()
    }

    private lateinit var toolbar: Toolbar

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
}
