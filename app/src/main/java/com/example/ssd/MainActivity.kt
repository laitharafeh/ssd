package com.example.ssd

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore



class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var rvServices: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        // 1. Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // 2. Find RecyclerView & optional ProgressBar
        rvServices = findViewById(R.id.rvServices)
        progressBar = findViewById(R.id.progressBar)  // see note below

        // 3. Setup RecyclerView
        rvServices.layoutManager = LinearLayoutManager(this)
        rvServices.setHasFixedSize(true)

        // 4. Fetch data
        fetchServices()
    }

    private fun fetchServices() {
        // Show loading
        progressBar.visibility = View.VISIBLE

        db.collection("services")
            .get()
            .addOnSuccessListener { result ->
                // Map Firestore documents to Service objects
                val list = result.map { doc ->
                    doc.toObject(Service::class.java)
                }
                // Set adapter
                rvServices.adapter = ServiceAdapter(list)
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Error fetching services", exception)
                progressBar.visibility = View.GONE
            }
    }
}
