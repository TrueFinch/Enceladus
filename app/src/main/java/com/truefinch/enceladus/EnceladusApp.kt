package com.truefinch.enceladus

import android.app.Application
import com.truefinch.enceladus.repository.server.Server


class EnceladusApp : Application() {

    companion object {
        lateinit var instance: EnceladusApp
    }

    public lateinit var server: Server
    override fun onCreate() {
        super.onCreate()
        server = Server()

        instance = this
    }
}