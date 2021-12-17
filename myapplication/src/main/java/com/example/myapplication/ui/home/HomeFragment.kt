package com.example.myapplication.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.ImmericActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.util.getDestination
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


@FragmentDestination(pageUrl = "main/tabs/home", asStart = true)
class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val btnCurtain: Button = root.findViewById(R.id.btn_curtain)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result =
                context?.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
            Log.d(TAG, "checkSelfPermission: " + result)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result =
                activity?.shouldShowRequestPermissionRationale(android.Manifest.permission.READ_PHONE_STATE)
            Log.d(TAG, "shouldShowRequestPermissionRationale: " + result)
        }
        root.btn_permission.setOnClickListener {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE
                ), 1
            );
        }

        root.btn_auto_change_network.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/wifi/strength")?.id!!)
        }

        root.btn_immersive_activity.setOnClickListener {
            startActivity(Intent(this.requireContext(), ImmericActivity::class.java))
        }

        btnCurtain.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/custom_page")?.id!!)
        }
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn1.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/divide_store")?.id!!)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(
            TAG,
            "onRequestPermissionsResult: " + permissions.contentToString() + "\n" + grantResults.contentToString()
        )
    }
}
