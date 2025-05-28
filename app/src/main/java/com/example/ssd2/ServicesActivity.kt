package com.example.ssd2

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class ServicesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServiceAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Initialize views
        recyclerView = findViewById(R.id.servicesRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ServiceAdapter(emptyList())
        recyclerView.adapter = adapter

        // Load services
        loadServices()
    }

    private fun loadServices() {
        progressBar.visibility = View.VISIBLE

        db.collection("services")
            .get()
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val servicesList = task.result?.documents?.map { document ->
                        document.toObject(Service::class.java)?.copy(id = document.id)
                    }?.filterNotNull() ?: emptyList()
                    adapter.updateServices(servicesList)
                } else {
                    Toast.makeText(
                        this,
                        "Error loading services: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
} 