package be.he2b.savory.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DefaultScheme = lightColorScheme(
    primary = TealBackground,
    secondary = HeartButtonColor,
    background = BackgroundColor,
    surface = BackgroundColor
)

private val ProtanScheme = lightColorScheme(
    primary = ProtanPrimary,
    secondary = ProtanAccent,
    tertiary = ProtanHeart,
    background = ProtanBackground,
    surface = ProtanBackground
)

private val DeuterScheme = lightColorScheme(
    primary = DeuterPrimary,
    secondary = DeuterAccent,
    tertiary = DeuterHeart,
    background = DeuterBackground,
    surface = DeuterBackground
)

private val TritanScheme = lightColorScheme(
    primary = TritanPrimary,
    secondary = TritanAccent,
    tertiary = TritanHeart,
    background = TritanBackground,
    surface = TritanBackground
)

@Composable
fun SavoryTheme(
    content: @Composable () -> Unit
) {
    // we observe the current mode in ThemeManager
    val colorScheme = when (ThemeManager.currentMode) {
        ColorBlindMode.Normal -> DefaultScheme
        ColorBlindMode.Protanopia -> ProtanScheme
        ColorBlindMode.Deuteranopia -> DeuterScheme
        ColorBlindMode.Tritanopia -> TritanScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}