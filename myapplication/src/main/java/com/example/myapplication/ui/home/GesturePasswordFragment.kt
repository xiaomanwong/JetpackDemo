package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import com.example.myapplication.widget.widget.password.GesturePasswordView
import kotlinx.android.synthetic.main.fragment_gesture_password.view.*

/**
 * @author admin
 * @date 4/12/21
 * @Desc
 */
@FragmentDestination(pageUrl = "main/tabs/home/gesture_password")
class GesturePasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gesture_password, container, false)

        root.gesture_password.setCallBack(callback = object : GesturePasswordView.CallBack {
            override fun onPasswordChanged(password: Int) {
                root.gesture_password_pre.updatePoint(password)
            }

            override fun onFinish(password: String) {
                Toast.makeText(requireContext(), password, Toast.LENGTH_SHORT).show()
                root.gesture_password.setResult(true)
                root.gesture_password_pre.resetView()
            }

            override fun onError() {

            }
        })

//        root.gesture_password.setResult()
        return root
    }
}