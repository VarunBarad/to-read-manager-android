package com.varunbarad.toreadmanager.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ThreadSchedulers {
    private var schedulers: RxSchedulers = RxSchedulers.Default

    fun enableTesting() {
        this.schedulers = RxSchedulers.Test
    }

    fun disableTesting() {
        this.schedulers = RxSchedulers.Default
    }

    fun computation() = this.schedulers.computation()
    fun io() = this.schedulers.io()
    fun main() = this.schedulers.main()

    private sealed class RxSchedulers {
        abstract fun computation(): Scheduler
        abstract fun io(): Scheduler
        abstract fun main(): Scheduler

        object Test : RxSchedulers() {
            override fun computation(): Scheduler = Schedulers.trampoline()
            override fun io(): Scheduler = Schedulers.trampoline()
            override fun main(): Scheduler = Schedulers.trampoline()
        }

        object Default : RxSchedulers() {
            override fun computation(): Scheduler = Schedulers.computation()
            override fun io(): Scheduler = Schedulers.io()
            override fun main(): Scheduler = AndroidSchedulers.mainThread()
        }
    }
}
