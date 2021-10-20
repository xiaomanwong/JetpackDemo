package com.example.myapplication.wifi

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.WifiManager
import android.os.Build
import kotlin.math.abs

/**
 * @author wangxu
 * @date 4/7/21
 * @Desc
 */
object WifiStrengthListener {
    /**
     * 网络不可用
     */
    private const val NETWORK_STATE_UNAVAILABLE = -1

    /**
     * 网络可用
     */
    private const val NETWORK_STATE_AVAILABLE = 0

    /**
     * 网络可用，且是移动网络
     */
    private const val NETWORK_STATE_AVAILABLE_MOBILE = 1

    /**
     * 网络可用，且是 WiFi 网络
     */
    private const val NETWORK_STATE_AVAILABLE_WIFI = 2

    /** WiFi 信号载入因子，用来判断 WiFi 强度标准 */
    private const val WIFI_SIGNAL_LOAD_FACTOR = 40

    /** 用来记录 WiFi 信号强度，避免重复注册事件*/
    private var preWifiSignalStrength: Int = 0

    /** 网络变化监听，可用来通知前台*/
    private lateinit var wifiSignalStrengthMonitor: WifiSignalStrengthMonitor


    interface WifiSignalStrengthMonitor {
        fun onWifiSignalStrengthChange(signalStrength: Int)

        fun onCapabilitiesChanged(toString: String)
    }

    /**
     * 注册 WiFi 信号强度监听
     */
    fun registerWifiSignalStrengthListener(
        context: Context,
        signalStrengthMonitor: WifiSignalStrengthMonitor
    ) {
        wifiSignalStrengthMonitor = signalStrengthMonitor
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION)
        context.registerReceiver(wifiIntentReceiver, intentFilter)
    }

    /**
     * 取消注册广播事件
     */
    fun unregisterWifiSignalStrengthListener(context: Context) {
        preWifiSignalStrength = 0
        context.unregisterReceiver(wifiIntentReceiver)
    }

    /**
     * WiFi 信号强度广播接收器
     */
    private val wifiIntentReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val level = abs(wifiManager.connectionInfo.rssi)

            (context as Activity).runOnUiThread {
                wifiSignalStrengthMonitor.onWifiSignalStrengthChange(level)
            }
            transformNetworkCapabilities(context, level)
        }

    }

    /**
     * 转换网络能力
     * WiFi 强度弱时，切换移动网，
     * WiFi 强度强时，切换会 WiFi 网络
     */
    fun transformNetworkCapabilities(context: Context, level: Int) {
        preWifiSignalStrength = if (preWifiSignalStrength == 0) {
            level
        } else {
            if (preWifiSignalStrength > WIFI_SIGNAL_LOAD_FACTOR && level > WIFI_SIGNAL_LOAD_FACTOR) {
                return
            } else if (preWifiSignalStrength < WIFI_SIGNAL_LOAD_FACTOR && level < WIFI_SIGNAL_LOAD_FACTOR) {
                return
            } else {
                level
            }
        }

        val connectivityManager: ConnectivityManager? =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivityManager == null) {
            println("获取网络管理失败")
            return
        }

        // 检查移动网络是否可用
//        val state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.state
//        if (0 != state?.compareTo(NetworkInfo.State.CONNECTED) || state.compareTo(NetworkInfo.State.CONNECTING) != 0) {
//            println("移动网络未链接")
//            return
//        }
        val builder =
            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        if (level > WIFI_SIGNAL_LOAD_FACTOR) {
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        }
        val request = builder.build()
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                request.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } else {
                TODO("VERSION.SDK_INT < P")
            }
        ) {
            wifiSignalStrengthMonitor.onCapabilitiesChanged("WiFi")
            println("当前为 ${NetworkCapabilities.TRANSPORT_WIFI}")
        } else {
            wifiSignalStrengthMonitor.onCapabilitiesChanged("Mobile")
            println("当前为 ${NetworkCapabilities.TRANSPORT_CELLULAR}")
        }
        connectivityManager.requestNetwork(request, WifiChangeListener(connectivityManager))
    }

    class WifiChangeListener(var connectivityManager: ConnectivityManager) :
        ConnectivityManager.NetworkCallback() {

        /** 网络可用*/
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            println("网络可用 onAvailable $network")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.bindProcessToNetwork(network)
            }
        }

        /** 网络将要断开*/
        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            println("网络将要断开")
        }

        /** 网络断开*/
        override fun onLost(network: Network) {
            super.onLost(network)
            println("网络断开")
        }

        /** 网络不可用*/
        override fun onUnavailable() {
            super.onUnavailable()
            println("网络不可用")
        }

        /** 网络能力变化，还是可用状态，可能多次调用*/
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            println("网络能力变化，还是可用状态，可能多次调用")
            // 网络变化时，这个函数会回调多次
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    // WiFi 网络已链接
                    println("Wifi 网络已链接")
                } else {
                    // 移动网络已链接
                    println("Mobile 网络已链接")
                }
            }
        }

        /** 链接属性改变*/
        override fun onLinkPropertiesChanged(
            network: Network,
            linkProperties: LinkProperties
        ) {
            super.onLinkPropertiesChanged(network, linkProperties)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                println("链接属性改变$network , ${linkProperties.privateDnsServerName}")
            }
        }

        /** 网络暂停*/
        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            println("网络暂停")
        }
    }
}