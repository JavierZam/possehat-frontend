package com.admin.possehat.model

import com.google.gson.annotations.SerializedName

data class Drug(
    @SerializedName("id") val id: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("stok") val stok: Int,
    @SerializedName("expired") val expired: String,
    @SerializedName("jenis") val jenis: String
)