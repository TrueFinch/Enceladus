package com.truefinch.enceladus.ui.splash

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.truefinch.enceladus.R


class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(SplashViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_splash, container, false)
        val textView: TextView = root.findViewById(R.id.text_splash)
        viewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            context?.let {
                findNavController().navigate(R.id.action_splashFragment_to_authFragment)
            }
        }, 2500)

    }
}
