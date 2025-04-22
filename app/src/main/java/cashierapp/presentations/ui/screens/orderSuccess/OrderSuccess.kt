package cashierapp.presentations.ui.screens.orderSuccess

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cashierapp.presentations.ui.components.LottieSuccess
import cashierapp.presentations.viewmodel.home.ProductViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay

@Composable
fun OrderSuccess(
    navController: NavController,
    viewModel: ProductViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.clearProductCart()
        delay(1500)
        navController.navigate("home") {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieSuccess()

            Text(
                text = "Checkout Success",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}