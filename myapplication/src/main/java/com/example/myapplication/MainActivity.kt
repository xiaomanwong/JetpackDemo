package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.util.NavGraphBuilder
import com.example.myapplication.util.getDestConfig
import com.example.myapplication.widget.AppBottomBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.nio.ByteBuffer


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var navView: AppBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
