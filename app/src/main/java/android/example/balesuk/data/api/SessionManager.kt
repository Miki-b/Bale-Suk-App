package android.example.balesuk.data.api

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
