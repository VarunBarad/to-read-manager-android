package com.varunbarad.toreadmanager.screens.home.fragments.current

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varunbarad.toreadmanager.databinding.ListItemToReadEntryCurrentBinding
import com.varunbarad.toreadmanager.screens.models.UiToReadEntry
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CurrentEntriesListAdapter : ListAdapter<UiToReadEntry, ToReadEntryViewHolder>(
    UiToReadEntry.DIFF_CALLBACK
) {
    private val openToReadEntryClickSubject = PublishSubject.create<UiToReadEntry>()
    private val archiveToReadEntryClickSubject = PublishSubject.create<UiToReadEntry>()
    private val deleteToReadEntryClickSubject = PublishSubject.create<UiToReadEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToReadEntryViewHolder {
        return ToReadEntryViewHolder(
            viewBinding = ListItemToReadEntryCurrentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            openButtonClickListener = this::onOpenButtonClickListener,
            archiveButtonClickListener = this::onArchiveButtonClickListener,
            deleteButtonClickListener = this::onDeleteButtonClickListener
        )
    }

    override fun onBindViewHolder(holder: ToReadEntryViewHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

    private fun onOpenButtonClickListener(entry: UiToReadEntry) {
        this.openToReadEntryClickSubject.onNext(entry)
    }

    private fun onArchiveButtonClickListener(entry: UiToReadEntry) {
        this.archiveToReadEntryClickSubject.onNext(entry)
    }

    private fun onDeleteButtonClickListener(entry: UiToReadEntry) {
        this.deleteToReadEntryClickSubject.onNext(entry)
    }

    fun getOpenToReadEntryObservable(): Observable<UiToReadEntry> {
        return this.openToReadEntryClickSubject
    }

    fun getArchiveToReadEntryObservable(): Observable<UiToReadEntry> {
        return this.archiveToReadEntryClickSubject
    }

    fun getDeleteToReadEntryObservable(): Observable<UiToReadEntry> {
        return this.deleteToReadEntryClickSubject
    }
}

class ToReadEntryViewHolder(
    private val viewBinding: ListItemToReadEntryCurrentBinding,
    private val openButtonClickListener: (UiToReadEntry) -> Unit,
    private val archiveButtonClickListener: (UiToReadEntry) -> Unit,
    private val deleteButtonClickListener: (UiToReadEntry) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(toReadEntry: UiToReadEntry) {
        this.viewBinding.title.text = toReadEntry.title
        this.viewBinding.url.text = toReadEntry.url

        this.viewBinding.buttonOpen.setOnClickListener {
            this.openButtonClickListener(toReadEntry)
        }
        this.viewBinding.buttonArchive.setOnClickListener {
            this.archiveButtonClickListener(toReadEntry)
        }
        this.viewBinding.buttonDelete.setOnClickListener {
            this.deleteButtonClickListener(toReadEntry)
        }
    }
}
