package com.example.myapplication.custom_page

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File

/**
 * @author admin
 * @date 2023/5/17
 * @Desc
 */
@FragmentDestination(pageUrl = "main/tabs/share")
class ShareFragment : Fragment(), View.OnClickListener {


    val getPkgName = mapOf(
        "kakao" to "com.kakao.talk",
        "ins feed" to "com.instagram.android",
        "ins story" to "com.instagram.android",
        "tiktok" to "com.zhiliaoapp.musically",
        "tiktok" to "com.ss.android.ugc.trill",
        "line" to "jp.naver.line.android",
        "whatsapp" to "com.whatsapp",
        "messenger " to "com.facebook.orca",
        "viber" to "com.viber.voip",
        "twitter" to "com.twitter.android"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.ins_feed).setOnClickListener(this)
        view.findViewById<TextView>(R.id.ins_story).setOnClickListener(this)
        view.findViewById<TextView>(R.id.twitter).setOnClickListener(this)
        view.findViewById<TextView>(R.id.tiktok).setOnClickListener(this)
        view.findViewById<TextView>(R.id.line).setOnClickListener(this)
        view.findViewById<TextView>(R.id.viber).setOnClickListener(this)
        view.findViewById<TextView>(R.id.kakao).setOnClickListener(this)
        view.findViewById<TextView>(R.id.messenger).setOnClickListener(this)
        view.findViewById<TextView>(R.id.whatsapp).setOnClickListener(this)
        view.findViewById<TextView>(R.id.xxx).setOnClickListener {


            TestBottomSheetFragment().show(childFragmentManager, "")

        }
    }


    fun shareVideoAction(activity: Activity, platform: String, shareUrl: String) {
        try {

            val tempUrl = "/data/data/com.example.myapplication/files/PXL_20221124_090646934.mp4"
            val file = File(tempUrl)
            val contentUri = FileProvider.getUriForFile(
                activity.applicationContext,
                activity.packageName + ".fileprovider",
                file
            )


            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "video/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.setPackage(getPkgName[platform])
//            activity.startActivity(Intent.createChooser(shareIntent, ""))
            activity.startActivity(shareIntent)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("share", "failed to share ${getPkgName[platform]}")
        }
    }

    override fun onClick(v: View?) {
        this.activity?.let { shareVideoAction(it, (v as TextView).text.toString(), "") }
    }

}


class TestBottomSheetFragment : BottomSheetDialogFragment() {


    lateinit var editText: EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val container1 = LinearLayout(this.context)

        container1.layoutParams =
            LinearLayout.LayoutParams(-1, -1).apply { gravity = Gravity.CENTER }

        editText = EditText(this.context)



        container1.addView(editText, ViewGroup.LayoutParams(-1, -2))

        return container1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editText.postDelayed(
            {
                editText.requestFocus()
                ((context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                    editText, InputMethodManager.SHOW_IMPLICIT
                ))
            }, 100
        )
    }

//    override fun onPause() {
//        super.onPause()
//        editText.clearFocus()
//        ((context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
//            this.activity?.window?.decorView?.windowToken, 0
//        ))
//    }

    override fun onDestroy() {
        super.onDestroy()
//        editText.postDelayed({
//            editText.clearFocus()
//            ((context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
//                editText.windowToken,0
//            ))
//        }, 100)
    }

}