package cashierapp.presentations.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cashierapp.data.resources.Resource
import cashierapp.presentations.ui.components.CardItem
import cashierapp.presentations.ui.components.Header
import cashierapp.presentations.ui.components.Search
import cashierapp.presentations.ui.components.SortChips
import cashierapp.presentations.ui.screens.home.components.AddProductForm
import cashierapp.presentations.ui.screens.home.components.OrderSheet
import cashierapp.presentations.viewmodel.home.ProductViewModel
import cashierapp.utils.ScreenSize
import cashierapp.utils.rememberScreenSize
import cashierapp.utils.toRupiahFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ProductViewModel) {
    var addProductShow by remember { mutableStateOf(false) }
    var orderSheetShow by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var searchVal by remember { mutableStateOf(TextFieldValue("")) }
    var sortedValBy by remember { mutableStateOf("") }
    var productId by remember { mutableStateOf("") }

    val products by viewModel.products.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val screenSize = rememberScreenSize()
    val defaultImage =
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

    val sheetSize = when (screenSize) {
        ScreenSize.COMPACT -> screenHeight * 0.6f
        ScreenSize.MEDIUM -> screenHeight * 0.5f
        ScreenSize.EXPANDED -> screenHeight * 0.8f
    }

    val filteredProduct = products.data?.let { productList ->
        when {
            searchVal.text.isEmpty() && sortedValBy.isEmpty() -> productList
            sortedValBy.isNotEmpty() -> productList.filter {
                it.name.contains(sortedValBy, ignoreCase = true)
            }

            else -> productList.filter {
                it.name.contains(searchVal.text, ignoreCase = true)
            }
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
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
                        name = "Muhammad Askar",
                        onClick = { addProductShow = true }
                    )
                }
                item(span = { GridItemSpan(gridColumns) }) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Search(
                            value = searchVal,
                            onChange = { searchVal = it }
                        )
                        products.data?.let {
                            SortChips(
                                product = it,
                                onClick = { sorted ->
                                    sortedValBy = if (sorted == sortedValBy) "" else sorted
                                }
                            )
                        }
                    }
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
                        filteredProduct?.let {
                            items(it.size) { index ->
                                val product = it[index]
                                CardItem(
                                    title = product.name,
                                    size = product.size,
                                    price = product.price.toRupiahFormat(),
                                    product.image ?: defaultImage
                                ) {
//                                    navController.navigate(route = Screen.Order.route + "/${product.id}")
                                    productId = product.id
                                    orderSheetShow = true
                                }
                            }
                        }
                    }
                }


            }

//          Add Product Sheet
            if (addProductShow) {
                ModalBottomSheet(
                    onDismissRequest = { addProductShow = false },
                    sheetState = sheetState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sheetSize),
                    containerColor = Color.White
                ) {
                    AddProductForm(onSuccessfulAdd = {
                        scope.launch {
                            sheetState.hide()
                            addProductShow = false
                        }
                    })
                }
            }

//          Order Sheet
            if (orderSheetShow) {
                ModalBottomSheet(
                    onDismissRequest = {
                        orderSheetShow = false
                        productId = ""
                    },
                    sheetState = sheetState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sheetSize),
                    containerColor = Color.White
                ) {
                    OrderSheet(
                        viewModel = viewModel,
                        productId = productId,
                        onSuccessfulAdd = {
                            scope.launch {
                                sheetState.hide()
                                orderSheetShow = false
                            }
                        }
                    )
                }
            }
        }
    }
}