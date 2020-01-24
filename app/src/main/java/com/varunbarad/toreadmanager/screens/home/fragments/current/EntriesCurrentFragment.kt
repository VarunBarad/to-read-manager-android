package com.varunbarad.toreadmanager.screens.home.fragments.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.toreadmanager.databinding.FragmentEntriesCurrentBinding
import com.varunbarad.toreadmanager.local_database.LinksDatabase
import com.varunbarad.toreadmanager.util.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class EntriesCurrentFragment : Fragment() {
    private val serviceDisposables = CompositeDisposable()
    private val toReadDatabase: LinksDatabase by lazy {
        Dependencies.getToReadDatabase(this.requireContext())
    }

    private lateinit var viewBinding: FragmentEntriesCurrentBinding

    private val toReadEntriesListAdapter: CurrentEntriesListAdapter =
        CurrentEntriesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.viewBinding = FragmentEntriesCurrentBinding.inflate(inflater, container, false)

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

        return this.viewBinding.root
    }

    override fun onStart() {
        super.onStart()

        this.serviceDisposables.add(
            this.toReadDatabase
                .linksDao()
                .getActiveEntries()
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
                    this.requireContext().openLinkInBrowser(entry.url)
                })
        )

        this.serviceDisposables.add(
            this.toReadEntriesListAdapter
                .getArchiveToReadEntryObservable()
                .subscribeBy(onNext = { entry ->
                    this.serviceDisposables.add(
                        this.toReadDatabase
                            .linksDao()
                            .updateEntry(entry.toDbLink().copy(archived = true))
                            .subscribeOn(ThreadSchedulers.io())
                            .observeOn(ThreadSchedulers.main())
                            .subscribeBy(onComplete = {
                                this.showMessage("Entry archived successfully")
                            })
                    )
                })
        )

        this.serviceDisposables.add(
            this.toReadEntriesListAdapter
                .getDeleteToReadEntryObservable()
                .subscribeBy(onNext = { entry ->
                    this.serviceDisposables.add(
                        this.toReadDatabase
                            .linksDao()
                            .deleteEntry(entry.toDbLink())
                            .subscribeOn(ThreadSchedulers.io())
                            .observeOn(ThreadSchedulers.main())
                            .subscribeBy(onComplete = {
                                this.showMessage("Entry deleted successfully")
                            })
                    )
                })
        )
    }

    override fun onStop() {
        super.onStop()

        this.serviceDisposables.clear()
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            this.viewBinding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
