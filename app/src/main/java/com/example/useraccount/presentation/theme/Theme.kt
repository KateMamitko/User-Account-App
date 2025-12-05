package com.example.useraccount.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    onSurface = Color.Black,
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFFFFBFE),
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    onSurface = Color(0xFF4DC7FF),
    background = Color(0xFFFCF6FC),
    onBackground = Color(0xFF1C1B1F),
)

val LocalGradientColors = staticCompositionLocalOf {
    AppGradientColors(
        gradientStart = Color.Unspecified,
        gradientEnd = Color.Unspecified
    )
}

private val LightGradients = AppGradientColors(
    gradientStart = GradientLight1,
    gradientEnd = GradientLight2
)

private val DarkGradients = AppGradientColors(
    gradientStart = GradientDark1,
    gradientEnd = GradientDark2
)

@Composable
fun UserAccountTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val gradients = if (darkTheme) DarkGradients else LightGradients

    CompositionLocalProvider(LocalGradientColors provides gradients) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


