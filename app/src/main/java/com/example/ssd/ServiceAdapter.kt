package com.example.ssd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServiceAdapter(
    private val services: List<Service>
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    // 1. ViewHolder holds references to the views in each item
    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.tvServiceName)
        val buildingText: TextView = itemView.findViewById(R.id.tvServiceBuilding)
        val descriptionText: TextView = itemView.findViewById(R.id.tvServiceDescription)
    }

    // 2. Inflate the item layout and create a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    // 3. Bind a Service object’s data to the views
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.nameText.text = service.name
        holder.buildingText.text = service.building
        holder.descriptionText.text = service.description
    }

    // 4. Return the total number of items
    override fun getItemCount(): Int = services.size
}
