package com.truefinch.enceladus.ui.week

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.truefinch.enceladus.R


class WeekFragment : Fragment() {

    companion object {
        fun newInstance() = WeekFragment()
    }

    private lateinit var viewModel: WeekViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(WeekViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_week, container, false)
        val textView: TextView = root.findViewById(R.id.text_week)
        viewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(WeekViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
