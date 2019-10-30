package com.truefinch.enceladus.ui.auth

import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.truefinch.enceladus.MainActivity
import com.truefinch.enceladus.R


class AuthFragment : Fragment() {

    companion object {
        fun newInstance() = AuthFragment()
    }

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            activity?.run { ViewModelProviders.of(this).get(AuthViewModel::class.java) }!!
        val root = inflater.inflate(R.layout.fragment_auth, container, false)
        val textView: TextView = root.findViewById(R.id.text_auth)
        viewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            context?.let {
                viewModel.setCheck(true)
                val v = viewModel.check.value
            }
        }, 0)

    }
}
