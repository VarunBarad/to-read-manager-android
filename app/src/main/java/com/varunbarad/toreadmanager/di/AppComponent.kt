package com.varunbarad.toreadmanager.di

import com.varunbarad.toreadmanager.ToReadManagerApplication
import com.varunbarad.toreadmanager.screens.home.fragments.current.EntriesCurrentFragment
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
    fun inject(target: EntriesCurrentFragment)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope
