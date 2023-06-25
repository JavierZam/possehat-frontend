package com.admin.possehat.model

import com.google.gson.annotations.SerializedName

data class MedicationResponse(
    @SerializedName("data")
    var data: List<Medication>?,

    @SerializedName("message")
    var message: String,

    @SerializedName("code_respon")
    var codeRespon: Int
)
