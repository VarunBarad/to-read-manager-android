package com.varunbarad.toreadmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.toreadmanager.databinding.ActivityAcceptUrlBinding
import com.varunbarad.toreadmanager.util.extractUrlIfAny
import com.varunbarad.toreadmanager.util.isUrl

class AcceptUrlActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityAcceptUrlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_accept_url)
        this.setTitle(R.string.app_name)

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
    }

    private fun clickListenerButton() {
        val url = this.dataBinding.editTextUrl.text.toString().trim()
        val title = this.dataBinding.editTextTitle.text.toString().trim()

        if (url.isUrl()) {
            // ToDo: Add to database here
            Toast.makeText(this, "Added to your list.", Toast.LENGTH_SHORT).show()
            this.setResult(Activity.RESULT_OK)
            this.finish()
        } else {
            this.showMessage("Please enter a valid URL.")
            this.dataBinding.editTextUrl.error = "Invalid URL"
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(this.dataBinding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
