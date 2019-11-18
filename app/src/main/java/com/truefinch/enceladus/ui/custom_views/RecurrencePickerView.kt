package com.truefinch.enceladus.ui.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.sip.SipSession
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.truefinch.enceladus.R
import kotlinx.android.synthetic.main.recurrence_picker_view.view.*
import android.os.Bundle
import java.time.ZoneId
import java.time.ZonedDateTime


/**
 * TODO: document your custom view class.
 */
class RecurrencePickerView : LinearLayout {
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
        inflate(context, R.layout.recurrence_picker_view, this)
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.RecurrencePickerView, defStyle, 0
        )
        rRule = ""
        a.recycle()
    }

    var rRule: String
        get() = _rRule
        set(value) {
            _rRule = value
        }

    private lateinit var _rRule: String

    var dateTime: ZonedDateTime
        get() = _dateTime
        set(value) {
            _dateTime = value
        }

    private lateinit var _dateTime: ZonedDateTime


}
