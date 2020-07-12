package com.varunbarad.toreadmanager.di

import com.varunbarad.toreadmanager.ToReadManagerApplication
import com.varunbarad.toreadmanager.screens.home.fragments.archived.EntriesArchivedFragment
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
    fun inject(target: EntriesArchivedFragment)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope
