package cashierapp.presentations.ui.screens.order

import ProductResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cashierapp.data.remote.repository.ProductDetailRepository
import cashierapp.data.resources.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val productDetailRepository: ProductDetailRepository
) : ViewModel() {
    private val _detailProduct = MutableStateFlow<Resource<ProductResponse>>(Resource.Success(null))
    val detailProduct: StateFlow<Resource<ProductResponse>> = _detailProduct

    fun fetchDetailProduct(productId: String) {
        viewModelScope.launch {
            _detailProduct.value = Resource.Loading()

            productDetailRepository.getDetailProducts(productId).fold(
                onSuccess = { product ->
                    _detailProduct.value = Resource.Success(product)
                },
                onFailure = { exception ->  
                    _detailProduct.value = Resource.Error(exception.message ?: "Unknown error occured")
                }
            )
        }
    }
}