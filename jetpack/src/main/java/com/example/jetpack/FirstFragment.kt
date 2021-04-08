package com.example.jetpack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_work_manager).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_WorkManagerFragment)
        }

        view.findViewById<Button>(R.id.divide_store).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        view.findViewById<Button>(R.id.button_five).setOnClickListener{
            findNavController().navigate(R.id.action_FirstFragment_to_AutoChangeNetworkFragment)
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}