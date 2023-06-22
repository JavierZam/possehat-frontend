package com.admin.possehat.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.*
import com.admin.possehat.model.LoginRequest
import com.admin.possehat.model.EmailRequest
import com.admin.possehat.model.EditProfileRequest
import com.admin.possehat.model.User
import com.admin.possehat.model.ApiResponse

interface UserApiService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ApiResponse<User>>

    @POST("/resetPassword")
    suspend fun resetPassword(@Body emailRequest: EmailRequest): Response<ApiResponse<Unit>>

    @GET("/user/{uid}")
    suspend fun getUserData(@Path("uid") uid: String): Response<ApiResponse<User>>

    @POST("/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @PUT("/edit-profile/{uid}")
    suspend fun editProfile(@Path("uid") uid: String, @Body editProfileRequest: EditProfileRequest): Response<ApiResponse<Unit>>
}
