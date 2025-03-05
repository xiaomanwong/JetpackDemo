package com.example.myapplication.custom_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import com.example.myapplication.widget.MaterialLoadingButton
import kotlinx.android.synthetic.main.activity_transition_pending.view.root
import kotlinx.android.synthetic.main.fragment_curtain.*
import kotlinx.android.synthetic.main.fragment_curtain.view.*
import kotlinx.android.synthetic.main.fragment_custom_page.view.btn_emoji_flower
import kotlinx.android.synthetic.main.fragment_emoji_flower.view.btnOpenGallery
import kotlinx.android.synthetic.main.fragment_emoji_flower.view.btnOpenGallery2
import kotlinx.android.synthetic.main.fragment_emoji_flower.view.emoji_view

/**
 * @author admin
 * @date 4/12/21
 * @Desc
 */
@FragmentDestination(pageUrl = "main/tabs/home/emoji_flower")
class EmojiFlowerFragment : Fragment() {


    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_emoji_flower, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btnOpenGallery.setButtonText("加载中")
        view.btnOpenGallery.setOnClickListener { btn ->
            view.emoji_view.setEmoji("🍺")
            view.emoji_view.startAnimation()
            view.btnOpenGallery.toggle()
            view.btnOpenGallery.postDelayed({
                view.btnOpenGallery.toggle()
            }, 5000)
        }

        view.btnOpenGallery2.setOnClickListener {
//            view.emoji_view.setEmoji("🍺")
            view.emoji_view.stopAnimation()
        }
    }

    override fun onResume() {
        super.onResume()
//        root.emoji_view.setEmoji("\uD83C\uDF7A")
    }
}