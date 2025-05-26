package com.example.cleaningsystem

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task10)
        startActivity(Intent(this, Task10Activity::class.java))
        finish()
    }
}