package com.varunbarad.toreadmanager.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module(
    includes = []
)
class AppModule(private val applicationContext: Application) {
    @Provides
    fun applicationContext(): Application {
        return applicationContext
    }
}
