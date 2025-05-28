package com.example.ssd2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ServiceDetailsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_NAME = "service_name"
        const val EXTRA_DESCRIPTION = "service_description"
        const val EXTRA_BUILDING = "service_building"
        const val EXTRA_SERVICE_ID = "service_id"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Get data from intent
        val name = intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val building = intent.getStringExtra(EXTRA_BUILDING)
        val serviceId = intent.getStringExtra(EXTRA_SERVICE_ID)

        // Set up views
        findViewById<TextView>(R.id.detailServiceName).text = name
        findViewById<TextView>(R.id.detailServiceDescription).text = description
        findViewById<TextView>(R.id.detailServiceBuilding).text = "Location: $building"

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.tasksRecyclerView)
        progressBar = findViewById(R.id.taskProgressBar)
        
        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter()
        recyclerView.adapter = adapter

        // Load tasks for this service
        serviceId?.let { loadTasks(it) }
    }

    private fun loadTasks(serviceId: String) {
        progressBar.visibility = View.VISIBLE

        db.collection("tasks")
            .whereEqualTo("service_id", serviceId)
            .get()
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val tasksList = task.result?.documents?.map { document ->
                        document.toObject(Task::class.java)?.copy(id = document.id)
                    }?.filterNotNull() ?: emptyList()
                    adapter.updateTasks(tasksList)
                } else {
                    Toast.makeText(
                        this,
                        "Error loading tasks: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
} 