package android.example.balesuk.data.models

data class AuthResponse(
    val success: Boolean,
    val data: AuthData
)

data class AuthData(
    val token: String,
    val token_type: String,
    val user: User
)
