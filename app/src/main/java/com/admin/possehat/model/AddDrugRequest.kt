package com.admin.possehat.model

import com.google.gson.annotations.SerializedName

data class AddDrugRequest(
    @SerializedName("nama") val nama: String,
    @SerializedName("stok") val stok: Int
)