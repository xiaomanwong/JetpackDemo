package com.example.myapplication.ui.find

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import com.example.myapplication.media.TruelyAudioPlayerManager

@FragmentDestination(pageUrl = "main/tabs/find", asStart = false)
class FindFragment : Fragment() {

    private lateinit var findViewModel: FindViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        findViewModel =
            ViewModelProviders.of(this).get(FindViewModel::class.java)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.text_dashboard).setOnClickListener {
            TruelyAudioPlayerManager.getInstance().play(
                arrayListOf(
                    "http://m8.music.126.net/20220507141015/79a2f86e75e1e2f6e0fef03d00803c55/ymusic/b774/f84c/19cf/1fbd6b108563ccd03bf86b1a304d4570.mp3",
                    "http://m8.music.126.net/20220507141407/95f073b86b62c718e8f129bc7305e664/ymusic/545d/5252/5553/c749b329eae8d1f85449b9dbdfaf08b4.mp3",
                    "http://m8.music.126.net/20220507141400/a507daf44ab21647c6c38ead7ac29e06/ymusic/54ea/91ec/767e/20497bcc9ffc83e89184a6999258ae72.mp3",
                    "http://m8.music.126.net/20220507141357/9d52c3bff1d9be629723425a502aee23/ymusic/0575/dc5e/aa10/781ae4d95b9fea03e1267170b1cacd32.mp3",
                    "http://m8.music.126.net/20220507141355/98fb75197943ab26f6e8354c40cb5a92/ymusic/2d9d/fe2f/52e3/93c5c73bcf793249ea4c6aba718ca2f7.mp3"
                ),
                0,
                60_000,
                TruelyAudioPlayerManager.AudioMediaCategory.JUKEBOX
            ) { code, msg ->
                Log.d("wangxu3", "FindFragment ===> onViewCreated() called")
            }
        }

        view.findViewById<View>(R.id.text_dashboard_2).setOnClickListener {
            TruelyAudioPlayerManager.getInstance().play(
                arrayListOf(
                    "http://m8.music.126.net/20220507141015/79a2f86e75e1e2f6e0fef03d00803c55/ymusic/b774/f84c/19cf/1fbd6b108563ccd03bf86b1a304d4570.mp3",
                    "http://m8.music.126.net/20220507141407/95f073b86b62c718e8f129bc7305e664/ymusic/545d/5252/5553/c749b329eae8d1f85449b9dbdfaf08b4.mp3",
                    "http://m8.music.126.net/20220507141400/a507daf44ab21647c6c38ead7ac29e06/ymusic/54ea/91ec/767e/20497bcc9ffc83e89184a6999258ae72.mp3",
                    "http://m8.music.126.net/20220507141357/9d52c3bff1d9be629723425a502aee23/ymusic/0575/dc5e/aa10/781ae4d95b9fea03e1267170b1cacd32.mp3",
                    "http://m8.music.126.net/20220507141355/98fb75197943ab26f6e8354c40cb5a92/ymusic/2d9d/fe2f/52e3/93c5c73bcf793249ea4c6aba718ca2f7.mp3"
                ),
                3,
                0,
                TruelyAudioPlayerManager.AudioMediaCategory.GUITAR
            ) { code, msg ->
                Log.d("wangxu3", "FindFragment ===> onViewCreated() called")
            }
        }

        view.findViewById<View>(R.id.close_1).setOnClickListener {
            TruelyAudioPlayerManager.getInstance()
                .stop(TruelyAudioPlayerManager.AudioMediaCategory.JUKEBOX)
        }
        view.findViewById<View>(R.id.close_2).setOnClickListener {
            TruelyAudioPlayerManager.getInstance()
                .stop(TruelyAudioPlayerManager.AudioMediaCategory.GUITAR)
        }
    }
}
