package android.example.balesuk.data.models

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
    // e.g., "patient" or "doctor"
)
