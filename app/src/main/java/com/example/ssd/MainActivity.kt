package com.example.ssd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ssd.R
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)    // initialize Firebase
        setContentView(R.layout.activity_main)
    }
}
