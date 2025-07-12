package android.example.balesuk.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import android.example.balesuk.data.api.RetrofitInstance
import android.example.balesuk.data.models.RegisterRequest
import android.example.balesuk.databinding.ActivityRegisterBinding
import org.json.JSONObject
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLoginPrompt.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val fullName = binding.editFullName.text.toString()
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            val confirmPassword=binding.editConfirmPassword.text.toString()
            if (validateInputs(fullName, email, password,confirmPassword)) {
                registerUser(fullName, email, password,confirmPassword)
            }
        }
    }

    private fun validateInputs(name: String, email: String, password: String, confirmPassword: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please fill in all fields")
            return false
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            showToast("Please enter a valid email address")
            return false
        }

        if (password.length < 6) {
            showToast("Password must be at least 6 characters")
            return false
        }

        if (password != confirmPassword) {
            showToast("Passwords do not match")
            return false
        }

        return true
    }


    private fun registerUser(name: String, email: String, password: String, confirmPassword: String) {
        lifecycleScope.launch {
            try {
                val request = RegisterRequest(name, email, password, confirmPassword)
                val response = RetrofitInstance.api.register(request)

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    val token = authResponse?.data?.token

                    if (token != null) {
                        showToast("Registration successful!")
                        // Optionally save token here
                        startActivity(Intent(this@Register, Login::class.java))
                        finish()
                    } else {
                        showToast("Unexpected response format")
                    }
                } else {
                    val error = response.errorBody()?.string()
                    showToast("Register failed: ${extractErrorMessage(error)}")
                }
            } catch (e: Exception) {
                showToast("Something went wrong: ${e.localizedMessage}")
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun extractErrorMessage(json: String?): String {
        return try {
            val obj = JSONObject(json ?: "")
            obj.getString("message")
        } catch (e: Exception) {
            "Unknown error"
        }
    }
}
