package com.varunbarad.toreadmanager.screens.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.varunbarad.toreadmanager.R
import com.varunbarad.toreadmanager.databinding.ActivityHomeBinding
import com.varunbarad.toreadmanager.screens.home.fragments.archived.EntriesArchivedFragment
import com.varunbarad.toreadmanager.screens.home.fragments.current.EntriesCurrentFragment
import io.reactivex.disposables.CompositeDisposable

class HomeActivity : AppCompatActivity() {
    private val serviceDisposables = CompositeDisposable()

    private lateinit var viewBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onStart() {
        super.onStart()

        this.viewBinding
            .bottomNavigationView
            .setOnNavigationItemSelectedListener(this::onBottomNavigationItemSelected)
    }

    override fun onStop() {
        super.onStop()

        this.serviceDisposables.clear()
        this.viewBinding
            .bottomNavigationView
            .setOnNavigationItemSelectedListener(null)
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
}
