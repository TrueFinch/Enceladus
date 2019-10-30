package com.truefinch.enceladus.ui.month

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.truefinch.enceladus.R

class MonthFragment : Fragment() {

    companion object {
        fun newInstance() = MonthFragment()
    }

    private lateinit var viewModel: MonthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(MonthViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_month, container, false)
        val textView: TextView = root.findViewById(R.id.text_month)
        viewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        Log.i("save_state","month fragment state restored")
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        Log.i("save_state","month fragment state saved")
//    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(MonthViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
