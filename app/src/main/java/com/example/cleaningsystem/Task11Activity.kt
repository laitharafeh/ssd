package com.example.cleaningsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Task11Activity : AppCompatActivity() {
    lateinit var inputWorker: EditText
    lateinit var inputBuilding: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task11)

        val service = intent.getStringExtra("service")
        val task = intent.getStringExtra("task")

        findViewById<TextView>(R.id.textServiceTask).text = "Service: $service\nTask: $task"

        inputWorker = findViewById(R.id.inputWorkerDetails)
        inputBuilding = findViewById(R.id.inputBuilding)

        findViewById<Button>(R.id.btnScan).setOnClickListener {
            val intent = Intent(this, Task12Activity::class.java)
            intent.putExtra("worker", inputWorker.text.toString())
            intent.putExtra("building", inputBuilding.text.toString())
            startActivity(intent)
        }
    }
}