package cashierapp.presentations.ui.screens.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cashierapp.data.resources.Resource
import cashierapp.presentations.ui.components.AsyncImage
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Minus
import com.composables.icons.lucide.Plus
import com.example.cashierapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(viewModel: DetailProductViewModel = hiltViewModel(), productId: String?, onNavigateBack: () -> Unit) {
    var qty by remember { mutableIntStateOf(1) }

    val defaultImage =
        "https://res.cloudinary.com/dxucl7cw6/image/upload/v1740777960/products/m0tthyioslkz5gu8kyes.jpg"


    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.fetchDetailProduct(productId)
        }
    }

    val productState by viewModel.detailProduct.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0f),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text("")
                },
                modifier = Modifier.statusBarsPadding(),
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
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
            BottomAppBar(
                containerColor = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        IconButton(
                            onClick = { if (qty > 0) qty-- }
                        ) {
                            Icon(
                                imageVector = Lucide.Minus,
                                contentDescription = "Decrease Quantity"
                            )
                        }
                        Text(
                            text = qty.toString(),
                            fontSize = 32.sp
                        )
                        IconButton(
                            onClick = { qty++ }
                        ) {
                            Icon(
                                imageVector = Lucide.Plus,
                                contentDescription = "Increase Quantity"
                            )
                        }
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { println("test") }
                    ) {
                        Icon(
                            imageVector = Lucide.Plus,
                            contentDescription = "Cart Icon"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Keranjang ${productState.data?.price}",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = innerPadding.calculateBottomPadding()
                ),
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
                            .height(450.dp),
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
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = productState.data?.name ?: "Coffee",
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                            Text(
                                text = ("Rp." + productState.data?.price.toString()) ?: "0",
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )

                        }
                        Text(
                            text = productState.data?.description ?: "Description",
                            fontSize = 24.sp
                        )
                        HorizontalDivider(
                            color = Color.Black
                        )

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


@Preview
@Composable
fun OrderPreview() {
    OrderScreen(productId = "1234", onNavigateBack = { println("debug") })
}