package cashierapp.presentations.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cashierapp.presentations.ui.theme.BorderGray


@Composable
fun Header(name: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val context = LocalContext.current

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = Color.Black, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.substring(0, 1),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp

            )
        }


        IconButton(
            modifier = Modifier
                .border(1.dp, color = BorderGray, shape = CircleShape)
                .size(36.dp),
            onClick = { onClick(context) }
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Menu")
        }
    }
}

fun onClick(context: Context) {
    Toast.makeText(context, "Test Click", Toast.LENGTH_SHORT).show()
}


@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Header("Muhammad Askar")
}
