package cashierapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cashierapp.data.remote.local.TokenManager
import cashierapp.presentations.ui.components.Header
import cashierapp.presentations.ui.screens.Screen
import cashierapp.presentations.ui.screens.auth.LoginScreen
import cashierapp.presentations.ui.screens.home.HomeScreen
import cashierapp.presentations.ui.screens.order.OrderScreen
import cashierapp.presentations.ui.theme.CashierAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CashierAppTheme {
                var startDestination by remember {
                    mutableStateOf(Screen.Login.route)
                }

                LaunchedEffect(Unit) {
                    val token = tokenManager.getToken()

                    println("is token $token")

                    startDestination = if (!token.isNullOrEmpty()) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }
                }

                NavGraph(startDestination = startDestination)
            }
        }
    }
}


@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            ) +  fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        }
        ) {

        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController
            )
        }

        composable(route = Screen.Order.route + "/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            OrderScreen(productId = productId, onNavigateBack = { navController.popBackStack() })
        }
    }
}

