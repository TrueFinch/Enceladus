package com.truefinch.enceladus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.truefinch.enceladus.navigation.TabManager
import com.truefinch.enceladus.ui.auth.AuthViewModel
import com.truefinch.enceladus.ui.day.DayViewModel
import com.truefinch.enceladus.ui.eventFragment.EventViewModel
import com.truefinch.enceladus.ui.month.MonthViewModel
import com.truefinch.enceladus.ui.schedule.ScheduleViewModel
import com.truefinch.enceladus.ui.week.WeekViewModel
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var navView: BottomNavigationView

    private lateinit var tabManager: TabManager
    private lateinit var sharedViewModel: SharedViewModel

    private var firstTime: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EnceladusApp.instance.tabManager = TabManager(this)
        tabManager = EnceladusApp.instance.tabManager

        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(this)

//        sharedViewModel = this.run {
//            ViewModelProviders.of(this)[SharedViewModel::class.java]
//        }
//
//        EnceladusApp.instance.sharedViewModel = sharedViewModel

        if (savedInstanceState == null) {
//            navView.selectedItemId = R.id.navigation_start
            navView.visibility = View.GONE

            //maybe this should be done in some other way
            val authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
            authViewModel.check.observe(this, Observer<Boolean> {
                //                tabManager.switchTab(R.id.navigation_month, false)
                navView.visibility = View.VISIBLE
                navView.selectedItemId = R.id.navigation_month
            })
            tabManager.currentNavController = tabManager.navStartController
        }

//        val locale = Locale("en")
//        Locale.setDefault(locale)
//        val configuration = Configuration()
//        configuration.locale = locale
//        baseContext.resources.updateConfiguration(configuration, null)
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel = run { ViewModelProviders.of(this)[SharedViewModel::class.java] }
        sharedViewModel.monthViewModel =
            run { ViewModelProviders.of(this)[MonthViewModel::class.java] }
        sharedViewModel.weekViewModel =
            run { ViewModelProviders.of(this)[WeekViewModel::class.java] }
        sharedViewModel.eventViewModel =
            run { ViewModelProviders.of(this)[EventViewModel::class.java] }
        sharedViewModel.dayViewModel =
            run { ViewModelProviders.of(this)[DayViewModel::class.java] }
        sharedViewModel.scheduleViewModel =
            run { ViewModelProviders.of(this)[ScheduleViewModel::class.java] }
        sharedViewModel.init()
        EnceladusApp.instance.sharedViewModel = sharedViewModel
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tabManager.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        tabManager.onRestoreInstanceState(savedInstanceState)
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        tabManager.supportNavigateUpTo(upIntent)
    }

    override fun onBackPressed() {
        tabManager.onBackPressed()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (this.firstTime) {
            firstTime = false
            tabManager.switchTab(R.id.navigation_week, addToTabHistory = false, initial = true)
            tabManager.switchTab(R.id.navigation_day, addToTabHistory = false, initial = true)
            tabManager.switchTab(R.id.navigation_month, addToTabHistory = false, initial = true)
            tabManager.switchTab(R.id.navigation_event, addToTabHistory = false, initial = true)
            tabManager.switchTab(R.id.navigation_schedule, addToTabHistory = false, initial = true)


            Log.d(
                "DEBUG",
                "Thread: " + Thread.currentThread().id.toString() + ". mainActivity.onStart"
            )
            val month = ZonedDateTime.now()
            val monthStart = month.toLocalDate()
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay(month.zone)
            val monthEnd = month.toLocalDate()
                .with(TemporalAdjusters.lastDayOfMonth())
                .atTime(LocalTime.MAX)
                .atZone(month.zone)
            sharedViewModel.fetchEvents(monthStart.minusMonths(1), monthEnd.minusMonths(1))
            sharedViewModel.fetchEvents(monthStart, monthEnd)
            sharedViewModel.fetchEvents(monthStart.plusMonths(1), monthEnd.plusMonths(1))
        }
        tabManager.switchTab(menuItem.itemId)
        return true
    }

}