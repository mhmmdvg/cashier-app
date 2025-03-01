package cashierapp.domain.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val token: String
)

data class ErrorResponse(
    val error: String
)
