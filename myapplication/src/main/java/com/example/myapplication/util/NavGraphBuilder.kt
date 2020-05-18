package com.example.myapplication.util

import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import com.example.myapplication.FixFragmentNavigator

/**
 * @author wangxu
 * @date 20-5-12
 * @Description
 *
 */
object NavGraphBuilder {

    fun builder(activity: FragmentActivity, containerId: Int, navController: NavController) {
        val navigatorProvider = navController.navigatorProvider
        val activityNavigator: ActivityNavigator =
            navigatorProvider.getNavigator(ActivityNavigator::class.java)
//        val fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator::class.java)
        // 自定义的 fragment 导航器
        val fragmentNavigator =
            FixFragmentNavigator(activity, activity.supportFragmentManager, containerId)
        navigatorProvider.addNavigator(fragmentNavigator)
        val destMap = getDestConfig()

        val navGraph = NavGraph(NavGraphNavigator(navigatorProvider))

        for (value in destMap.values) {
            if (value.isFragment) {
                val fragmentDestination = fragmentNavigator.createDestination()
                fragmentDestination.className = value.className
                fragmentDestination.id = value.id
                fragmentDestination.addDeepLink(value.pageUrl)
                navGraph.addDestination(fragmentDestination)
            } else {
                val activityDestination = activityNavigator.createDestination()
                activityDestination.id = value.id
                activityDestination.addDeepLink(value.pageUrl)
                activityDestination.setComponentName(
                    ComponentName(
                        getApplication().packageName,
                        value.className
                    )
                )
                navGraph.addDestination(activityDestination)
            }

            if (value.asStart) {
                navGraph.startDestination = value.id
            }
        }

        navController.graph = navGraph

    }
}

