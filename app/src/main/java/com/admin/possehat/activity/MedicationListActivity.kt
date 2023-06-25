package com.admin.possehat.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.possehat.api.MedicationApiService
import com.admin.possehat.databinding.ActivityMedicationListBinding
import com.admin.possehat.model.Medication
import com.admin.possehat.adapter.MedicationAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MedicationListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMedicationListBinding
    private lateinit var apiService: MedicationApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiobat-fglidlwv3q-et.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(MedicationApiService::class.java)

        binding.fabAddMedication.setOnClickListener {
            val intent = Intent(this, AddMedicationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadMedications()
    }

    private fun loadMedications() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getMedications()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body()?.data != null) {
                    setupRecyclerView(response.body()!!.data!!)
                } else {
                    Toast.makeText(this@MedicationListActivity, "Gagal mengambil data obat", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupRecyclerView(medications: List<Medication>) {
        val adapter = MedicationAdapter(medications)
        binding.rvMedicationList.layoutManager = LinearLayoutManager(this)
        binding.rvMedicationList.adapter = adapter
    }
}
