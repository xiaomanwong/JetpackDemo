package com.example.myapplication

import android.os.Bundle
import android.text.TextUtils
import android.transition.Fade
import android.transition.Slide
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.util.NavGraphBuilder
import com.example.myapplication.util.getDestConfig
import com.example.myapplication.widget.AppBottomBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var navView: AppBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window?.sharedElementsUseOverlay = false
        setContentView(R.layout.activity_main)
        window?.enterTransition = Fade()
        window?.exitTransition = Fade()
        window?.reenterTransition = Fade()
        window?.returnTransition = Fade()
        navView = findViewById(R.id.nav_view)
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = NavHostFragment.findNavController(fragment!!)
        NavGraphBuilder.builder(this, fragment.id, navController)
        navView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val destConfig = getDestConfig()
        val iterator = destConfig.iterator()

        while (iterator.hasNext()) {
            val destination = iterator.next()
            val value = destination.value

        }
        navController.navigate(item.itemId)
        return !TextUtils.isEmpty(item.title)

    }


    fun getNavController(): NavController {
        return navController
    }

}
