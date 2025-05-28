package com.example.ssd2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var tasks: List<Task> = emptyList()) : 
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_card_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.nameTextView.text = task.title
        holder.descriptionTextView.text = task.description
        holder.buildingTextView.text = "Status: ${task.status}"

        // Set click listener for the entire card
        holder.itemView.setOnClickListener {
            val intent = Intent(context, QRScanActivity::class.java).apply {
                putExtra(QRScanActivity.EXTRA_TASK_TITLE, task.title)
                putExtra(QRScanActivity.EXTRA_TASK_ID, task.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.serviceName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.serviceDescription)
        val buildingTextView: TextView = itemView.findViewById(R.id.serviceBuilding)
    }
} 