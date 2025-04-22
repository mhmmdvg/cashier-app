package cashierapp.presentations.ui.screens.order.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cashierapp.presentations.ui.theme.BorderGray
import cashierapp.utils.toRupiahFormat

@Composable
fun OrderBottomBar(
    priceTotal: Int?,
    onCheckout: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier.border(
            width = 1.dp,
            color = BorderGray
        ),
        containerColor = Color.White,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box {
                    Column {
                        Text(
                            text = "Total",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = priceTotal?.toRupiahFormat() ?: "Rp0",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Button(
                    onClick = onCheckout
                ) {
                    Text(
                        text = "Checkout",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}