package com.example.lacantera.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = LcNavy,
    onPrimary = LcWhite,

    primaryContainer = LcBlueSoft,
    onPrimaryContainer = LcNavy,

    secondary = LcRed,
    onSecondary = LcWhite,

    secondaryContainer = LcRedSoft,
    onSecondaryContainer = LcRed,

    tertiary = LcGreen,
    onTertiary = LcWhite,

    tertiaryContainer = LcGreenSoft,
    onTertiaryContainer = LcGreen,

    background = LcBackground,
    onBackground = LcTextPrimary,

    surface = LcSurface,
    onSurface = LcTextPrimary,

    surfaceVariant = LcSurfaceSoft,
    onSurfaceVariant = LcTextSecondary,

    outline = LcBorder,
    outlineVariant = LcBorder,

    error = Color(0xFFB42318),
    onError = LcWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB8CAFF),
    onPrimary = LcNavyDark,

    primaryContainer = LcNavyLight,
    onPrimaryContainer = LcWhite,

    secondary = Color(0xFFFFB3B4),
    onSecondary = Color(0xFF68000B),

    tertiary = Color(0xFF81D8A7),
    onTertiary = Color(0xFF00391D),

    background = LcNavyDark,
    onBackground = LcWhite,

    surface = Color(0xFF0B1735),
    onSurface = LcWhite,

    surfaceVariant = Color(0xFF152345),
    onSurfaceVariant = Color(0xFFD0D5DD),

    outline = Color(0xFF475467)
)

private val LaCanteraShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun LaCanteraTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        },
        typography = Typography,
        shapes = LaCanteraShapes,
        content = content
    )
}