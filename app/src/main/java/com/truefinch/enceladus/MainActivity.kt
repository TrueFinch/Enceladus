package com.truefinch.enceladus

import android.content.Intent
import android.content.res.Configuration
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
import java.util.*
import com.truefinch.enceladus.repository.server.model.EventRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val tabManager: TabManager by lazy { TabManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(this)
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

//        EnceladusApp.instance.server.api.createEvent(EventRequest("server", null, "eventTitle", "mem"))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe ({
//                Log.d("debug","Succees")
//            }, {
//                Log.d("debug", it.toString())
//            })

//        val locale = Locale("en")
//        Locale.setDefault(locale)
//        val configuration = Configuration()
//        configuration.locale = locale
//        baseContext.resources.updateConfiguration(configuration, null)
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
        tabManager.switchTab(menuItem.itemId)
        return true
    }

}