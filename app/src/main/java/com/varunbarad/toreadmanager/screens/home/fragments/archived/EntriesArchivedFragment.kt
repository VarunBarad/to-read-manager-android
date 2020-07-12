package com.varunbarad.toreadmanager.screens.home.fragments.archived

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.toreadmanager.ToReadManagerApplication
import com.varunbarad.toreadmanager.databinding.FragmentEntriesArchivedBinding
import com.varunbarad.toreadmanager.local_database.LinksDao
import com.varunbarad.toreadmanager.util.ThreadSchedulers
import com.varunbarad.toreadmanager.util.openLinkInBrowser
import com.varunbarad.toreadmanager.util.toDbLink
import com.varunbarad.toreadmanager.util.toUiToReadEntry
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class EntriesArchivedFragment : Fragment() {
    companion object {
        const val FRAGMENT_TAG = "EntriesArchivedFragment"
    }

    private val serviceDisposables = CompositeDisposable()

    @Inject
    lateinit var linksDao: LinksDao

    private lateinit var viewBinding: FragmentEntriesArchivedBinding

    private val toReadEntriesListAdapter: ArchivedEntriesListAdapter =
        ArchivedEntriesListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ToReadManagerApplication.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.viewBinding = FragmentEntriesArchivedBinding.inflate(inflater, container, false)

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
            this.linksDao
                .getArchivedEntries()
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
                .getUnarchiveToReadEntryObservable()
                .subscribeBy(onNext = { entry ->
                    this.serviceDisposables.add(
                        this.linksDao
                            .updateEntry(entry.toDbLink().copy(archived = false))
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
                        this.linksDao
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
