package com.admin.possehat.api

import com.admin.possehat.model.ApiResponse
import com.admin.possehat.model.PatientResponse
import com.admin.possehat.model.Patient
import com.admin.possehat.model.AddPatientRequest
import retrofit2.Response
import retrofit2.http.*

interface PatientApiService {
    @GET("/patients")
    suspend fun getPatients(): Response<ApiResponse<List<Patient>>>

    @GET("/patients/{id}")
    suspend fun getPatientById(@Path("id") id: String): Response<ApiResponse<Patient>>

    @POST("/patients")
    suspend fun addPatient(@Body patient: AddPatientRequest): Response<ApiResponse<PatientResponse>>

    @PUT("/patients/{id}")
    suspend fun updatePatient(@Path("id") id: String, @Body patient: Patient): Response<ApiResponse<Unit>>

    @DELETE("/patients/{id}")
    suspend fun deletePatient(@Path("id") id: String): Response<ApiResponse<Unit>>
}
