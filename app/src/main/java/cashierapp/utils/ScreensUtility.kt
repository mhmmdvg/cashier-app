package cashierapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

enum class ScreenSize {
    COMPACT,    // Phone
    MEDIUM,     // Small tablet
    EXPANDED    // Large tablet
}

@Composable
fun rememberScreenSize(): ScreenSize {
    val configuration = LocalConfiguration.current
    val screenWidthDp = with(LocalDensity.current) {
        configuration.screenWidthDp.dp
    }

    return when {
        screenWidthDp < 600.dp -> ScreenSize.COMPACT
        screenWidthDp < 840.dp -> ScreenSize.MEDIUM
        else -> ScreenSize.EXPANDED
    }
}