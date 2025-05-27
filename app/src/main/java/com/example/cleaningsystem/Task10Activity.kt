package com.example.cleaningsystem

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Task10Activity : AppCompatActivity() {
    lateinit var serviceSpinner: Spinner
    lateinit var taskSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task10)

        serviceSpinner = findViewById(R.id.spinnerService)
        taskSpinner = findViewById(R.id.spinnerTask)

        val services = listOf("Cleaning", "Inspection", "Waste Collection")
        val tasks = listOf("Toilet", "Office", "Hall", "Stairs")

        serviceSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, services)
        taskSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tasks)

        findViewById<Button>(R.id.btnNextTo11).setOnClickListener {
            val selectedService = serviceSpinner.selectedItem.toString()
            val selectedTask = taskSpinner.selectedItem.toString()

            val intent = Intent(this, Task11Activity::class.java)
            intent.putExtra("service", selectedService)
            intent.putExtra("task", selectedTask)
            startActivity(intent)
        }
    }
}