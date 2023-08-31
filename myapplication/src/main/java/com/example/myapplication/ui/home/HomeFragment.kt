package com.example.myapplication.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemModelListBinding
import com.example.myapplication.ui.ItemModel
import com.example.myapplication.util.getDestination
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

        val list = listOf<ItemModel>(
            ItemModel("自定义View", "main/tabs/custom_page"),
            ItemModel("权限请求", ""),
            ItemModel("网络切换", "main/wifi/strength"),
            ItemModel("沉浸式布局", "main/ImmericActivity"),
            ItemModel("分区存储", "main/tabs/divide_store"),
            ItemModel("IPv6", "main/tabs/ipv6"),
            ItemModel("分享", "main/tabs/share"),
        )
        root.findViewById<RecyclerView>(R.id.rv_list).adapter = HomeAdapter(list)
        return root
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


    inner class HomeAdapter(list: List<ItemModel>) : RecyclerView.Adapter<HomeAdapter.HomeRecyclerViewHolder>() {

        private val list = list

        inner class HomeRecyclerViewHolder(private val binding: ItemModelListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(itemModel: ItemModel) {
                binding.tvItemName.text = itemModel.itemName
                binding.root.setOnClickListener {
                    (requireActivity() as MainActivity).getNavController()
                        .navigate(getDestination(itemModel.itemPath)?.id!!)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
            return HomeRecyclerViewHolder(ItemModelListBinding.inflate(LayoutInflater.from(parent.context)))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
            holder.bind(list[position])

        }
    }

}
