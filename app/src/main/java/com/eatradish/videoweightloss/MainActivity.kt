package com.eatradish.videoweightloss

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private val stringBuild: StringBuilder = StringBuilder()

    private val resultText: TextView by lazy {
        findViewById<TextView>(R.id.text_result)
    }
    private val contentText: TextView by lazy {
        findViewById<TextView>(R.id.text_content)
    }
    private val button: Button by lazy {
        findViewById<Button>(R.id.button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultText.text = "点击压缩"
        stringBuild.append(contentText.text)
        button.setOnClickListener {
            val absolutePath = Environment.getExternalStoragePublicDirectory("").absolutePath
            Log.d("MainActivity","suihw absolutePath = $absolutePath")
            VideoWeightLoss.execute(
                arrayOf(
                    "ffmpeg",
                    "-i",
                    "$absolutePath/测试.mp4",
                    "-r",
                    "20",
                    "$absolutePath/测试1.mp4"
                ), object : VideoWeightLoss.OnHandleListener {
                    override fun onBegin() {
                        runOnUiThread {
                            application.toast("命令开始执行了!")
                            resultText.text = "开始执行"
                        }
                    }

                    override fun onMsg(msg: String?) {
                        runOnUiThread { application.toast("msg = $msg") }
                    }

                    override fun onProgress(position: Int, duration: Int) {
                        runOnUiThread {
                            stringBuild.append("\n").append("position = $position duration = $duration")
                            contentText.text = stringBuild.toString()
                        }
                    }

                    override fun onEnd(resultCode: Int, resultMsg: String?) {
                        runOnUiThread {
                            application.toast("命令执行结束了！code = $resultCode,msg = $resultMsg")
                            resultText.text = "执行结束"
                        }
                    }

                })
        }
    }

    fun Application.toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}