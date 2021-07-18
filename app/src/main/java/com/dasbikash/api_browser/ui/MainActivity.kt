package com.dasbikash.api_browser.ui

import android.os.Bundle
import android.support.annotation.IdRes
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import com.dasbikash.api_browser.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init(){
        setUpNavGraph()
        vhProgressHolder.setOnClickListener {  }
    }

    private fun setUpNavGraph() {
        with(bottomNavigationView) {
            setOnItemSelectedListener {
                onMenuItemSelected(it)
            }
        }
    }

    private fun getNavController() = findNavController(R.id.nav_host_fragment)

    private fun navigateTo(@IdRes resId:Int, args: Bundle?=null, navOptions: NavOptions?=null, navigatorExtras: Navigator.Extras?=null){
        lifecycleScope.launchWhenResumed { getNavController().navigate(resId, args, navOptions, navigatorExtras) }
    }

    override fun showLoadingScreen(){ vhProgressHolder.isVisible = true }
    override fun hideLoadingScreen(){ vhProgressHolder.isVisible = false }

    private fun onMenuItemSelected(it:MenuItem): Boolean {
        if(getNavController().currentDestination?.id == it.itemId) return true
        return when (it.itemId) {
            R.id.browseFragment -> {
                with(getNavController()) {
                    try {
                        while (popBackStack()) {
                        }
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    }
                    navigateTo(R.id.browseFragment)
                }
                true
            }

            R.id.bookmarksFragment -> {
                with(getNavController()) {
                    try {
                        while (popBackStack()) {
                        }
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    }
                    navigateTo(R.id.bookmarksFragment)
                }
                true
            }

            else -> false
        }
    }

    override fun onBackPressed() {
        if (vhProgressHolder.isVisible){
            vhProgressHolder.isVisible = false
            return
        }
        if (!getNavController().popBackStack()) {
            super.onBackPressed()
        }
    }
}