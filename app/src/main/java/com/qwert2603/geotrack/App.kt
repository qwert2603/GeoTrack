package com.qwert2603.geotrack

import android.app.Application
import android.content.Context
import android.util.Log

class App : Application() {
    companion object {
        lateinit var APP_CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        APP_CONTEXT = this
    }
}

fun logD(s: String) = Log.d("AASSDD", s)