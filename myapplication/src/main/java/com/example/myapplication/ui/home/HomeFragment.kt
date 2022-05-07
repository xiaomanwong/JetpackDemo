package com.example.myapplication.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.TransitionPendingActivity
import com.example.myapplication.transition.ChangeAlphaTransition
import com.example.myapplication.transition.ExitTransition
import com.example.myapplication.util.getDestination
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer


@FragmentDestination(pageUrl = "main/tabs/home", asStart = true)
class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val btnCurtain: Button = root.findViewById(R.id.btn_curtain)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result =
                context?.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
            Log.d(TAG, "checkSelfPermission: " + result)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result =
                activity?.shouldShowRequestPermissionRationale(android.Manifest.permission.READ_PHONE_STATE)
            Log.d(TAG, "shouldShowRequestPermissionRationale: " + result)
        }
        root.btn_permission.setOnClickListener {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE
                ), 1
            );
        }

        root.btn_auto_change_network.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/wifi/strength")?.id!!)
        }

        root.btn_immersive_activity.setOnClickListener {
//            startActivity(Intent(this.requireContext(), ImmericActivity::class.java))
            takeScreenShot()
        }

        btnCurtain.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/custom_page")?.id!!)
        }
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        root.btn3.setOnClickListener {

            val btn3: View = root.btn3

            // 退场动画
            val enterTransition = TransitionSet()
            val enterScale = ExitTransition(1, 20)
            val enterAlpha = ChangeAlphaTransition(1f, 0f)
            enterScale.addTarget(root.btn3)
            enterAlpha.addTarget(root.btn3)
            enterTransition.addTransition(enterScale)
            enterTransition.addTransition(enterAlpha)
            enterTransition.ordering = TransitionSet.ORDERING_TOGETHER
            enterTransition.duration = 300

            // 重新进入动画
            val reentrantTransition = TransitionSet()
            val reenterScale = ExitTransition(20, 1)
            val reenterAlpha = ChangeAlphaTransition(0f, 1f)
            reenterScale.addTarget(root.btn3)
            reenterAlpha.addTarget(root.btn3)
            reentrantTransition.addTransition(reenterScale)
            reentrantTransition.addTransition(reenterAlpha)
            reentrantTransition.ordering = TransitionSet.ORDERING_TOGETHER
            reentrantTransition.duration = 300

            requireActivity().window?.exitTransition = enterTransition
            requireActivity().window?.reenterTransition = reentrantTransition
            startActivity(
                Intent(activity, TransitionPendingActivity::class.java),
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity
                ).toBundle()
            )
//            startActivity()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn1.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/divide_store")?.id!!)
        }

        btn2.setOnClickListener {
            (requireActivity() as MainActivity).getNavController()
                .navigate(getDestination("main/tabs/ipv6")?.id!!)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(
            TAG,
            "onRequestPermissionsResult: " + permissions.contentToString() + "\n" + grantResults.contentToString()
        )
    }


    val EVENT_SCREENSHOT = 22 //截图事件

    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var image: Image? = null
    fun takeScreenShot() {
        mediaProjectionManager =
            activity?.application?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(
            mediaProjectionManager!!.createScreenCaptureIntent(),
            EVENT_SCREENSHOT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("whh0914", "onActivityResult...requestCode=$requestCode,resultCode=$resultCode")
        if (requestCode == EVENT_SCREENSHOT) {
            super.onActivityResult(requestCode, resultCode, data)
            Log.e("whh0914", "captureScreen...")
            val displayMetrics = DisplayMetrics()
            val windowManager =
                context?.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels
            Log.e("whh0914", "displayMetrics width=$width, height=$height")
            val mImageReader: ImageReader =
                ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
            mediaProjection = mediaProjectionManager!!.getMediaProjection(resultCode, data!!)
            val virtualDisplay = mediaProjection?.createVirtualDisplay(
                "screen-mirror",
                width,
                height,
                displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(),
                null,
                null
            )
            Handler().postDelayed(Runnable {
                try {
                    image = mImageReader.acquireLatestImage()
                    if (image != null) {
                        val planes: Array<Image.Plane> = image!!.getPlanes()
                        val buffer: ByteBuffer = planes[0].getBuffer()
                        val width: Int = image!!.getWidth()
                        val height: Int = image!!.getHeight()
                        Log.e("whh0914", "image width=$width, height=$height")
                        val pixelStride: Int = planes[0].getPixelStride()
                        val rowStride: Int = planes[0].getRowStride()
                        val rowPadding = rowStride - pixelStride * width
                        var bitmap = Bitmap.createBitmap(
                            width + rowPadding / pixelStride,
                            height,
                            Bitmap.Config.ARGB_8888
                        )
                        bitmap!!.copyPixelsFromBuffer(buffer)
                        bitmap =
                            Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
                        if (bitmap != null) {
                            Log.e("whh0914", "屏幕截图成功!")
                            saveBitmap(bitmap, "/sdcard/screenShot.png")
                        }
                        bitmap.recycle()
                    }
                } catch (e: Exception) {
                    Log.e("whh0914", "截图出现异常：$e")
                } finally {
                    image?.close()
                    if (mImageReader != null) {
                        mImageReader.close()
                    }
                    virtualDisplay?.release()
                    //必须代码，否则出现BufferQueueProducer: [ImageReader] dequeueBuffer: BufferQueue has been abandoned
                    mImageReader.setOnImageAvailableListener(null, null)
                    mediaProjection?.stop()
                }
            }, 100)
        }
    }

    //保存bitmap文件
    fun saveBitmap(bitmap: Bitmap, filePath: String?) {
        val f = File(filePath)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        try {
            out?.flush()
            out?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
