package com.truefinch.enceladus

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.truefinch.enceladus.navigation.TabManager
import com.truefinch.enceladus.ui.auth.AuthViewModel
import java.util.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.format.Time


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val tabManager: TabManager by lazy { TabManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(this)
        supportActionBar?.hide()
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

//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.monthFragment,
//                R.id.weekFragment,
//                R.id.dayFragment,
//                R.id.scheduleFragment
//            )
//        )
//
//        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
//            if (nd.id == R.id.splashFragment || nd.id == R.id.authFragment) {
//                navView.visibility = View.GONE
//            } else {
//                navView.visibility = View.VISIBLE
//            }
//        }
//
//        supportActionBar?.hide()
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


//        // Create a new Locale object
//        val locale = Locale(Locale.ENGLISH.toLanguageTag())
//        Locale.setDefault(locale)
//        // Create a new configuration object
//        val config = Configuration()
//        // Set the locale of the new configuration
//
//        config.setLocale(locale)
//        this.createConfigurationContext(config)
        val locale = Locale("en")
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(configuration, null)
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