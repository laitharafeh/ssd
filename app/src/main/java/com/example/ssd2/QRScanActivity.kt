package com.example.ssd2

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QRScanActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_TASK_TITLE = "task_title"
        const val EXTRA_TASK_ID = "task_id"
    }

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var qrIcon: ImageView
    private lateinit var scanAgainButton: Button
    private lateinit var rateButton: Button
    private lateinit var locationText: TextView
    private lateinit var buildingText: TextView
    private lateinit var floorText: TextView
    private lateinit var scanningLayout: LinearLayout
    private lateinit var resultsLayout: LinearLayout
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var isScanning = true

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)

        val taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE)
        findViewById<TextView>(R.id.taskTitleText).text = taskTitle

        // Initialize views
        previewView = findViewById(R.id.previewView)
        qrIcon = findViewById(R.id.qrIcon)
        scanAgainButton = findViewById(R.id.scanAgainButton)
        rateButton = findViewById(R.id.rateButton)
        locationText = findViewById(R.id.locationText)
        buildingText = findViewById(R.id.buildingText)
        floorText = findViewById(R.id.floorText)
        scanningLayout = findViewById(R.id.scanningLayout)
        resultsLayout = findViewById(R.id.resultsLayout)

        // Start scanning immediately
        startScanning()

        // Set up click listeners
        scanAgainButton.setOnClickListener {
            showScanningLayout()
            startScanning()
        }

        rateButton.setOnClickListener {
            // Handle rating functionality here
            Toast.makeText(this, "Rating submitted!", Toast.LENGTH_SHORT).show()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun showScanningLayout() {
        isScanning = true
        scanningLayout.visibility = View.VISIBLE
        resultsLayout.visibility = View.GONE
        qrIcon.visibility = View.VISIBLE
        previewView.visibility = View.GONE
    }

    private fun showResultsLayout() {
        scanningLayout.visibility = View.GONE
        resultsLayout.visibility = View.VISIBLE
        validateFields()
    }

    private fun validateFields() {
        val isLocationValid = locationText.text.toString().trim().isNotEmpty()
        val isBuildingValid = buildingText.text.toString().trim().isNotEmpty()
        val isFloorValid = floorText.text.toString().trim().isNotEmpty()

        rateButton.isEnabled = isLocationValid && isBuildingValid && isFloorValid
    }

    private fun startScanning() {
        checkCameraPermissionAndStart()
    }

    private fun checkCameraPermissionAndStart() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        // Show camera preview and hide the camera icon
        previewView.visibility = View.VISIBLE
        qrIcon.visibility = View.GONE

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        if (isScanning) {
                            processImageProxy(imageProxy)
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            try {
                cameraProvider?.unbindAll()

                camera = cameraProvider?.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalyzer
                )

                preview.setSurfaceProvider(previewView.surfaceProvider)

            } catch (exc: Exception) {
                Toast.makeText(this, "Failed to start camera", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun processImageProxy(imageProxy: ImageProxy) {
        val image = imageProxy.image
        if (image != null) {
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            
            val scanner = BarcodeScanning.getClient()
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        if (barcode.valueType == Barcode.TYPE_TEXT) {
                            barcode.rawValue?.let { value ->
                                handleScannedValue(value)
                            }
                        }
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun handleScannedValue(value: String) {
        if (!isScanning) return
        
        try {
            // Remove the curly braces and split by comma
            val cleanValue = value.trim().removeSurrounding("{", "}")
            val parts = cleanValue.split(",").map { it.trim() }
            
            if (parts.size == 3) {
                val location = parts[0]
                val building = parts[1]
                val floor = parts[2]

                runOnUiThread {
                    // Stop scanning first
                    isScanning = false
                    
                    // Update UI with scanned values
                    locationText.text = location
                    buildingText.text = building
                    floorText.text = floor

                    // Show the results layout
                    showResultsLayout()
                    
                    // Clean up camera resources
                    cameraProvider?.unbindAll()
                    
                    // Show success message
                    Toast.makeText(this, "QR Code scanned successfully!", Toast.LENGTH_SHORT).show()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Invalid QR Code format.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            runOnUiThread {
                Toast.makeText(this, "Invalid QR Code format.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
} 