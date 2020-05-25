package com.example.myapplication

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lib_network.GetRequest
import com.example.lib_network.JsonCallback
import com.example.myapplication.util.NavGraphBuilder
import com.example.myapplication.util.getDestConfig
import com.example.myapplication.widget.AppBottomBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject

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


}
