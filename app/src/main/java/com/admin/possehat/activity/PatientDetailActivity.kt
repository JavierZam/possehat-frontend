package com.admin.possehat.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.admin.possehat.api.PatientApiService
import com.admin.possehat.databinding.ActivityPatientDetailBinding
import com.admin.possehat.model.Patient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.admin.possehat.R

class PatientDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientDetailBinding
    private lateinit var patientApiService: PatientApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apipatient-fglidlwv3q-et.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        patientApiService = retrofit.create(PatientApiService::class.java)

        val patientId = intent.getStringExtra("patientId")

        loadPatientDetails(patientId)
    }

    private fun loadPatientDetails(patientId: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (patientId != null) {
                    val response = patientApiService.getPatientById(patientId)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body()?.code_respon == 200) {
                            val patient = response.body()?.data
                            if (patient != null) {
                                displayPatientDetails(patient)
                            } else {
                                Toast.makeText(this@PatientDetailActivity, "Patient not found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@PatientDetailActivity, "Failed to fetch patient data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PatientDetailActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayPatientDetails(patient: Patient) {
        binding.tvPatientName.text = patient.nama
        binding.tvPatientAge.text = getString(R.string.umur, patient.umur)
        binding.tvPatientAddress.text = getString(R.string.alamat, patient.alamat)
        binding.tvPatientWeight.text = getString(R.string.berat, patient.berat)
        binding.tvPatientBloodPressure.text = getString(R.string.tensi, patient.tensi)
    }
}
