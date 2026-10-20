package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DzDarkColorScheme = darkColorScheme(
  primary = DzWhite,
  onPrimary = DzBlack,
  primaryContainer = DzCardElevated,
  onPrimaryContainer = DzWhite,
  secondary = DzTextSecondary,
  onSecondary = DzWhite,
  background = DzDarkBackground,
  onBackground = DzTextPrimary,
  surface = DzCardBackground,
  onSurface = DzTextPrimary,
  surfaceVariant = DzCardElevated,
  onSurfaceVariant = DzTextSecondary,
  outline = DzCardBorder,
  error = DzRedExpense,
  onError = DzWhite
)

val DzLightColorScheme = lightColorScheme(
  primary = DzBlack,
  onPrimary = DzWhite,
  primaryContainer = Color(0xFFF3F4F6),
  onPrimaryContainer = DzBlack,
  secondary = Color(0xFF4B5563),
  onSecondary = DzWhite,
  background = Color(0xFFF9FAFB),
  onBackground = Color(0xFF111827),
  surface = Color(0xFFFFFFFF),
  onSurface = Color(0xFF111827),
  surfaceVariant = Color(0xFFF3F4F6),
  onSurfaceVariant = Color(0xFF4B5563),
  outline = Color(0xFFE5E7EB),
  error = DzRedExpense,
  onError = DzWhite
)

@Composable
fun DzWalletTheme(
  darkTheme: Boolean = true, // Default to sleek dark mode as specified in the reference design
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DzDarkColorScheme else DzLightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  DzWalletTheme(darkTheme = darkTheme, content = content)
}
