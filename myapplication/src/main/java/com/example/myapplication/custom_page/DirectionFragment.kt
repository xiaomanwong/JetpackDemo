package com.example.myapplication.custom_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R

/**
 * @author admin
 * @date 4/12/21
 * @Desc
 */
@FragmentDestination(pageUrl = "main/tabs/home/direction")
class DirectionFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_direction, container, false)
        return root
    }
}