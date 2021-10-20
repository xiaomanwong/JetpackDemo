package com.example.myapplication.custom_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_curtain.*
import kotlinx.android.synthetic.main.fragment_curtain.view.*

/**
 * @author admin
 * @date 4/12/21
 * @Desc
 */
@FragmentDestination(pageUrl = "main/tabs/home/curtain")
class CurtainFragment : Fragment() {


    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_curtain, container, false)

        root.btn_open.setOnClickListener {
            curtain_view.openAnimator()
        }

        root.btn_close.setOnClickListener {
            curtain_view.closeAnimator()
        }

        root.btn_pause.setOnClickListener {
            curtain_view.stopAnimator()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        root.postDelayed({
            curtain_view.translationX = 100f
        }, 3000)
    }
}