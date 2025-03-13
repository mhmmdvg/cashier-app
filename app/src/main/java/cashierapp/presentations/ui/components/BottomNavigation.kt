package cashierapp.presentations.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import cashierapp.domain.model.NavigationItem
import cashierapp.presentations.ui.screens.Screen
import cashierapp.presentations.ui.theme.PrimaryColor
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings

@Composable
fun BottomNavigation(navController: NavController) {
    val selectedNavigationIndex = rememberSaveable { mutableIntStateOf(0) }

    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            icon = Lucide.House,
            route = Screen.Home.route
        ),
        NavigationItem(
            title = "Settings",
            icon = Lucide.Settings,
            route = Screen.Settings.route
        )
    )

    NavigationBar(
        containerColor = Color.White,
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (index == selectedNavigationIndex.intValue) PrimaryColor
                        else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


