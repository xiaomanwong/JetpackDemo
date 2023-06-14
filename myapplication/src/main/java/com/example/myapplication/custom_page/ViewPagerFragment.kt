package com.example.myapplication.custom_page


import android.content.Context
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_view_pager.button_delete_txt
import kotlinx.android.synthetic.main.fragment_view_pager.tv_ssss
import kotlinx.android.synthetic.main.fragment_view_pager.tv_ssss2
import kotlinx.android.synthetic.main.fragment_view_pager.vp_paper_list
import java.io.File
import java.io.FileInputStream
import kotlin.math.*

/**
 * @author admin
 * @date 2021/10/20
 * @Desc
 */

@FragmentDestination(pageUrl = "main/tabs/home/view_pager", asStart = false)
class ViewPagerFragment : Fragment() {
    private val TAG = "ViewPagerFragment"

    companion object {
        fun newInstance(): ViewPagerFragment {
            val args = Bundle()
            val fragment = ViewPagerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_view_pager, container, false)

        return root
    }


    var listener: AzimuthSensorListener? = null
    private val runnable = object : Runnable {
        override fun run() {
//            tv_ssss.text = listener?.azimuth.toString()
//            tv_ssss.postDelayed(this, 1000)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_delete_txt.setOnClickListener {
            (vp_paper_list.adapter as PaperBallPageAdapter).papers?.removeAt(vp_paper_list.currentItem)
            vp_paper_list.adapter?.notifyDataSetChanged()
            val itemIndex = vp_paper_list.currentItem
            vp_paper_list.adapter=vp_paper_list.adapter
            vp_paper_list.setCurrentItem(itemIndex, true)
        }
        (context?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager)?.let {
            listener = AzimuthSensorListener(
                it
            ).apply {
                start()
            }
            tv_ssss.postDelayed(runnable, 1000)
        }

        (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
            tv_ssss2.text = ""

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.registerDefaultNetworkCallback(object :
                    ConnectivityManager.NetworkCallback(){
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        Log.d(TAG, "onAvailable: ${network.toString()}")
                    }

                    override fun onLosing(network: Network, maxMsToLive: Int) {
                        super.onLosing(network, maxMsToLive)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                    }

                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities
                    ) {
                        super.onCapabilitiesChanged(network, networkCapabilities)
                        Log.d(TAG, "onCapabilitiesChanged: $networkCapabilities")
                    }

                    override fun onLinkPropertiesChanged(
                        network: Network,
                        linkProperties: LinkProperties
                    ) {
                        super.onLinkPropertiesChanged(network, linkProperties)
                    }

                    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                        super.onBlockedStatusChanged(network, blocked)
                    }
                })
            }
        }

        vp_paper_list.adapter = PaperBallPageAdapter(vp_paper_list).apply {
            papers = mutableListOf(
                Paper(
                    "",
                    "/storage/emulated/0/Android/data/com.example.myapplication/files/imageWithTextTemp_1683959410968.jpg",
                    "8krpWJIYgn6T85",
                    "1",
                    "",
                    1123

                ),
                Paper(
                    "",
                    "/storage/emulated/0/Android/data/com.example.myapplication/files/imageWithTextTemp_1683877659547.jpg",
                    "8krpWJIYgn6T85",
                    "2",
                    "",
                    1123

                ),
                Paper(
                    "",
                    "/storage/emulated/0/Android/data/com.example.myapplication/files/imageWithTextTemp_1683959410968.jpg",
                    "8krpWJIYgn6T85",
                    "3",
                    "",
                    1123

                ),
                Paper(
                    "",
                    "/storage/emulated/0/Android/data/com.example.myapplication/files/imageWithTextTemp_1683877659547.jpg",
                    "8krpWJIYgn6T85",
                    "4",
                    "",
                    1123

                )
            )
        }

        vp_paper_list.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                Log.d("TAG", "onPageScrolled: position: $position")
            }

            override fun onPageSelected(position: Int) {
//                setPagerIndicator(position)
                Log.d("TAG", "onPageScrollStateChanged :position: $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.d("TAG", "onPageScrollStateChanged state:$state")

            }
        })
        vp_paper_list.setPageTransformer(true, TiltTransformer())
        vp_paper_list.offscreenPageLimit = 3

