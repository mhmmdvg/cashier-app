package cashierapp.utils

import cashierapp.data.remote.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenMangaer: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${tokenMangaer.getToken()}")
            .build()

        return chain.proceed(req)
    }
}