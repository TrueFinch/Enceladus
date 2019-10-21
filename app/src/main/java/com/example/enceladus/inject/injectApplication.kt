package com.example.enceladus.inject

import android.app.Application
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

class InjectApplication : Application() {

    //companion object -- static analog in kt
    //this is instance of inject application
    companion object {
        lateinit var inject: InjectApplication
    }

    private lateinit var cicerone: Cicerone<Router>

    val navigatorHolder: NavigatorHolder
        get() = cicerone.navigatorHolder

    val router: Router
        get() = cicerone.router

    override fun onCreate() {
        super.onCreate()
        cicerone = Cicerone.create()
        inject = this
    }
}