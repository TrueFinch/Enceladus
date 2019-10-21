package com.example.enceladus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enceladus.inject.InjectApplication
import com.example.enceladus.screens.Screens
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : AppCompatActivity() {

    // lib for navigation between  fragments in activity (activity is only one) - realized by stack
    private var navigator = SupportAppNavigator(this, R.id.clMainContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InjectApplication.inject.router.newRootScreen(Screens.MonthScreen())
    }

    override fun onResume() {
        super.onResume()
        InjectApplication.inject.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        InjectApplication.inject.navigatorHolder.removeNavigator()
        super.onPause()
    }
      // to support back pressed button
//    override fun onBackPressed() {
//        supportFragmentManager.executePendingTransactions()
//        val fragment = supportFragmentManager.findFragmentById(R.id.clMainContainer) as? OnBackPressed
//
//        if (fragment != null) {
//            fragment.onBackPressed()
//        } else {
//            InjectApplication.inject.router.exit()
//        }
//    }
}
