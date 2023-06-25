package com.admin.possehat.api

import com.admin.possehat.model.Medication
import com.admin.possehat.model.MedicationResponse
import retrofit2.Response
import retrofit2.http.*

interface MedicationApiService {
    @GET("/drugs")
    suspend fun getMedications(): Response<MedicationResponse>

    @POST("/drugs")
    suspend fun addMedication(@Body medication: Medication): Response<MedicationResponse>

    @GET("/drugs/{id}")
    suspend fun getMedicationById(@Path("id") id: String): Response<MedicationResponse>

    @PUT("/drugs/{id}")
    suspend fun updateMedication(@Path("id") id: String, @Body medication: Medication): Response<MedicationResponse>

    @DELETE("/drugs/{id}")
    suspend fun deleteMedication(@Path("id") id: String): Response<MedicationResponse>


}
