package com.example.myapplication.custom_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.util.getDestination
import kotlinx.android.synthetic.main.fragment_custom_page.view.*

/**
 * @author admin
 * @date 2021/10/20
 * @Desc
 */

@FragmentDestination(pageUrl = "main/tabs/custom_page", asStart = false)
class CustomFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_custom_page, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val btnCurtain: Button = root.findViewById(R.id.btn_curtain)

        root.btn_char_list.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/home/char_list")?.id!!)
        }

        root.btn_direction.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/home/direction")?.id!!)
        }

        root.btn_gesture_password.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/home/gesture_password")?.id!!)
        }

        root.btn_loading.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/home/loading")?.id!!)
        }

        root.btn_mask.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/home/mask")?.id!!)
        }

        btnCurtain.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/home/curtain")?.id!!)
        }

        return root
    }

}