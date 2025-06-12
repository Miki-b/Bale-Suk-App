package android.example.balesuk.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.example.balesuk.R
import android.example.balesuk.data.api.RetrofitInstance
import android.example.balesuk.data.api.SessionManager
import android.example.balesuk.data.models.LoginRequest
import android.example.balesuk.databinding.ActivityLoginBinding
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // If token exists, skip login screen
        if (sessionManager.fetchAuthToken() != null) {
            startActivity(Intent(this, Home::class.java))
            finish()
        }

        binding.tvRegisterPrompt.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    val token = authResponse?.data?.token

                    if (token != null) {
                        sessionManager.saveAuthToken(token)
                        showToast("Login successful")
                        startActivity(Intent(this@Login, Home::class.java))
                        finish()
                    } else {
                        showToast("Unexpected response format")
                    }
                } else {
                    val error = response.errorBody()?.string()
                    showToast("Login failed: ${extractErrorMessage(error)}")
                }
            } catch (e: Exception) {
                showToast("Something went wrong: ${e.localizedMessage}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun extractErrorMessage(json: String?): String {
        return try {
            val jsonObject = JSONObject(json ?: "")
            jsonObject.getString("message")
        } catch (e: Exception) {
            "Unknown error occurred"
        }
    }
}
