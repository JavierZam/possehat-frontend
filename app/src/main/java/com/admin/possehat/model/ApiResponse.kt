package com.admin.possehat.model

data class ApiResponse<T>(
    val data: T,
    val message: String,
    val code_respon: Int
)
