package com.varunbarad.toreadmanager.screens.home.entries_adapter

import androidx.recyclerview.widget.RecyclerView
import com.varunbarad.toreadmanager.databinding.ListItemToReadEntryBinding
import com.varunbarad.toreadmanager.screens.models.UiToReadEntry

class ToReadEntryViewHolder(
    private val viewBinding: ListItemToReadEntryBinding,
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

        this.viewBinding.executePendingBindings()
    }
}
