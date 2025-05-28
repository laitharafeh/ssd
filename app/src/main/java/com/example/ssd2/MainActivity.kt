package com.example.ssd2

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create a vertical LinearLayout
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
        
        // Create and add Hello World TextView
        val textView = TextView(this).apply {
            text = "Hello World"
            textSize = 24f
        }
        layout.addView(textView)
        
        // Create and add Services button
        val servicesButton = Button(this).apply {
            text = "Go to Services"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, ServicesActivity::class.java))
            }
        }
        layout.addView(servicesButton)
        
        // Set the layout as content view
        setContentView(layout)
    }
} 