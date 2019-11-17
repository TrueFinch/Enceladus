package com.truefinch.enceladus.ui.eventFragment


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarUtils
import com.truefinch.enceladus.R
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.recurrence_picker_view.*
import kotlinx.android.synthetic.main.recurrence_picker_view.view.*
import java.sql.Time
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*


class EventFragment : Fragment(), RecurrencePickerDialogFragment.OnRecurrenceSetListener {

    companion object {
        fun newInstance() = EventFragment()
    }

    private val viewModel: EventViewModel by lazy {
        ViewModelProviders.of(this).get(EventViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Calendar.getInstance(Locale.ENGLISH).firstDayOfWeek = Calendar.MONDAY
        Calendar.getInstance().firstDayOfWeek = Calendar.MONDAY
        android.icu.util.Calendar.getInstance(Locale.ENGLISH).firstDayOfWeek =
            android.icu.util.Calendar.MONDAY
        android.icu.util.Calendar.getInstance().firstDayOfWeek = android.icu.util.Calendar.MONDAY
        val selectedDate = viewModel.selectedDateTime.value!!
        startDateTimePicker.dateTime = selectedDate
        endDateTimePicker.dateTime = selectedDate.plusHours(1)
        val recPicker = recurrencePickerView5
        recPicker.dateTime = selectedDate
        recPicker.switch_recurrence.setOnClickListener {
            //            findNavController().navigate(R.id.action_eventFragment_to_recurrencePickerFragment)
            val fm = fragmentManager!!
            val bundle = Bundle()
            val time = Time.from(Instant.now())
            bundle.putLong(
                RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS,
                time.toInstant().toEpochMilli()
            )
            bundle.putString(
                RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE,
                ZonedDateTime.now().zone.toString()
            )
            bundle.putBoolean(RecurrencePickerDialogFragment.BUNDLE_HIDE_SWITCH_BUTTON, true)

            // may be more efficient to serialize and pass in EventRecurrence
            bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, mRrule)

            var dialogFragment: RecurrencePickerDialogFragment? =
                fm.findFragmentByTag(FRAG_TAG_RECUR_PICKER) as RecurrencePickerDialogFragment?
            dialogFragment?.dismiss()
            dialogFragment = RecurrencePickerDialogFragment()
            dialogFragment.arguments = bundle
            dialogFragment.setOnRecurrenceSetListener(this@EventFragment)
            dialogFragment.show(fm, FRAG_TAG_RECUR_PICKER)
        }

    }

    private val FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment"

    var mRrule: String = ""

    private val mEventRecurrence = EventRecurrence()

    override fun onRecurrenceSet(rrule: String) {
        mRrule = rrule
        if (mRrule != null) {
            mEventRecurrence.parse(mRrule)
        }
        populateRepeats()
    }

//    override fun onResume() {
//        // Example of reattaching to the fragment
//        super.onResume()
//        val rpd = fragmentManager?.findFragmentByTag(
//            FRAG_TAG_RECUR_PICKER
//        ) as RecurrencePickerDialogFragment
//        rpd?.setOnRecurrenceSetListener(this)
//    }

    private fun populateRepeats() {
        val r = resources
        var repeatString = ""
        val enabled: Boolean
        if (!TextUtils.isEmpty(mRrule)) {
            repeatString =
                EventRecurrenceFormatter.getRepeatString(this.context, r, mEventRecurrence, true)
        }

        label_recurrence_rule.setText(mRrule + "\n" + repeatString)
    }
}
