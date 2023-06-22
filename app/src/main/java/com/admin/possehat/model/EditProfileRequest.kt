package com.admin.possehat.model

data class EditProfileRequest(
    val email: String,
    val password: String,
    val phone: String,
    val currentEmail: String,
    val currentPassword: String
)
