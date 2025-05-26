package com.example.cleaningsystem

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Task12Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task12)

        val worker = intent.getStringExtra("worker")
        val building = intent.getStringExtra("building")

        val simulatedScanData = "QR Code Scan Success: Worker ID XYZ123"
        val result = "Worker: $worker\nBuilding: $building\n\n$simulatedScanData"

        findViewById<TextView>(R.id.scanResult).text = result
    }
}