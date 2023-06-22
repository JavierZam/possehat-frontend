package com.admin.possehat.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.admin.possehat.api.UserApiService
import com.admin.possehat.databinding.ActivityHomeBinding
import com.admin.possehat.util.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userApiService: UserApiService
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiuser-fglidlwv3q-et.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userApiService = retrofit.create(UserApiService::class.java)
        binding.viewPatients.setOnClickListener {
            val intent = Intent(this@HomeActivity, PatientListActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = userApiService.logout()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body()?.code_respon == 200) {
                    preferencesHelper.clear() // Clear all preferences on successful logout
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@HomeActivity, response.body()?.message ?: "Logout failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
