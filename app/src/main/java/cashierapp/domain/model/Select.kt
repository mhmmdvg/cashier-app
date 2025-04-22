package cashierapp.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class SelectItemModel(
    val icon: ImageVector? = null,
    val id: Int,
    val name: String
)