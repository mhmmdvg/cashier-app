package cashierapp.data.remote.api

import ProductRequest
import ProductRequestRes
import ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApi {
    @GET("products/")
    suspend fun products(): Response<List<ProductResponse>>

    @GET("products/{id}")
    suspend fun detailProducts(@Path("id") productId: String): Response<ProductResponse>

    @POST("products/")
    suspend fun addProduct(@Body request: ProductRequest): Response<ProductRequestRes>
}