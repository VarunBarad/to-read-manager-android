package com.varunbarad.toreadmanager.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.toreadmanager.R
import com.varunbarad.toreadmanager.databinding.ActivityAcceptUrlBinding
import com.varunbarad.toreadmanager.local_database.models.DbLink
import com.varunbarad.toreadmanager.util.Dependencies
import com.varunbarad.toreadmanager.util.ThreadSchedulers
import com.varunbarad.toreadmanager.util.extractUrlIfAny
import com.varunbarad.toreadmanager.util.isUrl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class AcceptUrlActivity : AppCompatActivity() {
    private val serviceDisposables = CompositeDisposable()

    private lateinit var dataBinding: ActivityAcceptUrlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_accept_url
        )
        this.setSupportActionBar(this.dataBinding.toolbar)

        val sharedText: String? = intent.getStringExtra(Intent.EXTRA_TEXT)
        val sharedTitle: String? = intent.getStringExtra(Intent.EXTRA_TITLE)

        val sharedUrl: String? = sharedText?.extractUrlIfAny()

        if (sharedUrl != null) {
            this.dataBinding.editTextUrl.setText(sharedUrl)
            this.dataBinding.editTextTitle.setText(sharedTitle ?: "")

            this.dataBinding.editTextTitle.requestFocus()
        } else {
            this.showMessage("No URL found in the shared text. Please check it or enter URL yourself.")

            this.dataBinding.editTextUrl.requestFocus()
        }
    }

    override fun onStart() {
        super.onStart()

        this.dataBinding.buttonAddToList.setOnClickListener {
            this.clickListenerButton()
        }
    }

    override fun onStop() {
        super.onStop()

        this.dataBinding.buttonAddToList.setOnClickListener(null)
        this.serviceDisposables.clear()
    }

    private fun clickListenerButton() {
        val url = this.dataBinding.editTextUrl.text.toString().trim()
        val title = this.dataBinding.editTextTitle.text.toString().trim()

        if (url.isUrl()) {
            this.serviceDisposables.add(
                Dependencies.getToReadDatabase(this)
                    .linksDao()
                    .insertEntry(
                        DbLink(
                            url = url,
                            title = title,
                            archived = false
                        )
                    )
                    .subscribeOn(ThreadSchedulers.io())
                    .observeOn(ThreadSchedulers.main())
                    .subscribeBy(onComplete = {
                        Toast.makeText(
                            this,
                            "Added to your list.",
                            Toast.LENGTH_SHORT
                        ).show()

                        this.setResult(Activity.RESULT_OK)
                        this.finish()
                    })
            )
        } else {
            this.showMessage("Please enter a valid URL.")
            this.dataBinding.editTextUrl.error = "Invalid URL"
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(this.dataBinding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
