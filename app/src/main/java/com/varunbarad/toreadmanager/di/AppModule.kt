package com.varunbarad.toreadmanager.di

import android.app.Application
import com.varunbarad.toreadmanager.export.ExportModule
import com.varunbarad.toreadmanager.local_database.DatabaseModule
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        DatabaseModule::class,
        ExportModule::class,
    ]
)
class AppModule(private val applicationContext: Application) {
    @Provides
    fun applicationContext(): Application {
        return applicationContext
    }
}
