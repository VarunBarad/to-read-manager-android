package com.varunbarad.toreadmanager.screens.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.varunbarad.toreadmanager.R
import com.varunbarad.toreadmanager.databinding.ActivityHomeBinding
import com.varunbarad.toreadmanager.util.Dependencies
import com.varunbarad.toreadmanager.util.ThreadSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class HomeActivity : AppCompatActivity() {
    private val serviceDisposables = CompositeDisposable()

    private lateinit var viewBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )
    }

    override fun onStart() {
        super.onStart()

        this.serviceDisposables.add(
            Dependencies.getToReadDatabase(this)
                .linksDao()
                .getEntries()
                .subscribeOn(ThreadSchedulers.io())
                .observeOn(ThreadSchedulers.main())
                .subscribeBy(
                    onNext = { entries ->
                        val message: String =
                            entries.joinToString(
                                separator = "\n",
                                transform = { "${it.title} -> ${it.url}" }
                            )

                        this.viewBinding.tempTextView.text = message
                    }
                )
        )
    }

    override fun onStop() {
        super.onStop()

        this.serviceDisposables.clear()
    }
}
