package com.truefinch.enceladus.ui.pickers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.truefinch.enceladus.R
import kotlinx.android.synthetic.main.fragment_recurrence_picker.*
import java.sql.Time
import java.time.DayOfWeek
import java.util.*

class RecurrencePickerFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    internal var currentRecType: RecurrenceType
        get() = _currentRecType
        set(value) {
            _currentRecType = value
        }

    internal enum class RecurrenceType {
        HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recurrence_picker, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val spinnerAdapter = ArrayAdapter<String>(
            this.context!!,
            R.layout.support_simple_spinner_dropdown_item,
            recurrenceOption
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinner_rec_type.adapter = spinnerAdapter
        spinner_rec_type.onItemSelectedListener = this
        spinner_rec_type.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        for (button in weekOfDaysButtons) {
            button.setOnClickListener {
                val current = dayOfWeekShortNameToDayOfWeekEnum[button.text]!!
                if (!chosenDays.contains(current)) {
                    button.setBackgroundColor(
                        ContextCompat.getColor(context!!, R.color.colorPrimary)
                    )
                    chosenDays.add(current)
                } else {
                    button.setBackgroundColor(
                        ContextCompat.getColor(context!!, R.color.colorWhite)
                    )
                    chosenDays.remove(current)
                }
            }
            button.setBackgroundColor(
                ContextCompat.getColor(context!!, R.color.colorWhite)
            )
        }
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        currentRecType = optionToRecType[recurrenceOption[position]]!!
        tvIntervalUnit.text = recTypeToUnit[currentRecType]
        etIntervalValue.setText(defaultInterval.toString())

        lytDaysOfWeek1.isEnabled = false
        lytDaysOfWeek2.isEnabled = false

        when (currentRecType) {
            RecurrenceType.HOURLY -> {
            }
            RecurrenceType.DAILY -> {
            }
            RecurrenceType.WEEKLY -> {
                lytDaysOfWeek1.isEnabled = true
                lytDaysOfWeek2.isEnabled = true

            }
            RecurrenceType.MONTHLY -> {
            }
            RecurrenceType.YEARLY -> {
            }
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    private val weekOfDaysButtons by lazy {
        arrayListOf(
            btnMonday,
            btnTuesday,
            btnWednesday,
            btnThursday,
            btnFriday,
            btnSaturday,
            btnSunday
        )
    }

    private val recTypeToOption by lazy {
        hashMapOf(
            RecurrenceType.HOURLY to recurrenceOption[0]!!,
            RecurrenceType.DAILY to recurrenceOption[1]!!,
            RecurrenceType.WEEKLY to recurrenceOption[2]!!,
            RecurrenceType.MONTHLY to recurrenceOption[3]!!,
            RecurrenceType.YEARLY to recurrenceOption[4]!!
        )
    }

    private val recTypeToUnit by lazy {
        hashMapOf(
            RecurrenceType.HOURLY to recurrenceUnit[0]!!,
            RecurrenceType.DAILY to recurrenceUnit[1]!!,
            RecurrenceType.WEEKLY to recurrenceUnit[2]!!,
            RecurrenceType.MONTHLY to recurrenceUnit[3]!!,
            RecurrenceType.YEARLY to recurrenceUnit[4]!!
        )
    }

    private val recTypeToUnits by lazy {
        hashMapOf(
            RecurrenceType.HOURLY to recurrenceUnits[0]!!,
            RecurrenceType.DAILY to recurrenceUnits[1]!!,
            RecurrenceType.WEEKLY to recurrenceUnits[2]!!,
            RecurrenceType.MONTHLY to recurrenceUnits[3]!!,
            RecurrenceType.YEARLY to recurrenceUnits[4]!!
        )
    }

    private val optionToRecType by lazy {
        hashMapOf(
            recurrenceOption[0]!! to RecurrenceType.HOURLY,
            recurrenceOption[1]!! to RecurrenceType.DAILY,
            recurrenceOption[2]!! to RecurrenceType.WEEKLY,
            recurrenceOption[3]!! to RecurrenceType.MONTHLY,
            recurrenceOption[4]!! to RecurrenceType.YEARLY
        )
    }

    private val dayOfWeekShortNameToDayOfWeekEnum by lazy {
        hashMapOf(
            dayOfWeekShortNames[0] to DayOfWeek.MONDAY,
            dayOfWeekShortNames[1] to DayOfWeek.TUESDAY,
            dayOfWeekShortNames[2] to DayOfWeek.WEDNESDAY,
            dayOfWeekShortNames[3] to DayOfWeek.THURSDAY,
            dayOfWeekShortNames[4] to DayOfWeek.FRIDAY,
            dayOfWeekShortNames[5] to DayOfWeek.SATURDAY,
            dayOfWeekShortNames[6] to DayOfWeek.SUNDAY
        )
    }

    private val recurrenceOption by lazy {
        resources.getStringArray(R.array.recurrence_type_items)
    }
    private val recurrenceUnit by lazy {
        resources.getStringArray(R.array.recurrence_unit)
    }
    private val recurrenceUnits by lazy {
        resources.getStringArray(R.array.recurrence_units)
    }

    private val dayOfWeekShortNames by lazy {
        arrayOf(
            resources.getString(R.string.lbl_short_monday),
            resources.getString(R.string.lbl_short_tuesday),
            resources.getString(R.string.lbl_short_wednesday),
            resources.getString(R.string.lbl_short_thursday),
            resources.getString(R.string.lbl_short_friday),
            resources.getString(R.string.lbl_short_saturday),
            resources.getString(R.string.lbl_short_sunday)
        )
    }

    private var _currentRecType: RecurrenceType = RecurrenceType.HOURLY

    private val defaultInterval: Int = 1

    private var chosenDays: MutableSet<DayOfWeek> = mutableSetOf()
}
