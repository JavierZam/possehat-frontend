package com.admin.possehat.model

import com.google.gson.annotations.SerializedName

data class Medication(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("nama")
    var name: String,

    @SerializedName("jenis")
    var type: String,

    @SerializedName("stok")
    var stock: Int,

    @SerializedName("expired")
    var expiryDate: String
)
