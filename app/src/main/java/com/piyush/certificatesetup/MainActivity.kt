package com.piyush.certificatesetup

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = TextView(this).apply {
            text = "Certificate Setup"
            textSize = 24f
            setPadding(32, 48, 32, 32)
        }

        setContentView(title)
    }
}