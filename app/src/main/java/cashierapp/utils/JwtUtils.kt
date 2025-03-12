package cashierapp.utils

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.nio.charset.StandardCharsets

object JwtUtils {

    private const val TAG = "JwtUtils"

    fun decodeToken(token: String): JSONObject? {
        try {
            val parts = token.split(".")

            if (parts.size != 3) {
                Log.e(TAG, "Invalid token format")
                return null
            }

            val payload = parts[1]
            val decodeBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodeString = String(decodeBytes, StandardCharsets.UTF_8)

            return JSONObject(decodeString)
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding token", e)
            return null
        }
    }

    fun getClaim(token: String, claimName: String): String? {
        try {
            val payload = decodeToken(token) ?: return null
            return if (payload.has(claimName)) payload.getString(claimName) else null
        } catch (e: Exception) {
            Log.e(TAG, "Error getting claim: $claimName", e)
            return null
        }
    }

    fun getUserId(token: String): String? {
        return getClaim(token, "userId")
    }
}