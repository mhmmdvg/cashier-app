package cashierapp.presentations.ui.components

import ProductResponse
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cashierapp.presentations.ui.theme.BorderGray
import cashierapp.presentations.ui.theme.PrimaryColor

@Composable
fun SortChips(product: List<ProductResponse>, onClick: (name: String) -> Unit) {
    var selected by remember { mutableStateOf<Int?>(null) }

    val scrollState = rememberScrollState()
    val uniqueProductName = product.distinctBy { it.name }

    Row(
        modifier = Modifier.horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        uniqueProductName.forEachIndexed { index, it ->
            Box(
                modifier = Modifier
                    .wrapContentWidth()
//                   For outer padding because declare before border
                    .padding(horizontal = 4.dp, vertical = 6.dp)
                    .border(
                        width = 1.dp,
                        color = if (selected == index) Color.Transparent else BorderGray,
                        shape = CircleShape
                    )
                    .background(
                        color = if (selected == index) PrimaryColor else Color.White,
                        shape = CircleShape
                    )
                    .clip(shape = CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true),
                    ) {
                        selected = if (selected == index) null else index
                        onClick(it.name)
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it.name ?: "Undefined",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected == index) Color.White else Color.Black,
                    maxLines = 1,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}