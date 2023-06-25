package com.admin.possehat.model

import com.google.gson.annotations.SerializedName

data class PatientResponse(
    @SerializedName("data") val data: Patient,
    @SerializedName("message") val message: String,
    @SerializedName("code_respon") val codeResponse: Int
)
