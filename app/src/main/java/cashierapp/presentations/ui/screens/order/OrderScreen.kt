package cashierapp.presentations.ui.screens.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cashierapp.presentations.ui.components.AsyncImage
import cashierapp.presentations.ui.screens.Screen
import cashierapp.presentations.ui.screens.order.components.OrderBottomBar
import cashierapp.presentations.ui.theme.BorderGray
import cashierapp.presentations.viewmodel.home.ProductViewModel
import cashierapp.utils.toRupiahFormat
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Minus
import com.composables.icons.lucide.Plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    viewModel: ProductViewModel,
    navController: NavController,
    onNavigateBack: () -> Unit
) {
    val cartItem by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()

    val scrollState = rememberScrollState()

    val defaultImage =
        "https://res.cloudinary.com/dxucl7cw6/image/upload/v1740777960/products/m0tthyioslkz5gu8kyes.jpg"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Confirmation Order")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.background(
                            color = Color.White,
                            shape = CircleShape
                        )
                    ) {
                        Icon(
                            imageVector = Lucide.ArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        bottomBar = {
            OrderBottomBar(
                priceTotal = totalPrice,
                onCheckout = {
                    navController.navigate(Screen.OrderSuccess.route)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Order List",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )

            cartItem.forEach { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = BorderGray,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = item.product.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = item.product.size,
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.product.price.toRupiahFormat(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .border(1.dp, color = BorderGray, shape = CircleShape)
                                            .size(36.dp),
                                        onClick = { viewModel.decreaseProductQty(item.product.id) }
                                    ) {
                                        Icon(
                                            imageVector = Lucide.Minus,
                                            contentDescription = "Decrease Quantity"
                                        )
                                    }
                                    Text(
                                        text = item.product.qty.toString(),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    IconButton(
                                        modifier = Modifier
                                            .border(1.dp, color = BorderGray, shape = CircleShape)
                                            .size(36.dp),
                                        onClick = { viewModel.increaseProductQty(item.product.id) }
                                    ) {
                                        Icon(
                                            imageVector = Lucide.Plus,
                                            contentDescription = "Increase Quantity"
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .height(180.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = Color.Gray)
                        ) {
                            AsyncImage(
                                imageUrl = item.product.image ?: defaultImage,
                                description = item.product.name
                            )
                        }
                    }
                }
            }
        }
    }
}

