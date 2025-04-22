package cashierapp.presentations.ui.screens.home.components

import CartProduct
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cashierapp.data.resources.Resource
import cashierapp.presentations.ui.components.AsyncImage
import cashierapp.presentations.ui.theme.BorderGray
import cashierapp.presentations.ui.theme.PrimaryColor
import cashierapp.presentations.viewmodel.home.ProductViewModel
import cashierapp.utils.toRupiahFormat
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Minus
import com.composables.icons.lucide.Plus

@Composable
fun OrderSheet(
    viewModel: ProductViewModel,
    productId: String?,
    onSuccessfulAdd: () -> Unit
) {
    val defaultImage =
        "https://res.cloudinary.com/dxucl7cw6/image/upload/v1740777960/products/m0tthyioslkz5gu8kyes.jpg"

    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.fetchDetailProduct(productId)
        }
    }

    val productState by viewModel.detailProduct.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    val cartItem = cartItems.find { it.product.id == productId }

    var itemQty by remember { mutableIntStateOf(cartItem?.product?.qty ?: 1) }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            when (productState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .padding(horizontal = 20.dp),
                    ) {
                        AsyncImage(
                            imageUrl = productState.data?.image ?: defaultImage,
                            description = productState.data?.name,
                        )
                    }
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = (productState.data?.name + " - " + productState.data?.size),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                            )
                            Text(
                                text = productState.data?.price?.toRupiahFormat().toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )

                        }
                        Text(
                            text = productState.data?.description ?: "Description",
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                IconButton(
                                    modifier = Modifier
                                        .border(1.dp, color = BorderGray, shape = CircleShape)
                                        .size(36.dp),
                                    onClick = {
                                        if (itemQty != 0) itemQty--
                                    }
                                ) {
                                    Icon(
                                        imageVector = Lucide.Minus,
                                        contentDescription = "Decrease Quantity"
                                    )
                                }
                                Text(
                                    text = itemQty.toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                IconButton(
                                    modifier = Modifier
                                        .border(1.dp, color = BorderGray, shape = CircleShape)
                                        .size(36.dp),
                                    onClick = { itemQty++ }
                                ) {
                                    Icon(
                                        imageVector = Lucide.Plus,
                                        contentDescription = "Increase Quantity"
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    val product = productState.data?.let {
                                        CartProduct(
                                            id = it.id,
                                            name = it.name,
                                            price = it.price,
                                            description = it.description,
                                            size = it.size,
                                            type = it.type,
                                            image = it.image,
                                            qty = itemQty
                                        )
                                    }
                                    if (product != null && itemQty > 0) {
                                        viewModel.addToCart(itemQty, product)
                                        onSuccessfulAdd()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f),
                                contentPadding = PaddingValues(vertical = 14.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor
                                )

                            ) {
                                Icon(
                                    imageVector = Lucide.Plus,
                                    contentDescription = "Cart Icon"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Keranjang ${
                                        productState.data?.price?.times(itemQty)?.toRupiahFormat()
                                    }",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = productState.message ?: "Unknown error occured",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = {
                                    if (productId != null) {
                                        viewModel.fetchDetailProduct(productId)
                                    }
                                }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}