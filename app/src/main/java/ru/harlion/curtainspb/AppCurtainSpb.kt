package ru.harlion.curtainspb

import android.app.Application
import android.content.Context

class AppCurtainSpb : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
    }
}