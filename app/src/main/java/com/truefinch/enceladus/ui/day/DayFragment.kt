package com.truefinch.enceladus.ui.day

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.truefinch.enceladus.R


class DayFragment : Fragment() {

    companion object {
        fun newInstance() = DayFragment()
    }

    private lateinit var viewModel: DayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(DayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_day, container, false)
        val textView: TextView = root.findViewById(R.id.text_day)
        viewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(DayViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
