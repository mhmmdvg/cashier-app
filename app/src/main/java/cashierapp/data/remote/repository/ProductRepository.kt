package cashierapp.data.remote.repository

import ProductRequest
import ProductRequestRes
import ProductResponse
import android.util.Log
import cashierapp.data.remote.api.ProductAddItemApi
import cashierapp.data.remote.api.ProductApi
import cashierapp.data.remote.api.ProductDetailApi
import retrofit2.HttpException
import retrofit2.Response
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

            if (res.isSuccessful) {
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

class ProductAddItemRepository @Inject constructor(
    private val productAddItemApi: ProductAddItemApi
) {

    suspend fun addProduct(
        userId: String,
        name: String,
        price: Int,
        description: String? = null,
        size: String,
        type: String,
        image: String? = null
    ): Result<ProductRequestRes> {

        val request = ProductRequest(
            userId = userId,
            name = name,
            price = price,
            description = description,
            size = size,
            type = type,
            image = image
        )

        return try {
            val response = productAddItemApi.addProduct(request)

            if (response.isSuccessful) {
                Log.d("AddProductFormResponse", response.body().toString())

                response.body()?.let {
                    Result.success(it)
                }
                    ?: Result.failure(IllegalStateException("Empty response body for product addition"))
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}