package cashierapp.utils

import cashierapp.domain.model.SelectItemModel
import com.composables.icons.lucide.CupSoda
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Popcorn


val SelectTypeList: List<SelectItemModel> = listOf(
    SelectItemModel(icon = Lucide.CupSoda, id = 1, name = "DRINK"),
    SelectItemModel(icon = Lucide.Popcorn, id = 2, name = "FOOD"),
)

val SelectSizeList: List<SelectItemModel> = listOf(
    SelectItemModel(id = 1, name = "PLASTIC"),
    SelectItemModel(id = 2, name = "MEDIUM"),
    SelectItemModel(id = 3, name = "JUMBO")
)