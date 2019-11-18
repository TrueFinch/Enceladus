package com.truefinch.enceladus.ui.week

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alamkanak.weekview.DateTimeInterpreter
import com.truefinch.enceladus.R

import com.alamkanak.weekview.WeekView
import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.utils.lazyView
import java.util.*




class WeekFragment : Fragment() {

    companion object {
        fun newInstance() = WeekFragment()
    }

    private lateinit var viewModel: WeekViewModel

    private val weekView: WeekView<EventModel> by lazyView(R.id.weekView)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(WeekViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_week, container, false)

        //sample example
//        val textView: TextView = root.findViewById(R.id.text_week)
//        viewModel.text.observe(this, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        weekView.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretTime(hour: Int): String {
                return if (hour > 11) (hour - 12).toString() + " PM" else if (hour == 0) "12 M" else "$hour AM"
            }


            @SuppressLint("DefaultLocale")
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat("dd/MM", Locale.getDefault())

                return "%s\n%s".format(weekday.toUpperCase(), format.format(date.time))
            }
        }
    }

}
