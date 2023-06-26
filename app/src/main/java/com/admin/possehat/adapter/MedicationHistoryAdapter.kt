package com.admin.possehat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.admin.possehat.R
import com.admin.possehat.model.Drug

class MedicationHistoryAdapter : ListAdapter<Drug, MedicationHistoryAdapter.MedicationHistoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medication_history, parent, false)
        return MedicationHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationHistoryViewHolder, position: Int) {
        val drug = getItem(position)
        holder.bind(drug)
    }

    inner class MedicationHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMedicationName: TextView = itemView.findViewById(R.id.tvMedicationName)
        private val tvMedicationStock: TextView = itemView.findViewById(R.id.tvMedicationStock)

        fun bind(drug: Drug) {
            tvMedicationName.text = drug.nama
            tvMedicationStock.text = itemView.context.getString(R.string.stock, drug.stok)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Drug>() {
        override fun areItemsTheSame(oldItem: Drug, newItem: Drug): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Drug, newItem: Drug): Boolean {
            return oldItem == newItem
        }
    }
}
