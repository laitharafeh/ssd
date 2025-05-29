package com.example.ssd2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Start ServicesActivity immediately
        startActivity(Intent(this, ServicesActivity::class.java))
        finish() // Close MainActivity
    }
} 