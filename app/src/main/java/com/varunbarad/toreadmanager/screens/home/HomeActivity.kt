package com.varunbarad.toreadmanager.screens.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.varunbarad.toreadmanager.R
import com.varunbarad.toreadmanager.databinding.ActivityHomeBinding
import com.varunbarad.toreadmanager.export.model.ExportLink
import com.varunbarad.toreadmanager.local_database.LinksDao
import com.varunbarad.toreadmanager.local_database.LinksDatabase
import com.varunbarad.toreadmanager.screens.AcceptUrlActivity
import com.varunbarad.toreadmanager.screens.home.fragments.archived.EntriesArchivedFragment
import com.varunbarad.toreadmanager.screens.home.fragments.current.EntriesCurrentFragment
import com.varunbarad.toreadmanager.util.toExportLink
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class HomeActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_FILE_CHOOSER = 1834
        private const val EXPORT_FILE_MIME_TYPE = "application/json"
    }

    private val serviceDisposables = CompositeDisposable()

    lateinit var linksDao: LinksDao

    lateinit var moshi: Moshi

    private lateinit var viewBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.injectDependencies()

        this.viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        this.setContentView(this.viewBinding.root)

        this.setSupportActionBar(this.viewBinding.toolbar)

        if (savedInstanceState == null) {
            val initialFragment = this.supportFragmentManager
                .findFragmentByTag(EntriesCurrentFragment.FRAGMENT_TAG)
                ?: EntriesCurrentFragment()

            this.supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.container_fragment,
                    initialFragment,
                    EntriesCurrentFragment.FRAGMENT_TAG
                ).commitAllowingStateLoss()
        }
    }

    private fun injectDependencies() {
        this.moshi = Moshi.Builder().build()

        this.linksDao = LinksDatabase.getInstance(this).linksDao()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_home, menu)
        return true
    }

    override fun onStart() {
        super.onStart()

        this.viewBinding
            .bottomNavigationView
            .setOnNavigationItemSelectedListener(this::onBottomNavigationItemSelected)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.button_export -> {
                this.openExportFileChooser(exportFileName(), EXPORT_FILE_MIME_TYPE)
                true
            }
            R.id.buttonAdd -> {
                this.openAddUrlScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()

        this.serviceDisposables.clear()
        this.viewBinding
            .bottomNavigationView
            .setOnNavigationItemSelectedListener(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_FILE_CHOOSER) {
            try {
                val result = when (resultCode) {
                    RESULT_OK -> {
                        val fileUri = data!!.data!!
                        val outputStream = contentResolver.openOutputStream(fileUri)!!
                        FileChooserResult.Success(outputStream)
                    }
                    RESULT_CANCELED -> FileChooserResult.Error
                    else -> FileChooserResult.Error
                }

                this.onExportDataFileChooserResult(result)
            } catch (e: Exception) {
                Log.e("ToReadManager", e.message, e)
                this.showMessage(getString(R.string.home_message_errorInExport))
            }
        }
    }

    private fun onBottomNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.category_current -> {
                val currentCategoryFragment = this.supportFragmentManager
                    .findFragmentByTag(EntriesCurrentFragment.FRAGMENT_TAG)
                    ?: EntriesCurrentFragment()

                this.supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container_fragment,
                        currentCategoryFragment,
                        EntriesCurrentFragment.FRAGMENT_TAG
                    ).commitAllowingStateLoss()

                return true
            }
            R.id.category_archvied -> {
                val archivedCategoryFragment = this.supportFragmentManager
                    .findFragmentByTag(EntriesArchivedFragment.FRAGMENT_TAG)
                    ?: EntriesArchivedFragment()

                this.supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container_fragment,
                        archivedCategoryFragment,
                        EntriesArchivedFragment.FRAGMENT_TAG
                    ).commitAllowingStateLoss()

                return true
            }
        }

        return false
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            this.viewBinding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun openAddUrlScreen() {
        val intent = Intent(this, AcceptUrlActivity::class.java).apply {
            putExtra(Intent.EXTRA_REFERRER, HomeActivity::class.java.simpleName)
        }
        startActivity(intent)
    }

    private fun openExportFileChooser(fileName: String, mimeType: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSER)
    }

    private fun onExportDataFileChooserResult(result: FileChooserResult) {
        when (result) {
            FileChooserResult.Error -> this.showMessage(getString(R.string.home_message_errorInExport))
            is FileChooserResult.Success -> {
                this.serviceDisposables.add(
                    this.linksDao
                        .getAllEntries()
                        .firstOrError()
                        .map { links -> links.map { it.toExportLink() } }
                        .subscribeBy { links ->
                            try {
                                result.fileOutputStream.use {
                                    val adapter = moshi.adapter<List<ExportLink>>(
                                        Types.newParameterizedType(
                                            List::class.java,
                                            ExportLink::class.java,
                                        )
                                    )

                                    it.write(
                                        adapter.toJson(links).toByteArray(
                                            charset = Charsets.UTF_8,
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("ToReadManager", e.message, e)
                                this.showMessage(getString(R.string.home_message_errorInExport))
                            }
                        }
                )
            }
        }
    }

    private fun exportFileName(): String {
        val date = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDateTime.now())
        return "to-read-manager-${date}.json"
    }
}
