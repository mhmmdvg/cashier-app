package cashierapp.presentations.viewmodel.home

import CartItem
import CartProduct
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
    val detailProduct: StateFlow<Resource<ProductResponse>> = _detailProduct.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> = _totalPrice.asStateFlow()

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


    fun addToCart(quantity: Int, product: CartProduct) {
        val currentItems = _cartItems.value.toMutableList()

        val existingItemIndex = currentItems.indexOfFirst { it.product.id == product.id }

        if (existingItemIndex >= 0) {
            val existingItem = currentItems[existingItemIndex]
            val updatedProduct =
                existingItem.product.copy(qty = existingItem.product.qty + quantity)
            currentItems[existingItemIndex] =
                existingItem.copy(
                    product = updatedProduct,
                    quantity = existingItem.quantity + quantity
                )
        } else {
            val updatedProduct = product.copy(qty = quantity)
            currentItems.add(CartItem(quantity, updatedProduct))
        }

        _cartItems.value = currentItems
        calculateTotal()
    }

    fun clearProductCart() {
        _cartItems.value = emptyList()
    }

    fun increaseProductQty(productId: String) {
        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.product.id == productId }

        if (itemIndex >= 0) {
            val item = currentItems[itemIndex]

            val updatedProduct = item.product.copy(qty = item.product.qty + 1)

            currentItems[itemIndex] = item.copy(
                product = updatedProduct,
                quantity = item.quantity + 1
            )
            _cartItems.value = currentItems
            calculateTotal()
        }
    }

    fun decreaseProductQty(productId: String) {
        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.product.id == productId }

        if (itemIndex < 0) return

        val item = currentItems[itemIndex]

        if (item.product.qty > 1) {
            val updatedProduct = item.product.copy(qty = item.product.qty - 1)

            currentItems[itemIndex] = item.copy(
                product = updatedProduct,
                quantity = item.quantity - 1
            )

            _cartItems.value = currentItems
            calculateTotal()
            return
        }

        if (item.quantity > 1) {
            currentItems[itemIndex] = item.copy(quantity = item.quantity - 1)
            _cartItems.value = currentItems
            calculateTotal()
            return
        }

        currentItems.removeAt(itemIndex)
        _cartItems.value = currentItems
        calculateTotal()
    }

    private fun calculateTotal() {
        val total = _cartItems.value.sumOf { it.product.price * it.quantity }
        _totalPrice.value = total
    }
}