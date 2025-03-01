package cashierapp.presentations.ui.screens

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Order : Screen("order")
    object Settings : Screen("settings")
}