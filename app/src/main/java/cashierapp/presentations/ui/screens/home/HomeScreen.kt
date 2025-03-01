package cashierapp.presentations.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cashierapp.data.remote.local.TokenManager
import cashierapp.data.resources.Resource
import cashierapp.presentations.ui.components.CardItem
import cashierapp.presentations.ui.components.Header
import cashierapp.presentations.ui.components.Search
import cashierapp.presentations.ui.screens.Screen
import cashierapp.utils.ScreenSize
import cashierapp.utils.rememberScreenSize

@Composable
fun HomeScreen(viewModel: ProductViewModel = hiltViewModel(), navController: NavController) {
    val products by viewModel.products.collectAsState()
    val screenSize = rememberScreenSize()
    val defaultImage: String =
        "https://res.cloudinary.com/dxucl7cw6/image/upload/v1740777960/products/m0tthyioslkz5gu8kyes.jpg"

    val gridColumns = when (screenSize) {
        ScreenSize.COMPACT -> 2
        ScreenSize.MEDIUM -> 3
        ScreenSize.EXPANDED -> 4
    }

    val horizontalSpacing = when (screenSize) {
        ScreenSize.COMPACT -> 16.dp
        ScreenSize.MEDIUM -> 20.dp
        ScreenSize.EXPANDED -> 24.dp
    }

    val horizontalPadding = when (screenSize) {
        ScreenSize.COMPACT -> 20.dp
        ScreenSize.MEDIUM -> 24.dp
        ScreenSize.EXPANDED -> 32.dp
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            ) {
                item(span = { GridItemSpan(gridColumns) }) {
                    Header(
                        name = "Muhammad Askar"
                    )
                }
                item(span = { GridItemSpan(gridColumns) }) {
                    Search()
                }

                when (products) {
                    is Resource.Loading -> {
                        item(span = { GridItemSpan(gridColumns) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is Resource.Error -> {
                        item(span = { GridItemSpan(gridColumns) }) {
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
                                        text = products.message ?: "Unknown error occured",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Button(
                                        onClick = { viewModel.fetchProducts() }
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                    }

                    is Resource.Success -> {
                        products.data?.let {
                            items(it.size) { index ->
                                val product = it[index]
                                CardItem(title = product.name, "Rp.${product.price}", product.image ?: defaultImage) {
                                    navController.navigate(route = Screen.Order.route + "/${product.id}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}