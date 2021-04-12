package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.util.getDestination
import kotlinx.android.synthetic.main.fragment_home.view.*


@FragmentDestination(pageUrl = "main/tabs/home", asStart = true)
class HomeFragment : Fragment() {

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

        root.btn_char_list.setOnClickListener { (requireActivity() as MainActivity).getNavController()
            .navigate(getDestination("main/tabs/home/char_list")?.id!!) }

        root.btn_direction.setOnClickListener { (requireActivity() as MainActivity).getNavController()
            .navigate(getDestination("main/tabs/home/direction")?.id!!) }

        root.btn_gesture_password.setOnClickListener { (requireActivity() as MainActivity).getNavController()
            .navigate(getDestination("main/tabs/home/gesture_password")?.id!!) }

        root.btn_loading.setOnClickListener { (requireActivity() as MainActivity).getNavController()
            .navigate(getDestination("main/tabs/home/loading")?.id!!) }

        root.btn_mask.setOnClickListener { (requireActivity() as MainActivity).getNavController()
            .navigate(getDestination("main/tabs/home/mask")?.id!!) }


        btnCurtain.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/home/curtain")?.id!!)
        }
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