//        requireActivity().addBackPressed {
//            activity?.supportFragmentManager?.popBackStack()
//            true
//        }.also {
//            it.remove()
//        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    Log.d(TAG, "handleOnBackPressed: ")

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.remove(this@ViewPagerFragment)?.commit()
                }
            })

    }

}


class TiltTransformer : ViewPager.PageTransformer {
    private val mOffset = 60
    override fun transformPage(page: View, position: Float) {

        if (position <= 0) {
            page.rotation = 30 * position
            page.translationX = page.width * position
//            page.alpha = position * -1
        } else {
            //移动X轴坐标，使得卡片在同一坐标
            page.translationX = -position * page.width
            //缩放卡片并调整位置
            val scale: Float = (page.width - mOffset * position) / page.width
            page.scaleX = scale
            page.scaleY = scale
            //移动Y轴坐标
            page.translationY = -position * mOffset
        }
    }
}

class PaperBallPageAdapter(private val vp: ViewPager) : PagerAdapter() {
    var papers: MutableList<Paper>? = null

    var viewList: MutableList<View> = mutableListOf()
    override fun getCount(): Int {
        return papers?.count() ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val parent = ConstraintLayout(container.context)
        parent.layoutParams = ViewGroup.LayoutParams(-1, -1)
        val item = LayoutInflater.from(container.context)
            .inflate(R.layout.layout_single_paper_rview_container, parent)

        item.apply {
            val image = findViewById<ImageView>(R.id.one_paper)
            val imageBg = findViewById<ImageView>(R.id.one_paper_bg)
            val owner = findViewById<TextView>(R.id.paper_owner)
            val date = findViewById<TextView>(R.id.paper_publish_date)
            val paperItem = papers?.getOrNull(position)

//            val newBitmap = BitmapFactory.decodeResource(resources, R.drawable.die).let {
            val newBitmap =
                BitmapFactory.decodeStream(FileInputStream(paperItem?.image_url?.let { File(it) }))
                    .apply {

                        image.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
//                Bitmap.createBitmap(
//                    it,
//                    0,
//                    0,
//                    it.width,
//                    it.height,
//                    Matrix().apply {
//                        val scale = image.layoutParams.width / it.width.toFloat()
//                        postScale(scale, scale)
//
//                    },
//                    true
//                )

                    }
            image.setOnLongClickListener {
                papers?.removeAt(position)
                notifyDataSetChanged()

                if (position == 0) {
                    vp.setCurrentItem(1, true)
                    vp.setCurrentItem(0, true)
                } else {
                    vp.setCurrentItem(position - 1, true)
                }
                true
            }
            image.setImageBitmap(newBitmap)
//            val transY = (image.layoutParams.height - newBitmap.height) / 2f
//            image.updatePadding(top = transY.toInt())

            owner.text = paperItem?.username
            date.text = paperItem?.create_time.toString()
        }

        container.addView(parent)
        viewList.add(parent)
        return parent
    }
}

@Keep
data class Paper(
    val content_id: String,
    val image_url: String,
    val userid: String,
    val username: String,
    val bon_id: String,
    val create_time: Long
)

class AzimuthSensorListener(private val sensorManager: SensorManager) : SensorEventListener {

    var azimuth: Float = 0f
        get() {
            return field
        }

    private var accValues = FloatArray(3)
    private var magValues = FloatArray(3)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            accValues = event.values
        } else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magValues = event.values
        }

        calculateOrientation()
    }

    fun start() {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { accelerometer ->
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.let { magnetic ->
            sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }


    private fun calculateOrientation() {
        val values = FloatArray(3)
        val r = FloatArray(9)
        SensorManager.getRotationMatrix(r, null, accValues, magValues)
        SensorManager.getOrientation(r, values)
        values[0] = Math.toDegrees(values[0].toDouble()).toFloat()
        var orientation = values[0]
        if (orientation < 0) {
            orientation += 360
        }

        Log.d("xxxxxxxxxxx",orientation.toString())
        azimuth = orientation
    }

}