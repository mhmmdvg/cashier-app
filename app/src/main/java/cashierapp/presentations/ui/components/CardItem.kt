package cashierapp.presentations.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardItem(title: String, price: String, image: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(18)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color.Gray)
            ) {
                AsyncImage(
                    imageUrl = image,
                    description = title,
                )
            }

            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Text(price, fontWeight = FontWeight.Normal, fontSize = 18.sp)
        }
    }

}


@Preview
@Composable
fun CardPreview() {
    CardItem(
        "Item Name",
        "Rp.20.000",
        "https://res.cloudinary.com/dxucl7cw6/image/upload/v1740777960/products/m0tthyioslkz5gu8kyes.jpg"
    ) {
        print("test")
    }
}