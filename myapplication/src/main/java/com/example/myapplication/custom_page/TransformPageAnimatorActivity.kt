package com.example.myapplication.custom_page

import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import androidx.appcompat.app.AppCompatActivity
import com.example.lib_annotation.ActivityDestination
import com.example.myapplication.R
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

/**
 * @author Quinn
 * @date 2024/7/4
 * @Desc
 */
@ActivityDestination(pageUrl = "main/page/transform_page_animator")
class TransformPageAnimatorActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
//        window.requestFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS)
//        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        window.enterTransition = Explode()
        window.exitTransition = Explode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transform_page_animator)
        val container = findViewById<android.view.ViewGroup>(R.id.fragment_container)
//        container.transitionName = intent.getStringExtra("transition_name")

//        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
//            addTarget(container)
//            duration = 11300
//        }
//
//        window.sharedElementExitTransition = MaterialContainerTransform().apply {
//            addTarget(container)
//            duration = 1300
//        }
    }

}