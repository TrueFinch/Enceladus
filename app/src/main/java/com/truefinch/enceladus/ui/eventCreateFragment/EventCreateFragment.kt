package com.truefinch.enceladus.ui.eventCreateFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.truefinch.enceladus.R
import kotlinx.android.synthetic.main.fragment_event.*
import java.time.ZonedDateTime

class EventCreateFragment : Fragment() {

    companion object {
        fun newInstance() = EventCreateFragment()
    }

    private lateinit var viewModel: EventCreateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EventCreateViewModel::class.java)
        startDateTimePicker.dateTime = ZonedDateTime.now()
        endDateTimePicker.dateTime = ZonedDateTime.now().plusHours(1)
        // TODO: Use the ViewModel
    }

}
