package cashierapp.presentations.ui.screens.home

import cashierapp.data.remote.repository.ProductRepository
import ProductResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashierapp.data.resources.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _products = MutableStateFlow<Resource<List<ProductResponse>>>(Resource.Success(null))
    val products: StateFlow<Resource<List<ProductResponse>>> = _products.asStateFlow()

    init {
        fetchProducts()
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
}