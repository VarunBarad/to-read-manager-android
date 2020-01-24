package com.varunbarad.toreadmanager.screens.home.fragments.archived

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varunbarad.toreadmanager.databinding.ListItemToReadEntryArchivedBinding
import com.varunbarad.toreadmanager.screens.models.UiToReadEntry
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ArchivedEntriesListAdapter : ListAdapter<UiToReadEntry, ToReadEntryViewHolder>(
    UiToReadEntry.DIFF_CALLBACK
) {
    private val openToReadEntryClickSubject = PublishSubject.create<UiToReadEntry>()
    private val unarchiveToReadEntryClickSubject = PublishSubject.create<UiToReadEntry>()
    private val deleteToReadEntryClickSubject = PublishSubject.create<UiToReadEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToReadEntryViewHolder {
        return ToReadEntryViewHolder(
            viewBinding = ListItemToReadEntryArchivedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            openButtonClickListener = this::onOpenButtonClickListener,
            unarchiveButtonClickListener = this::onUnarchiveButtonClickListener,
            deleteButtonClickListener = this::onDeleteButtonClickListener
        )
    }

    override fun onBindViewHolder(holder: ToReadEntryViewHolder, position: Int) {
        holder.bind(this.getItem(position))
    }

    private fun onOpenButtonClickListener(entry: UiToReadEntry) {
        this.openToReadEntryClickSubject.onNext(entry)
    }

    private fun onUnarchiveButtonClickListener(entry: UiToReadEntry) {
        this.unarchiveToReadEntryClickSubject.onNext(entry)
    }

    private fun onDeleteButtonClickListener(entry: UiToReadEntry) {
        this.deleteToReadEntryClickSubject.onNext(entry)
    }

    fun getOpenToReadEntryObservable(): Observable<UiToReadEntry> {
        return this.openToReadEntryClickSubject
    }

    fun getUnarchiveToReadEntryObservable(): Observable<UiToReadEntry> {
        return this.unarchiveToReadEntryClickSubject
    }

    fun getDeleteToReadEntryObservable(): Observable<UiToReadEntry> {
        return this.deleteToReadEntryClickSubject
    }
}

class ToReadEntryViewHolder(
    private val viewBinding: ListItemToReadEntryArchivedBinding,
    private val openButtonClickListener: (UiToReadEntry) -> Unit,
    private val unarchiveButtonClickListener: (UiToReadEntry) -> Unit,
    private val deleteButtonClickListener: (UiToReadEntry) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(toReadEntry: UiToReadEntry) {
        this.viewBinding.title.text = toReadEntry.title
        this.viewBinding.url.text = toReadEntry.url

        this.viewBinding.buttonOpen.setOnClickListener {
            this.openButtonClickListener(toReadEntry)
        }
        this.viewBinding.buttonUnarchive.setOnClickListener {
            this.unarchiveButtonClickListener(toReadEntry)
        }
        this.viewBinding.buttonDelete.setOnClickListener {
            this.deleteButtonClickListener(toReadEntry)
        }

        this.viewBinding.executePendingBindings()
    }
}
