package com.example.customize.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customize.databinding.FragmentCurtainBinding

/**
 * @author wangxu
 * @date 2020/8/6
 * @Desc
 */
class CurtainFragment : Fragment() {

    fun newInstance(): CurtainFragment {
        return CurtainFragment()
    }


    lateinit var binding: FragmentCurtainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurtainBinding.inflate(inflater)
        return binding.root
    }

}