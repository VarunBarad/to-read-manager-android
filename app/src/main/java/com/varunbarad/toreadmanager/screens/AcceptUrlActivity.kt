package com.varunbarad.toreadmanager.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.toreadmanager.databinding.ActivityAcceptUrlBinding
import com.varunbarad.toreadmanager.local_database.LinksDao
import com.varunbarad.toreadmanager.local_database.LinksDatabase
import com.varunbarad.toreadmanager.local_database.models.DbLink
import com.varunbarad.toreadmanager.screens.home.HomeActivity
import com.varunbarad.toreadmanager.util.ThreadSchedulers
import com.varunbarad.toreadmanager.util.extractUrlIfAny
import com.varunbarad.toreadmanager.util.isUrl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class AcceptUrlActivity : AppCompatActivity() {
    private val serviceDisposables = CompositeDisposable()

    lateinit var linksDao: LinksDao

    private lateinit var viewBinding: ActivityAcceptUrlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.injectDependencies()

        this.viewBinding = ActivityAcceptUrlBinding.inflate(layoutInflater)
        this.setContentView(this.viewBinding.root)

        this.setSupportActionBar(this.viewBinding.toolbar)

        val sharedText: String? = intent.getStringExtra(Intent.EXTRA_TEXT)
        val extraTitle: String? = intent.getStringExtra(Intent.EXTRA_TITLE)
        val extraSubject: String? = intent.getStringExtra(Intent.EXTRA_SUBJECT)
        val sharedTitle: String = if (extraTitle != null && extraTitle.isNotBlank()) {
            extraTitle
        } else if (extraSubject != null && extraSubject.isNotBlank()) {
            extraSubject
        } else {
            ""
        }

        val sharedUrl: String? = sharedText?.extractUrlIfAny()

        if (sharedUrl != null) {
            this.viewBinding.editTextUrl.setText(sharedUrl)
            this.viewBinding.editTextTitle.setText(sharedTitle)

            this.viewBinding.editTextTitle.requestFocus()
        } else {
            val referer = intent.getStringExtra(Intent.EXTRA_REFERRER)
            if (referer != HomeActivity::class.java.simpleName) {
                this.showMessage("No URL found in the shared text. Please check it or enter URL yourself.")

                this.viewBinding.editTextUrl.requestFocus()
            }
        }
    }

    private fun injectDependencies() {
        this.linksDao = LinksDatabase.getInstance(this).linksDao()
    }

    override fun onStart() {
        super.onStart()

        this.viewBinding.buttonAddToList.setOnClickListener {
            this.clickListenerButton()
        }
    }

    override fun onStop() {
        super.onStop()

        this.viewBinding.buttonAddToList.setOnClickListener(null)
        this.serviceDisposables.clear()
    }

    private fun clickListenerButton() {
        val url = this.viewBinding.editTextUrl.text.toString().trim()
        val title = this.viewBinding.editTextTitle.text.toString().trim()

        if (url.isUrl()) {
            this.serviceDisposables.add(
                this.linksDao
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
            this.viewBinding.textLayoutUrl.error = "Invalid URL"
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(this.viewBinding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
