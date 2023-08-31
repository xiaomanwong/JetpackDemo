package com.example.myapplication.ui.sofa

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.util.post
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


@FragmentDestination(pageUrl = "main/tabs/sofa", asStart = false)
class SofaFragment : Fragment() {

    private lateinit var sofaViewModel: SofaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sofaViewModel =
            ViewModelProviders.of(this).get(SofaViewModel::class.java)
        val root = inflater.inflate(
            com.example.myapplication.R.layout.fragment_notifications,
            container,
            false
        )
        val textView: TextView =
            root.findViewById(com.example.myapplication.R.id.text_notifications)

        textView.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "your_channel_id"
                val channelName: CharSequence = "Your Channel Name"
                val channelDescription = "Your Channel Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, channelName, importance)
                channel.description = channelDescription
                val notificationManager: NotificationManager? = context?.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager!!.createNotificationChannel(channel)
            }

            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(this.requireContext(), "your_channel_id")
                    .setSmallIcon(com.example.myapplication.R.mipmap.ic_launcher)
                    .setContentTitle("Notification Title")
                    .setContentText("Notification Content")
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true) // 当用户点击通知后，自动取消通知


            val notificationId = 1 // 通知的ID，可以用于后续更新或取消通知
            textView.postDelayed({

                val notificationManager = NotificationManagerCompat.from(this.requireContext())
                notificationManager.notify(notificationId, builder.build())
            }, 5000)

            //viewModel中调用，将Int转成String
            lifecycleScope.launchWhenCreated {
//               val simple =  simple()
//                    .flowOn(Dispatchers.Unconfined)
//                    .map { "response $it" }
////                    .collectLatest {
////                        sofaViewModel._text.value = it
////                        Log.d("", "response  $it")
////                        cancel()
////                    }
//
//
//                runBlocking {
//                    withTimeoutOrNull(2500){
//                        simple.collectLatest {
//                            sofaViewModel._text.value = it
//                            println(it)
//                        }
//                    }
//                }

//                field.collectLatest {
//                    if (it == 2) {
//                        cancel("怎么就取消了呢", CancellationException("强制取消"))
//                    }
//                    println(it)
//                }.runCatching {
//                    println("running catching $this")
//                }
//
//                val sum = (1..5).asFlow()
//                    .map { it * it } // 数字 1 至 5 的平方
//                    .reduce { a, b ->
//                        println("a=$a, b=$b")
//                        a + b
//                    } // 求和（末端操作符）
//                println(sum)

//                startActivity(Intent(requireContext(), IndexableListViewActivity::class.java))
//
//                println("초".toCharArray().contentToString())
//                fun requestFlow(i: Int): Flow<String> = flow {
//                    emit("$i: First")
//                    check(i > 2)
//                    delay(500) // 等待 500 毫秒
//                    emit("$i: Second")
//                }
//
//
//                callbackFlow<String> {
//
//                }
//                (1..3).asFlow()
//                    .onEach { delay(100) }
//                    .onEach { println(it) }
//                    .collect()

//                (1..3).asFlow() // 一个请求流
//                    .map { request ->
//                        println("start Request $request")
//                        performRequest(request)
//                    }
//                    .map {
//                        "Result: $it"
//                    }
////                    .flatMapConcat {
////                        trySend("result $it")
////                    }
//                    .collect { response ->
//
////                        val cat:TomCat = TomCat()
////                        println(cat.speak())
//
//                    }
            }

        }
        sofaViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }


    private suspend fun performRequest(request: Int): String {
        delay(1000) // 模仿长时间运行的异步工作
        return "response $request"
    }


    //创建一个流，每隔一秒发送一个Int数据
    private fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(1000)
            Log.d("", "Emitting $i")
            emit(i)
        }
    }

    private val field = (1..3).asFlow().catch {
        println("catch ${this.emit(5)}")
    }

    private val field2 = flowOf(5)

    /**
     * flow 操作符：
     * map: 数据转换，将一个数据，转换为另外一种格式
     * filter： 过滤，将符合条件的元素过滤掉
     * take: 限长
     * drop： 丢弃，与 take 正好相反
     * flowOn: 线程切换
     * onStart: 流开始执行前调用
     * onCompletion： 流执行完成后调用
     * catch: 异常操作符
     */
}