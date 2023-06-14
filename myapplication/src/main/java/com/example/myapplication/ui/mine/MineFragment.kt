package com.example.myapplication.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNotificationsBinding

@FragmentDestination(pageUrl = "main/tabs/my", asStart = false)
class MineFragment : Fragment() {

    private lateinit var mineViewModel: MineViewModel

    private val binding by lazy {
        DataBindingUtil.inflate<FragmentNotificationsBinding>(
            layoutInflater,
            R.layout.fragment_notifications,null, false
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mineViewModel =
            ViewModelProviders.of(this).get(MineViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mineViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textNotifications.text = it
        })

        binding.textCalendar.setOnClickListener {

        }

//        binding.calendarView.apply {
//         this.weekDayTextAppearance = R.style.Widget_MaterialComponents_MaterialCalendar_Day_Today
//        }
    }
}
