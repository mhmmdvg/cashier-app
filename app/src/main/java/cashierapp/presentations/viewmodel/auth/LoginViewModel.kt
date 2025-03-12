package cashierapp.presentations.viewmodel.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cashierapp.data.remote.repository.AuthRepository
import cashierapp.data.resources.Resource
import cashierapp.domain.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Success(null))
    val loginState: StateFlow<Resource<LoginResponse>> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()

            try {
                val result = authRepository.login(email, password)

                result.fold(
                    onSuccess = { response ->
                        _loginState.value = Resource.Success(response)
                    },
                    onFailure = { exception ->
                        _loginState.value = Resource.Error(exception.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

}