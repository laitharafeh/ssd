package com.example.cleaningsystem

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cleaningsystem.R
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

import com.journeyapps.barcodescanner.CaptureActivity
class CaptureActivityPortrait : CaptureActivity()

class Task12Activity : AppCompatActivity() {

    private lateinit var scanResultView: TextView
    private var worker: String? = null
    private var building: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task12)

        worker = intent.getStringExtra("worker")
        building = intent.getStringExtra("building")

        scanResultView = findViewById(R.id.scanResult)

        startQrScan(worker, building)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            scanResultView.text = "Worker: $worker\nBuilding: $building\n\nQR Result: ${result.contents}"
        } else {
            scanResultView.text = "Scan cancelled or failed."
        }
    }

    private fun startQrScan(worker: String?, building: String?) {
        val options = ScanOptions()
        options.setPrompt("Scan a QR code")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setCaptureActivity(CaptureActivityPortrait::class.java) // Optional for forced portrait

        barcodeLauncher.launch(options)
    }
}