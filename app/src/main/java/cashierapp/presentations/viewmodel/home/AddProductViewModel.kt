package cashierapp.presentations.viewmodel.home

import ProductRequestRes
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashierapp.data.remote.local.TokenManager
import cashierapp.data.remote.repository.ProductAddItemRepository
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
class AddProductViewModel @Inject constructor(
    private val productRepository: ProductAddItemRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {
    private val _addProductState =
        MutableStateFlow<Resource<ProductRequestRes>>(Resource.Success(null))
    val addProductState: StateFlow<Resource<ProductRequestRes>> = _addProductState.asStateFlow()

    fun getToken(): String? {
        return tokenManager.getToken()
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