package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.AnimatedImageDrawable
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.speech.SpeechRecognizer
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ImageSpan
import android.text.style.ReplacementSpan
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.lib_annotation.ActivityDestination
import com.example.myapplication.R
import com.example.myapplication.util.ImageUtil
import com.example.myapplication.widget.RoundedBackgroundSpan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


@SuppressLint("MissingPermission")
@ActivityDestination(pageUrl = "main/transform_audio", asStart = true)
class SpeechRecognizersActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var languageSpinner: Spinner
//    private lateinit var waveformView: WaveformView

    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    //    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var audioFilePath: String

    private val bitmapList = mutableListOf<Bitmap>()

    private val path =
        "/storage/emulated/0/Android/data/com.example.myapplication/files/22100901041601010000012_8/"

//    var audioRecorder: AudioRecorder = AudioRecorder()

    private val audioRecord: MediaRecorder by lazy {
        MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            val path = getExternalFilesDir(null)?.absolutePath + "/audio.3gp"
            println("targetFile: $path")
            setOutputFile(path)
        }
    }
    private val bufferSize: Int

    init {
        val sampleRate = 44100
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("LongLogTag", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_one_example)

        // Initialize TextView
        textView = findViewById(R.id.textViewWithIcon)


        lifecycleScope.launch(Dispatchers.IO) {
            val file = File("${this@SpeechRecognizersActivity.cacheDir}${File.separator}xxx.txt")
            if(file.exists()) {
                file.delete()
            }
            FileOutputStream(file).use {
                it.write("Hello, World!111".toByteArray())
                it.flush()
            }
        }

        // Set up the TextView
        textView.apply {
            // Create a SpannableStringBuilder for the text
            val ssb = SpannableStringBuilder("Echo了你：")

            // Create and add the image
            val micDrawable = ContextCompat.getDrawable(this@SpeechRecognizersActivity, R.drawable.play)
            micDrawable?.let {
                it.setBounds(0, 0, 60, 60)  // Adjust size as needed
                val imageSpan = ImageSpan(it, ImageSpan.ALIGN_CENTER)
                ssb.append(" ")
                ssb.setSpan(imageSpan, ssb.length - 1, ssb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            // Add the "哔哔哔哔" text
            ssb.append("哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔哔")

            // Create a background span for the image and "哔哔哔哔"
            val backgroundSpan = object : ReplacementSpan() {
                override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
                    return paint.measureText(text, start, end).toInt()
                }

                override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
                    val rect = RectF(x, top.toFloat(), x + paint.measureText(text, start, end), bottom.toFloat())
                    paint.color = ContextCompat.getColor(this@SpeechRecognizersActivity, R.color.colorAccent)
                    canvas.drawRoundRect(rect, 8f, 8f, paint)
                    paint.color = Color.BLACK
                    canvas.drawText(text, start, end, x, y.toFloat(), paint)
                }
            }
            ssb.setSpan(backgroundSpan, ssb.length - 32, ssb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Set the SpannableStringBuilder as the text
            text = ssb

            setTextColor(Color.BLACK)
            textSize = 18f
            gravity = Gravity.CENTER_VERTICAL

            // Add padding to the TextView
            setPadding(32, 16, 32, 16)
        }
//     
//        languageSpinner = findViewById(R.id.languageSpinner)
//        waveformView = findViewById(R.id.waveformView)

        // 检查权限
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.RECORD_AUDIO),
//                REQUEST_RECORD_AUDIO_PERMISSION
//            )
//        }
//
//        // 初始化 SpeechRecognizer
//        if (SpeechRecognizer.isRecognitionAvailable(this)) {
//            speechRecognizer =
//                SpeechRecognizer.createSpeechRecognizer(this)//, ComponentName(this, VoiceRecognitionService::class.java))
//        } else {
//            Toast.makeText(this, "设备不支持语音识别", Toast.LENGTH_SHORT).show()
//            return
//        }
////        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
////        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
////        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
////        sr.startListening(intent)
//        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//        speechRecognizerIntent.putExtra(
//            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//        )
//        speechRecognizerIntent.putExtra(
//            RecognizerIntent.EXTRA_CALLING_PACKAGE,
//            "com.example.myapplication.ui"
//        )
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
////        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_AUDIO_INJECT_SOURCE, Uri.parse(getExternalFilesDir(null)?.absolutePath + "/audio.3gp"))
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
//
//        // 设置监听器
//        speechRecognizer.setRecognitionListener(object : RecognitionListener {
//            override fun onReadyForSpeech(params: Bundle) {
////                audioRecord.startRecording()
//                Log.d("Quinn", ": $params");
////                textView.text = "请说话..."
//            }
//
//            override fun onBeginningOfSpeech() {
////                audioRecord.startRecording()
//            }
//
//            override fun onRmsChanged(rmsdB: Float) {
//                Log.d("SpeechRecognizersActivity", "语音强度：$rmsdB")
////                waveformView.updateAmplitude(rmsdB)
//            }
//
//            override fun onBufferReceived(buffer: ByteArray) {
//                Log.d("SpeechRecognizersActivity", "onBufferReceived: $buffer")
//            }
//
//            override fun onEndOfSpeech() {
//                textView.text = "停止说话"
//            }
//
//            override fun onError(error: Int) {
//                when (error) {
//                    SpeechRecognizer.ERROR_AUDIO -> textView.text = "音频错误"
//                    SpeechRecognizer.ERROR_CLIENT -> textView.text = "客户端错误"
//                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> textView.text = "权限不足"
//                    SpeechRecognizer.ERROR_NETWORK -> textView.text = "网络错误"
//                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> textView.text = "网络超时"
//                    SpeechRecognizer.ERROR_NO_MATCH -> textView.text = "无匹配结果"
//                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> textView.text = "识别器忙"
//                    SpeechRecognizer.ERROR_SERVER -> textView.text = "服务器错误"
//                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> textView.text = "无语音输入"
//                    else -> textView.text = "未知错误：$error"
//                }
//                if (SpeechRecognizer.ERROR_RECOGNIZER_BUSY == error) {
//                    speechRecognizer.cancel()
//                    speechRecognizer.startListening(speechRecognizerIntent)
//
//                }
//            }
//
//            override fun onResults(results: Bundle) {
//                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//                if (matches != null) {
//                    textView.text = matches[0]
//                }
//            }
//
//            @SuppressLint("LongLogTag", "SetTextI18n")
//            override fun onPartialResults(partialResults: Bundle) {
//                val matches =
//                    partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//                matches?.let {
//                    Log.d("SpeechRecognizersActivity", "onPartialResults: $it")
//                    it.forEach { result ->
//                        if (result.isNotEmpty()) {
//                            textView.text = "请说话...$result"
//                        }
//                    }
//                }
//            }
//
//            override fun onEvent(eventType: Int, params: Bundle) {}
//        })
//        button.setOnTouchListener { v, event ->
//            loadImage()


//                loadGifByAnimatedImageDrawable(
//                    findViewById(R.id.imageViewGif),
//                    App.instance.filesDir.path + "/gif/xxxx.gif.gif"
//                )
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
//                        == PackageManager.PERMISSION_GRANTED
//                    ) {
//                        speechRecognizer.startListening(speechRecognizerIntent)
//                        audioRecord.prepare()
//                        audioRecord.start()
//                    } else {
//                        ActivityCompat.requestPermissions(
//                            this,
//                            arrayOf(Manifest.permission.RECORD_AUDIO),
//                            REQUEST_RECORD_AUDIO_PERMISSION
//                        )
//                    }
//                }
//
//                MotionEvent.ACTION_UP -> {
//                    audioRecord.stop()
////                    audioRecord.release()
//                    speechRecognizer.stopListening()
//                }
//            }


//            true
//        }


        // 获取AudioManager
//        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

// 开始识别前静音
//        val originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
//        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0)
//        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0)
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
//        audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0)
//        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0)

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun loadImage() {
        lifecycleScope.launch(Dispatchers.IO) {
//            gifPath = GifMakeUtil.createGif(
//                "xxxx.gif",
//                File(path).listFiles()?.map { it.absolutePath } ?: emptyList(), 70,
//                68,
//                68
//            )

            File(path).listFiles()?.map { it.absolutePath }?.map {
                ImageUtil.resizeImage2(it, 68, 68)
            }?.let { bitmapList.addAll(it) }

            withContext(Dispatchers.Main) {
                loadAnimatedImageDrawable(findViewById(R.id.imageViewGif), bitmapList)
//                loadGifByAnimatedImageDrawable(
//                    findViewById(R.id.imageViewGif),
//                    App.instance.filesDir.path + "/gif/xxxx.gif.gif"
//                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun loadGifByAnimatedImageDrawable(showOffFrameView: ImageView, path: String) {
        ImageDecoder.createSource(
            showOffFrameView.context.contentResolver,
            Uri.parse("file://${path}")
        ).let {
            val drawable = ImageDecoder.decodeDrawable(it) as AnimatedImageDrawable
            drawable.repeatCount = 1
            showOffFrameView.setImageDrawable(drawable)
            drawable.start()
        }
    }

    private fun loadAnimatedImageDrawable(showOffFrameView: ImageView, bitmapList: List<Bitmap>?) {
        bitmapList?.forEachIndexed { index, bitmap ->
//            Thread.sleep(100)
//            showOffFrameView.setImageBitmap(bitmap)
            handler.sendMessageDelayed(Message.obtain().apply {
                obj = bitmap
            }, (100 * (index + 1)).toLong())
        }
    }

    private val handler: Handler = Handler(Looper.getMainLooper()) {
        findViewById<ImageView>(R.id.imageViewGif).setImageBitmap(it.obj as Bitmap)
        true
    }

}