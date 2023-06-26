package com.admin.possehat.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.admin.possehat.api.PatientApiService
import com.admin.possehat.databinding.ActivityPatientListBinding
import com.admin.possehat.model.Patient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.admin.possehat.R
import android.widget.TextView

class PatientListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientListBinding
    private lateinit var patientApiService: PatientApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.base_url_patient))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the API service
        patientApiService = retrofit.create(PatientApiService::class.java)

        // Load the patient list
        loadPatientList()
    }

    private fun loadPatientList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = patientApiService.getPatients()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()?.code_respon == 200) {
                        val patientList = response.body()?.data
                        if (!patientList.isNullOrEmpty()) {
                            displayPatientList(patientList)
                        } else {
                            Toast.makeText(this@PatientListActivity, "No patients found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@PatientListActivity, "Failed to fetch patient data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PatientListActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayPatientList(patientList: List<Patient>) {
        val llPatientList = binding.llPatientList

        // Clear any existing views
        llPatientList.removeAllViews()

        // Create and add views for each patient
        for (patient in patientList) {
            val patientView = layoutInflater.inflate(R.layout.item_patient, llPatientList, false)

            val tvPatientName = patientView.findViewById<TextView>(R.id.tvPatientName)
            val tvPatientAge = patientView.findViewById<TextView>(R.id.tvPatientAge)

            tvPatientName.text = patient.nama
            tvPatientAge.text = getString(R.string.umur, patient.umur)

            // Set click listener to open patient detail activity
            patientView.setOnClickListener {
                val intent = Intent(this@PatientListActivity, PatientDetailActivity::class.java)
                intent.putExtra("patientId", patient.id)
                startActivity(intent)
            }

            llPatientList.addView(patientView)
        }
    }
}
