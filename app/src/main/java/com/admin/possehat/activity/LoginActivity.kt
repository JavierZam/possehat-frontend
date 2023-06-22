package com.admin.possehat.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.admin.possehat.api.UserApiService
import com.admin.possehat.model.LoginRequest
import com.admin.possehat.databinding.ActivityLoginBinding
import com.admin.possehat.util.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var userApiService: UserApiService
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiuser-fglidlwv3q-et.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userApiService = retrofit.create(UserApiService::class.java)

        checkLoginStatus()

        binding.login.setOnClickListener {
            val emailInput = binding.email.text.toString()
            val passwordInput = binding.password.text.toString()

            if (emailInput.isBlank() || passwordInput.isBlank()) {
                Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val loginRequest = LoginRequest(emailInput, passwordInput)
                login(loginRequest)
            }
        }
    }

    private fun checkLoginStatus() {
        if (preferencesHelper.isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login(loginRequest: LoginRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = userApiService.login(loginRequest)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body()?.code_respon == 200) {
                    val user = response.body()?.data
                    if (user != null) {
                        preferencesHelper.isLoggedIn = true
                        preferencesHelper.uid = user.uid
                        preferencesHelper.email = user.email
                        preferencesHelper.token = user.token
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, response.body()?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
