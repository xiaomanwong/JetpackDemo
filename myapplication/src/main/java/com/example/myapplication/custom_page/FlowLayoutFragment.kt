package com.example.myapplication.custom_page


import android.content.Context
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.generateViewId
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_view_pager.button_delete_txt
import kotlinx.android.synthetic.main.fragment_view_pager.tv_ssss
import kotlinx.android.synthetic.main.fragment_view_pager.tv_ssss2
import kotlinx.android.synthetic.main.fragment_view_pager.vp_paper_list
import java.io.File
import java.io.FileInputStream
import kotlin.math.*
import kotlin.random.Random

/**
 * @author admin
 * @date 2021/10/20
 * @Desc
 */

@FragmentDestination(pageUrl = "main/tabs/home/flow_layout", asStart = false)
class FlowLayoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_flow_layout, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val flowLayout = view.findViewById<Flow>(R.id.flow)

        for (i in 0..10) {
            val textView = TextView(context)
            textView.text = "item $i"
            textView.id = generateViewId()
            textView.setTextColor(resources.getColor(R.color.black))
            textView.layoutParams = ConstraintLayout.LayoutParams(
                150 + 100 * (0.5 + Math.random()).toInt(),
               100
            )
            textView.maxLines = 1
            textView.ellipsize = TextUtils.TruncateAt.END
            flowLayout.referencedIds += textView.id
            (view as ViewGroup).addView(textView)
        }
    }


}