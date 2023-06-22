package com.admin.possehat.model

import com.google.gson.annotations.SerializedName

data class Patient(
    @SerializedName("id") val id: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("umur") val umur: Int,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("berat") val berat: Double,
    @SerializedName("tensi") val tensi: String
)
