package com.varunbarad.toreadmanager.di

import com.varunbarad.toreadmanager.ToReadManagerApplication
import dagger.Component
import javax.inject.Scope

@AppScope
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {
    fun inject(target: ToReadManagerApplication)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope
