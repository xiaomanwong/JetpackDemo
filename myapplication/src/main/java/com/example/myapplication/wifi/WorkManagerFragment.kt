package com.example.myapplication.wifi

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.util.getDestination

/**
 * @author wangxu
 * @date 2021/1/29
 * @Description
 *
 */
class WorkManagerFragment : Fragment(R.layout.fragment_work_manager) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.single_task).setOnClickListener {
            val request = OneTimeWorkRequest.Builder(NewWork1::class.java).build()
            WorkManager.getInstance(this.requireContext()).enqueue(request)
        }

        view.findViewById<Button>(R.id.data_transform).setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/custom_page")?.id!!)
        }

        view.findViewById<Button>(R.id.multiply_task).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }


    }

}