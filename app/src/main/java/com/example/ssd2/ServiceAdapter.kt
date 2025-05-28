package com.example.ssd2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServiceAdapter(private var services: List<Service>) : 
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
    
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.service_card_item, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.nameTextView.text = service.name
        holder.descriptionTextView.text = service.description
        holder.buildingTextView.text = "Location: ${service.building}"

        // Set click listener for the entire card
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ServiceDetailsActivity::class.java).apply {
                putExtra(ServiceDetailsActivity.EXTRA_NAME, service.name)
                putExtra(ServiceDetailsActivity.EXTRA_DESCRIPTION, service.description)
                putExtra(ServiceDetailsActivity.EXTRA_BUILDING, service.building)
                putExtra(ServiceDetailsActivity.EXTRA_SERVICE_ID, service.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = services.size

    fun updateServices(newServices: List<Service>) {
        services = newServices
        notifyDataSetChanged()
    }

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.serviceName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.serviceDescription)
        val buildingTextView: TextView = itemView.findViewById(R.id.serviceBuilding)
    }
} 