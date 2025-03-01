package cashierapp.data.remote.local

import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val KEY_TOKEN = "jwt_token"
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString(KEY_TOKEN, "")

        if (token.isNullOrEmpty()) {
            return null
        }

        return if (isTokenExpired(token)) {
            clearToken()
            null
        } else {
            token
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        try {

            val parts = token.split(".")
            if (parts.size != 3) {
                return true
            }

            val payload = parts[1]


            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes, StandardCharsets.UTF_8)


            val jsonObject = JSONObject(decodedString)


            // Get expiration time ("exp" claim)
            val expirationTime = jsonObject.optLong("exp", 0)

            // If there's no exp claim or it's 0, assume token doesn't expire
            if (expirationTime == 0L) {
                return false
            }

            // Compare with current time (exp is in seconds, currentTimeMillis is in milliseconds)
            val currentTime = System.currentTimeMillis() / 1000

            return expirationTime < currentTime
        } catch (e: Exception) {
            // If there's any error in parsing, consider the token invalid
            return true
        }
    }

    fun clearToken() {
        sharedPreferences.edit().remove(KEY_TOKEN).apply()
    }
}