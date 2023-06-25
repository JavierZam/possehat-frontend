package com.admin.possehat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.admin.possehat.R
import com.admin.possehat.model.Medication

class MedicationAdapter(private val medications: List<Medication>) : RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder>() {

    class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvMedicationName)
        val tvType: TextView = itemView.findViewById(R.id.tvMedicationType)
        val tvStock: TextView = itemView.findViewById(R.id.tvMedicationStock)
        val tvExpireDate: TextView = itemView.findViewById(R.id.tvMedicationExpiry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medication, parent, false)
        return MedicationViewHolder(view)
    }

    override fun getItemCount(): Int = medications.size

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val medication = medications[position]
        holder.tvName.text = medication.name
        holder.tvType.text = medication.type
        holder.tvStock.text = medication.stock.toString()
        holder.tvExpireDate.text = medication.expiryDate
    }
}
