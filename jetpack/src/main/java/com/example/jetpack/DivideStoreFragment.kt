package com.example.jetpack

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileRequestFactory
import java.io.ByteArrayInputStream


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DivideStoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.textview_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        val iv = view.findViewById<ImageView>(R.id.iv)
        //图片存储

        view.findViewById<Button>(R.id.button_save_picture).setOnClickListener {
            Glide.with(this.requireActivity())
                .load("http://pic.5tu.cn/uploads/allimg/1506/210952576680.jpg")
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // 加载失败后的逻辑处理
                        Log.e("TAG", "onResourceReady: " )
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("TAG", "onLoadFailed: ")
                        return false
                    }
                })
                .into(iv)
//            val request = FileRequestFactory.getRequest()
//            val displayName = "${System.currentTimeMillis().toString().takeLast(8)}.jpg"
//            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.die)
//
//            //传入的source类型必须为InputSrteam
//            request.createFile(
//                this.requireContext(),
//                FileRequest(
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                        .toString(), displayName
//                )
////                 FileRequest(Environment.DIRECTORY_PICTURES, displayName)
//            ) { it ->
//                if (it.isSuccess) {
//                    Toast.makeText(
//                        this.requireContext(),
//                        "获取uri成功 uri = ${it.uri}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                it.uri?.also { uri ->
//                    request.updateFile(
//                        this.requireContext(), FileRequest(
//                            Environment.DIRECTORY_PICTURES,
//                            displayName,
//                            source = bitmap,
//                            uri = uri
//                        )
//                    ) {
//                        Toast.makeText(this.requireContext(), "保存图片成功}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
        }

        //图片查询
        view.findViewById<Button>(R.id.button_get_picture).setOnClickListener {
            val request = FileRequestFactory.getRequest()
            val displayName = "92606621.jpg"
            request.queryFile(
                this.requireContext(),
                FileRequest(Environment.DIRECTORY_PICTURES, displayName)
            ) {
                Toast.makeText(this.requireContext(), "查询到图片path${it.uri}}", Toast.LENGTH_SHORT).show()

            }
        }

        //图片删除
        view.findViewById<Button>(R.id.button_cancel_picture).setOnClickListener {
            val request = FileRequestFactory.getRequest()
            val displayName = "92606621.jpg"
            request.deleteFile(
                this.requireContext(),
                FileRequest(Environment.DIRECTORY_PICTURES, displayName)
            ) {
                if (it.isSuccess) {
                    Toast.makeText(this.requireContext(), "删除成功", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //txt 保存 到Document DownLoad
        view.findViewById<Button>(R.id.button_create_txt).setOnClickListener {
            val request = FileRequestFactory.getRequest()
            val displayName = "test.txt"
//            val source = resources.assets.open("test.txt")
            val str = "112314"
            val source = ByteArrayInputStream(str.toByteArray())
            request.createFile(
                this.requireContext(),
//                FileRequest(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
//                   , displayName, source = source)
                FileRequest(
                    this.requireContext().getExternalFilesDir("txt")!!.absolutePath,
                    displayName,
                    source = source
                )
//                FileRequest(Environment.DIRECTORY_DOCUMENTS, displayName, source = source)
            ) {
                if (it.isSuccess) {
                    Toast.makeText(this.requireContext(), "获取uri成功", Toast.LENGTH_SHORT).show()
                }
                it.file?.also { file ->
                    request.updateFile(
                        this.requireContext(),
                        FileRequest(
                            Environment.DIRECTORY_DOWNLOADS,
                            displayName,
                            file = file,
                            source = source
                        )
                    ) {
                        Toast.makeText(this.requireContext(), "创建成功", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //txt 查询
        view.findViewById<Button>(R.id.button_query_txt).setOnClickListener {
            val request = FileRequestFactory.getRequest()
            val displayName = "down/file_1612505786010.txt"
            request.queryFile(
                this.requireContext(),
                FileRequest(Environment.DIRECTORY_DOWNLOADS, displayName)
//                FileRequest(Environment.DIRECTORY_DOCUMENTS, displayName, source = source)
            ) {
                if (it.isSuccess) {
                    Toast.makeText(this.requireContext(), "查询成功${it.uri}", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
}