package com.admin.possehat.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.admin.possehat.api.MedicationApiService
import com.admin.possehat.databinding.ActivityAddMedicationBinding
import com.admin.possehat.model.Medication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddMedicationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMedicationBinding
    private lateinit var apiService: MedicationApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiobat-fglidlwv3q-et.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(MedicationApiService::class.java)

        binding.btnAddMedication.setOnClickListener {
            if (validateInputs()) {
                addMedication()
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (binding.etMedicationName.text.toString().isEmpty()) {
            binding.etMedicationName.error = "Field ini tidak boleh kosong"
            return false
        }

        if (binding.etMedicationType.text.toString().isEmpty()) {
            binding.etMedicationType.error = "Field ini tidak boleh kosong"
            return false
        }

        if (binding.etMedicationStock.text.toString().isEmpty()) {
            binding.etMedicationStock.error = "Field ini tidak boleh kosong"
            return false
        }

        if (binding.etMedicationExpiryDate.text.toString().isEmpty()) {
            binding.etMedicationExpiryDate.error = "Field ini tidak boleh kosong"
            return false
        }

        return true
    }

    private fun addMedication() {
        val name = binding.etMedicationName.text.toString()
        val type = binding.etMedicationType.text.toString()
        val stock = binding.etMedicationStock.text.toString().toInt()
        val expiryDate = binding.etMedicationExpiryDate.text.toString()

        val medication = Medication(name = name, type = type, stock = stock, expiryDate = expiryDate)

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.addMedication(medication)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body()?.codeRespon == 200) {
                    Toast.makeText(this@AddMedicationActivity, "Obat berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddMedicationActivity, "Gagal menambahkan obat", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
