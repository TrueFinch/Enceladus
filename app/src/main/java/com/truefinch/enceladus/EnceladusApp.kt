package com.truefinch.enceladus

import android.app.Application
import com.truefinch.enceladus.navigation.TabManager
import com.truefinch.enceladus.repository.server.EventRepository
import com.truefinch.enceladus.repository.server.Server


class EnceladusApp : Application() {

    companion object {
        lateinit var instance: EnceladusApp
    }


    lateinit var tabManager: TabManager
    lateinit var server: Server
    lateinit var repository: EventRepository
    lateinit var sharedViewModel: SharedViewModel


    override fun onCreate() {
        super.onCreate()
        server = Server()
        repository = EventRepository(server.api)
        instance = this
    }
}