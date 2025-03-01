package cashierapp.data.remote.repository

import ProductResponse
import cashierapp.data.remote.api.ProductApi
import cashierapp.data.remote.api.ProductDetailApi
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApi: ProductApi
) {
    suspend fun getProducts(): Result<List<ProductResponse>> {
        return try {
            val res = productApi.products()

            if (res.isSuccessful) {
                Result.success(res.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch products"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class ProductDetailRepository @Inject constructor(
    private val productDetailApi: ProductDetailApi
) {
    suspend fun getDetailProducts(productId: String): Result<ProductResponse> {
        return try {
            val res = productDetailApi.detailProducts(productId)

            if(res.isSuccessful) {
                res.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("Failed to fetch detail product"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}