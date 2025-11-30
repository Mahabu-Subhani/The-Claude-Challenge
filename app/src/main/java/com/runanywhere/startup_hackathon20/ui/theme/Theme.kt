package com.runanywhere.startup_hackathon20.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// GridZero Tactical Color Scheme
private val GridZeroDarkColorScheme = darkColorScheme(
    primary = Color(0xFF00FF00),          // Tactical Green
    secondary = Color(0xFF4CAF50),        // Success Green
    tertiary = Color(0xFF03DAC5),         // Teal
    background = Color(0xFF0A0A0A),       // Almost Black
    surface = Color(0xFF1A1A1A),          // Dark Gray
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFF00FF00),
    onSurface = Color(0xFF00FF00),
    error = Color(0xFFB00020)             // Critical Red
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Startup_hackathon20Theme(
    darkTheme: Boolean = true, // GridZero is always dark (tactical theme)
    // Dynamic color disabled for GridZero - we need consistent tactical colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // GridZero uses a fixed dark tactical color scheme
    val colorScheme = GridZeroDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}