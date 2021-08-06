package com.eatradish.videoweightloss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private val stringBuild : StringBuilder = StringBuilder()

    private val resultText : TextView by lazy {
        findViewById<TextView>(R.id.text_result)
    }
    private val contentText : TextView by lazy {
        findViewById<TextView>(R.id.text_content)
    }
    private val button : Button by lazy {
        findViewById<Button>(R.id.button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultText.text = "点击压缩"
        stringBuild.append(contentText.text)
        button.setOnClickListener {

        }
    }
}