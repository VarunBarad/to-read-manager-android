package com.varunbarad.toreadmanager

import android.app.Application
import com.varunbarad.toreadmanager.di.AppComponent
import com.varunbarad.toreadmanager.di.AppModule
import com.varunbarad.toreadmanager.di.DaggerAppComponent

class ToReadManagerApplication : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = this.buildDaggerGraph()
        appComponent.inject(this)
    }

    private fun buildDaggerGraph(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
