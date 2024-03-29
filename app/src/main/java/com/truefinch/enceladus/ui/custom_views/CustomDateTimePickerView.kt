package com.truefinch.enceladus.ui.custom_views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.truefinch.enceladus.R
import kotlinx.android.synthetic.main.custom_date_time_picker_view.view.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * TODO: document your custom view class.
 */
class CustomDateTimePickerView : LinearLayout {
    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        inflate(context, R.layout.custom_date_time_picker_view, this)
        // Load attributes
        local_date_time.isEnabled = false
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.CustomDateTimePickerView, defStyle, 0
        )
        date_time_label.text = a.getString(R.styleable.CustomDateTimePickerView_date_time_label)
        a.recycle()

        date_time_picker.setOnClickListener(this::onClickListener)
    }

// "eee, dd LLL yyyy, hh:mm a"

    private fun updateViewDateTime() {
        date_time_picker.setText(DateTimeFormatter.ofPattern(_pattern).format(dateTime))
        if (dateTime.zone != ZoneId.systemDefault()) {
            local_date_time.isEnabled = true
            local_date_time.setText(
                dateTime.toLocalDateTime().format(
                    DateTimeFormatter.ofPattern(
                        _localPattern
                    )
                )
            )
        } else {
            local_date_time.isEnabled = false
        }
    }

    var zoneId: ZoneId
        get() = dateTime.zone
        set(value) {
            dateTime = dateTime.withZoneSameLocal(value)
        }

    var dateTime: ZonedDateTime
        get() = _dateTime
        set(value) {
            _dateTime = value
            _dateTimeChangeListener?.onDateTimeChange(dateTime, this.id)
            updateViewDateTime()
        }

    private fun onClickListener(view: View) {
        if (!isEnabled)
            return
        val dpd = DatePickerDialog(
            this.context, R.style.CustomDatePickerDialog,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val tpd = TimePickerDialog(
                    this.context, R.style.CustomTimePickerDialog,
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        dateTime = ZonedDateTime.of(
                            LocalDateTime.of(year, month + 1, day, hour, minute),
                            dateTime.zone
                        )
                    }, dateTime.hour, dateTime.minute, false
                )
                tpd.show()
            }, dateTime.year, dateTime.monthValue - 1, dateTime.dayOfMonth
        )
        dpd.show()
    }

//    var enabled: Boolean
//    get() = _enabled
//    set(value) {
//        date_time_picker.setOnClickListener(this::onClickListener)
//        _enabled = value
//    }

    private lateinit var _dateTime: ZonedDateTime
    private var _pattern: String = "eee, dd LLL yyyy, hh:mm a"
    private var _localPattern: String = "eee, dd LLL yyyy, hh:mm a, O"

    private var _dateTimeChangeListener: OnDateTimeChangeListener? = null
//    private var _enabled: Boolean = true

    interface OnDateTimeChangeListener {
        //id -- of dateTimePickerView
        fun onDateTimeChange(zonedDateTime: ZonedDateTime, id: Int)
    }

    fun setOnDateTimeChangeListener(listener: (zonedDateTime: ZonedDateTime, id: Int) -> Unit) {
        _dateTimeChangeListener = object : OnDateTimeChangeListener {
            override fun onDateTimeChange(zonedDateTime: ZonedDateTime, id: Int) =
                listener(zonedDateTime, id)
        }
    }
}
