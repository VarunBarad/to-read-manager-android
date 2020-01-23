package com.varunbarad.toreadmanager.screens.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.varunbarad.toreadmanager.R
import com.varunbarad.toreadmanager.databinding.ActivityHomeBinding
import com.varunbarad.toreadmanager.screens.home.entries_adapter.EntriesListAdapter
import com.varunbarad.toreadmanager.util.Dependencies
import com.varunbarad.toreadmanager.util.ThreadSchedulers
import com.varunbarad.toreadmanager.util.toUiToReadEntry
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class HomeActivity : AppCompatActivity() {
    private val serviceDisposables = CompositeDisposable()

    private lateinit var viewBinding: ActivityHomeBinding

    private val toReadEntriesListAdapter: EntriesListAdapter = EntriesListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )

        val recyclerViewLayoutManager = LinearLayoutManager(
            this.viewBinding.recyclerViewToReadEntries.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        this.viewBinding.recyclerViewToReadEntries.layoutManager = recyclerViewLayoutManager
        this.viewBinding.recyclerViewToReadEntries.addItemDecoration(
            DividerItemDecoration(
                this.viewBinding.recyclerViewToReadEntries.context,
                recyclerViewLayoutManager.orientation
            )
        )
        this.viewBinding.recyclerViewToReadEntries.adapter = this.toReadEntriesListAdapter
    }

    override fun onStart() {
        super.onStart()

        this.serviceDisposables.add(
            Dependencies.getToReadDatabase(this)
                .linksDao()
                .getEntries()
                .map { entries -> entries.map { it.toUiToReadEntry() } }
                .subscribeOn(ThreadSchedulers.io())
                .observeOn(ThreadSchedulers.main())
                .subscribeBy(
                    onNext = { entries ->
                        this.toReadEntriesListAdapter.submitList(entries)
                    }
                )
        )

        this.serviceDisposables.add(
            this.toReadEntriesListAdapter
                .getOpenToReadEntryObservable()
                .subscribeBy(onNext = { entry ->
                    Toast.makeText(
                        this,
                        "Open -> ${entry.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )

        this.serviceDisposables.add(
            this.toReadEntriesListAdapter
                .getArchiveToReadEntryObservable()
                .subscribeBy(onNext = { entry ->
                    Toast.makeText(
                        this,
                        "Archive -> ${entry.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )

        this.serviceDisposables.add(
            this.toReadEntriesListAdapter
                .getDeleteToReadEntryObservable()
                .subscribeBy(onNext = { entry ->
                    Toast.makeText(
                        this,
                        "Delete -> ${entry.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )
    }

    override fun onStop() {
        super.onStop()

        this.serviceDisposables.clear()
    }
}
