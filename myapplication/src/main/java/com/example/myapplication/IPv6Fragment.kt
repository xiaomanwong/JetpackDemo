package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lib_annotation.FragmentDestination
import inet.ipaddr.HostName
import inet.ipaddr.IPAddress
import kotlinx.android.synthetic.main.fragment_ipv6.*
import java.net.NetworkInterface
import java.net.SocketException
import kotlin.concurrent.thread

/**
 * @author admin
 * @date 2022/1/18
 * @Desc
 */

@FragmentDestination(pageUrl = "main/tabs/ipv6", asStart = true)
class IPv6Fragment : Fragment() {

    var hostIp6: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ipv6, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_1.setOnClickListener {
            tv_desc.text = getLocalIpV6()
        }
        btn_2.setOnClickListener {
            tv_desc.text = getlocalIp()
        }
    }


    fun getLocalIpV6(): String? {
        thread {

            val hostName = "a1.go2yd.com"
//        val hostName = "1.2.3.4:8080"
//        val ipv6Address = IPAddressString()
            val host = HostName(hostName)
            host.validate()
            val toInetAddress = host.toAddress().toIPv6()
            tv_desc.postDelayed(
                {
                    tv_desc.text = "${toInetAddress.isIPv6} === ${toInetAddress.toString()}}"
                },
                300
            )
        }
        return null
//        val asInetSocketAddress = host.asInetSocketAddress()
//
//        val ipv6Address = asInetSocketAddress.address
//        return ipv6Address.hostAddress
//        thread {
//            try {
//                val en = NetworkInterface
//                    .getNetworkInterfaces()
//                while (en.hasMoreElements()) {
//                    val intf = en.nextElement()
//                    val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
//                    while (enumIpAddr.hasMoreElements()) {
//                        val inetAddress: InetAddress = enumIpAddr.nextElement()
//                        Log.e("ip1====1  ", "ip1       $inetAddress")
//                        Log.e("ip1====2  ", inetAddress.hostAddress)
//                        Log.e("ip1====3  ", "getHostName  " + inetAddress.hostName)
//                        Log.e(
//                            "ip1====4  ",
//                            "getCanonicalHostName  " + inetAddress.canonicalHostName
//                        )
//                        Log.e(
//                            "ip1====5  ",
//                            "getAddress  " + Arrays.toString(inetAddress.address)
//                        )
//                        Log.e("ip1====6  ", "getHostAddress  " + inetAddress.hostAddress)
//                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet6Address) {
//                            tv_desc.postDelayed({
//                                tv_desc.text = inetAddress.getHostAddress()
//                            }, 500)
//
//                            break
//                        }
//                    }
//                }
//            } catch (ex: Exception) {
//                Log.e("IP Address", ex.toString())
//            }
//        }

//        return null
    }


    fun getlocalIp(): String? {
        var ip: String
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && !inetAddress.isLinkLocalAddress) {
//                           ip=inetAddress.getHostAddress().toString();
                        println("ip==========" + inetAddress.hostAddress)
                        return inetAddress.hostAddress
                    }
                }
            }
        } catch (ignored: SocketException) {
        }
        return null
    }

    fun validateV6(): String? {
        Thread { hostIp6 = getLocalIpV6() }.start()

        //过滤找到真实的ipv6地址
        Log.e("ipv6", "v6 validateV6 $hostIp6")
        if (hostIp6 != null && hostIp6!!.contains("%")) {
            val split: List<String>? = hostIp6?.split("%")
            val s1 = split?.get(0)
            Log.e("ipv6", "v6 remove % is $s1")
            if (s1 != null && s1.contains(":")) {
                val split1 = s1.split(":").toTypedArray()
                if (split1.size == 6 || split1.size == 8) {
                    return if (split1[0].contains("fe") || split1[0].contains("fc")) {
                        "0.0.0.0"
                    } else {
                        s1
                    }
                }
            }
        }
        return "0.0.0.0"
    }

}