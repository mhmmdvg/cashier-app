data class ProductResponse(
    val id: String,
    val name: String,
    val price: Int,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val createdBy: String,
    val updatedBy: String?,
    val size: String,
    val type: String,
    val image: String?
)

data class ProductRequest(
    val userId: String,
    val name: String,
    val price: Int,
    val description: String?,
    val size: String,
    val type: String,
    val image: String?
)

data class ProductRequestRes(
    val id: String,
    val createdAt: String,
    val message: String
)