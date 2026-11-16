package com.vp.week7soal2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tema warna gelap terinspirasi dari Spotify sesuai gambar soal
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1DB954),
    secondary = Color(0xFF1DB954),
    background = Color(0xFF0E0E0E),
    surface = Color(0xFF1C1C1E),
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun Week7soal2Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
