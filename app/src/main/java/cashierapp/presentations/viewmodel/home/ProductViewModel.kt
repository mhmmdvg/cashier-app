package cashierapp.presentations.viewmodel.home

import ProductRequestRes
import cashierapp.data.remote.repository.ProductRepository
import ProductResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashierapp.data.remote.local.TokenManager
import cashierapp.data.resources.Resource
import cashierapp.presentations.ui.screens.home.ProductEvent
import cashierapp.presentations.ui.screens.home.ProductEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _products =
        MutableStateFlow<Resource<List<ProductResponse>>>(Resource.Success(null))
    val products: StateFlow<Resource<List<ProductResponse>>> = _products.asStateFlow()

    private val _addProductState =
        MutableStateFlow<Resource<ProductRequestRes>>(Resource.Success(null))
    val addProductState: StateFlow<Resource<ProductRequestRes>> = _addProductState.asStateFlow()

    private val _detailProduct = MutableStateFlow<Resource<ProductResponse>>(Resource.Success(null))
    val detailProduct: StateFlow<Resource<ProductResponse>> = _detailProduct

    fun getToken(): String? {
        return tokenManager.getToken()
    }

    init {
        fetchProducts()

        viewModelScope.launch {
            ProductEventBus.events.collect { event ->
                when (event) {
                    is ProductEvent.ProductAdded,
                    is ProductEvent.ProductUpdated,
                    is ProductEvent.ProductDeleted -> fetchProducts()
                }
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _products.value = Resource.Loading()

            productRepository.getProducts()
                .onSuccess { products ->
                    _products.value = Resource.Success(products)
                }

                .onFailure { exception ->
                    _products.value = Resource.Error(
                        message = exception.message ?: "Unknown error occured",
                        data = _products.value.data
                    )
                }
        }
    }

    fun fetchDetailProduct(productId: String) {
        viewModelScope.launch {
            _detailProduct.value = Resource.Loading()

            productRepository.getDetailProducts(productId).fold(
                onSuccess = { product ->
                    _detailProduct.value = Resource.Success(product)
                },
                onFailure = { exception ->
                    _detailProduct.value =
                        Resource.Error(exception.message ?: "Unknown error occured")
                }
            )
        }
    }

    fun addProduct(
        userId: String,
        name: String,
        price: Int,
        description: String? = null,
        size: String,
        type: String,
        image: String? = null
    ) {

        viewModelScope.launch {
            _addProductState.value = Resource.Loading()

            try {
                val result = productRepository.addProduct(
                    userId,
                    name,
                    price,
                    description,
                    size,
                    type,
                    image
                )


                result.fold(
                    onSuccess = { response ->
                        _addProductState.value = Resource.Success(response)

                        ProductEventBus.emitEvent(ProductEvent.ProductAdded)

                        delay(300)
                        _addProductState.value = Resource.Success(null)
                    },
                    onFailure = { exception ->
                        _addProductState.value =
                            Resource.Error(exception.message ?: "Unknown Error")
                    }
                )
            } catch (e: Exception) {
                _addProductState.value = Resource.Error(e.message ?: "Unknown Error")
            }
        }
    }
}