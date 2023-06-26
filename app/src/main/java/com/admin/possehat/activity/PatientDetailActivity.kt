package com.admin.possehat.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.possehat.R
import com.admin.possehat.adapter.*
import android.widget.ArrayAdapter
import com.admin.possehat.model.AddDrugRequest
import com.admin.possehat.api.PatientApiService
import com.admin.possehat.databinding.ActivityPatientDetailBinding
import com.admin.possehat.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PatientDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientDetailBinding
    private lateinit var patientApiService: PatientApiService
    private lateinit var medicationHistoryAdapter: MedicationHistoryAdapter
    private var patientId: String? = null
    private var drugs: List<Drug> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        patientId = intent.getStringExtra("patientId") ?: run {
            Toast.makeText(this, "No patient ID provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.base_url_patient))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        patientApiService = retrofit.create(PatientApiService::class.java)

        setupRecyclerView()
        loadPatientDetails()
        loadAvailableDrugs()
        loadPatientDrugHistory()

        binding.btnAddMedicine.setOnClickListener {
            addDrugToPatient()
        }
    }

    private fun setupRecyclerView() {
        binding.rvMedicineHistory.layoutManager = LinearLayoutManager(this)
        medicationHistoryAdapter = MedicationHistoryAdapter()
        binding.rvMedicineHistory.adapter = medicationHistoryAdapter
    }

    private fun loadPatientDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                patientId?.let {
                    val response = patientApiService.getPatientById(it)

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

    private fun loadAvailableDrugs() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = patientApiService.getDrugs()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()?.code_respon == 200) {
                        drugs = response.body()?.data ?: emptyList()
                        displayAvailableDrugs()
                    } else {
                        Toast.makeText(this@PatientDetailActivity, "Failed to fetch drugs data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PatientDetailActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadPatientDrugHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                patientId?.let {
                    val response = patientApiService.getDrugHistoryByPatientId(it)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body()?.code_respon == 200) {
                            val drugHistory = response.body()?.data
                            if (!drugHistory.isNullOrEmpty()) {
                                displayDrugHistory(drugHistory)
                            } else {
                                Toast.makeText(this@PatientDetailActivity, "No drug history found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@PatientDetailActivity, "Failed to fetch drug history data", Toast.LENGTH_SHORT).show()
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

    private fun addDrugToPatient() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val selectedDrug = binding.spMedicines.selectedItem as Drug
                val drugQuantity = binding.etMedicineQuantity.text.toString().toInt()

                if (selectedDrug == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@PatientDetailActivity, "Please select a drug", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                patientId?.let {
                    val response = patientApiService.addDrugHistoryToPatient(
                        it,
                        AddDrugRequest(selectedDrug.nama, drugQuantity)
                    )

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body()?.code_respon == 200) {
                            Toast.makeText(this@PatientDetailActivity, "Drug added successfully", Toast.LENGTH_SHORT).show()
                            loadPatientDrugHistory()
                        } else {
                            Toast.makeText(this@PatientDetailActivity, "Failed to add drug", Toast.LENGTH_SHORT).show()
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

    private fun displayAvailableDrugs() {
        val drugAdapter = DrugAdapter(this, R.layout.item_drug_spinner, drugs)
        binding.spMedicines.adapter = drugAdapter
    }


    private fun displayDrugHistory(drugHistory: List<Drug>) {
        medicationHistoryAdapter.submitList(drugHistory)
    }
}
