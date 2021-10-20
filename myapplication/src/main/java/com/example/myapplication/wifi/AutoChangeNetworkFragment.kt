package com.example.myapplication.wifi

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R

/**
 * @author admin
 * @date 4/7/21
 * @Desc
 */
@FragmentDestination(pageUrl = "main/wifi/strength", asStart = false)
class AutoChangeNetworkFragment : Fragment() {


    lateinit var tvWifiSignalStrength: TextView
    lateinit var tvCapabilitiesChanged: TextView
    lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.activity_auto_change_network, container, false)
        tvWifiSignalStrength = root.findViewById(R.id.tv_wifi_signal_strength)
        tvCapabilitiesChanged = root.findViewById(R.id.tv_capabilities_changed)
        webView = root.findViewById(R.id.webview)
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        return root
    }

    override fun onResume() {
        super.onResume()
        webView.loadUrl("https://www.baidu.com")
        WifiStrengthListener.registerWifiSignalStrengthListener(requireActivity(), object :
            WifiStrengthListener.WifiSignalStrengthMonitor {
            override fun onWifiSignalStrengthChange(signalStrength: Int) {
                tvWifiSignalStrength.text = "Wifi 信号强度 $signalStrength"
            }

            override fun onCapabilitiesChanged(toString: String) {
                requireActivity().runOnUiThread {
                    tvCapabilitiesChanged.text = "链接属性改变 $toString"
                }
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        WifiStrengthListener.unregisterWifiSignalStrengthListener(requireActivity())
    }
}