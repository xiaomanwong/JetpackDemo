package com.example.jetpack

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileRequestFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DivideStoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        val request = FileRequestFactory.getRequest()
        request.updateFile(
            this.requireContext(),
            FileRequest(Environment.DIRECTORY_MOVIES, "hhaha.mp4")
        ) {

        }
    }
}