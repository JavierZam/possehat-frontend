package com.admin.possehat.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.admin.possehat.api.PatientApiService
import com.admin.possehat.model.AddPatientRequest
import com.admin.possehat.databinding.ActivityAddPatientBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson

class AddPatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPatientBinding
    private lateinit var apiService: PatientApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apipatient-fglidlwv3q-et.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(PatientApiService::class.java)

        binding.btnAddPatient.setOnClickListener {
            if (validateInputs()) {
                addPatient()
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (binding.etPatientName.text.toString().isEmpty()) {
            binding.etPatientName.error = "Field ini tidak boleh kosong"
            return false
        }

        if (binding.etPatientAge.text.toString().isEmpty()) {
            binding.etPatientAge.error = "Field ini tidak boleh kosong"
            return false
        }

        if (binding.etPatientAddress.text.toString().isEmpty()) {
            binding.etPatientAddress.error = "Field ini tidak boleh kosong"
            return false
        }

        if (binding.etPatientWeight.text.toString().isEmpty()) {
            binding.etPatientWeight.error = "Field ini tidak boleh kosong"
            return false
        }

        if (binding.etPatientTension.text.toString().isEmpty()) {
            binding.etPatientTension.error = "Field ini tidak boleh kosong"
            return false
        }

        return true
    }
    data class ErrorResponse(
        val error: String,
        val message: String,
        val code_respon: Int
    )

    private fun addPatient() {
        val name = binding.etPatientName.text.toString()
        val age = binding.etPatientAge.text.toString().toInt()
        val address = binding.etPatientAddress.text.toString()
        val weight = binding.etPatientWeight.text.toString().toDouble()
        val tension = binding.etPatientTension.text.toString()

        val patientRequest = AddPatientRequest(
            nama = name,
            umur = age,
            alamat = address,
            berat = weight,
            tensi = tension
        )

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.addPatient(patientRequest)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddPatientActivity, "Pasien berhasil ditambahkan", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    val errorResponse = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                    AlertDialog.Builder(this@AddPatientActivity)
                        .setTitle("Error")
                        .setMessage(errorResponse.message)
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }

}
