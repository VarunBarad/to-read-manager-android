package com.varunbarad.toreadmanager

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class ToReadManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
    }
}
