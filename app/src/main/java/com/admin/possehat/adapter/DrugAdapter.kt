package com.admin.possehat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.admin.possehat.R
import com.admin.possehat.model.Drug

class DrugAdapter(context: Context, private val resource: Int, private val items: List<Drug>) :
    ArrayAdapter<Drug>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val drug = items[position]

        val tvDrugName = view.findViewById<TextView>(R.id.tvDrugName)
        val tvDrugType = view.findViewById<TextView>(R.id.tvDrugType)
        val tvDrugExpired = view.findViewById<TextView>(R.id.tvDrugExpired)
        val tvDrugStock = view.findViewById<TextView>(R.id.tvDrugStock)

        tvDrugName.text = drug.nama
        tvDrugType.text = drug.jenis
        tvDrugExpired.text = drug.expired
        tvDrugStock.text = drug.stok.toString()

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
