package com.atar.galeria.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.atar.galeria.ui.misc.NavigateEvent
import com.atar.galeria.ui.misc.SingleEvent

import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

    /**
     * Data
     */
    private lateinit var mNavController: NavController
    private lateinit var mViewModel: GalleryViewModel
    private val mNavigateObserver = Observer<SingleEvent<NavigateEvent>> {
        it.getContentIfNotHandled()?.let { navEvent ->
            mNavController.navigate(navEvent.id, navEvent.bundle, null, navEvent.extras)
        }
    }
    private val mNavigationListener = NavController.OnDestinationChangedListener { _, des, _ ->
        updateToolbar(des.id)
    }

    /**
     * AppCompatActivity Functions
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.atar.galeria.R.layout.activity_main)
        setSupportActionBar(am_toolbar)

        mViewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        mViewModel.getNavigateEvent().observe(this, mNavigateObserver)

        mNavController = findNavController(com.atar.galeria.R.id.am_nav)
        mNavController.addOnDestinationChangedListener(mNavigationListener)
    }

    override fun onResume() {
        updateToolbar(mNavController.currentDestination?.id)
        super.onResume()
    }

    override fun onDestroy() {
        mNavController.removeOnDestinationChangedListener(mNavigationListener)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * MainActivity Functions
     */
    private fun updateToolbar(navId: Int?) {
        when (navId) {
            com.atar.galeria.R.id.galleryFragment -> {
                supportActionBar?.title = getString(com.atar.galeria.R.string.app_name)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.setDisplayShowHomeEnabled(false)
            }
            com.atar.galeria.R.id.photoFragment -> {
                supportActionBar?.title = getString(com.atar.galeria.R.string.photo_label)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
                am_toolbar.menu.clear()
            }
        }
    }

}
