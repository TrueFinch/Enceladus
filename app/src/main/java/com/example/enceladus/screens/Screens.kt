package com.example.enceladus.screens

import androidx.fragment.app.Fragment
import com.example.enceladus.fragments.monthFragment.MonthFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class MonthScreen: SupportAppScreen() {
        override fun getFragment(): Fragment = MonthFragment.newInstance()
    }
}