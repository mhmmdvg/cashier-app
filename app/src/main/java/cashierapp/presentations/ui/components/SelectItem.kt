package cashierapp.presentations.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cashierapp.domain.model.SelectItemModel
import cashierapp.presentations.ui.theme.BorderGray


fun Modifier.bottomBorder(color: Color, thickness: Float = 1f) = this.then(
    Modifier.drawBehind {
        val strokeWidth = thickness.dp.toPx()
        drawLine(
            color = color,
            start = Offset(0f, size.height - strokeWidth / 2),
            end = Offset(size.width, size.height - strokeWidth / 2),
            strokeWidth = strokeWidth
        )
    }
)

@Composable
fun SelectItem(
    title: String?,
    data: List<SelectItemModel>,
    onClick: (e: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        title?.takeIf { it.isNotEmpty() }?.let {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = it,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            data.forEach { it ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bottomBorder(BorderGray, 1f)
                        .clickable { onClick(it.name) }
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    it.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = it.name,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }

                    Text(
                        text = it.name,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}