package com.example.ui.theme

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

private val DarkColorScheme =
  darkColorScheme(
      primary = NeonCyan,
      secondary = NeonGreen,
      tertiary = NeonPurple,
      background = DarkBackground,
      surface = SurfaceDark,
      surfaceVariant = SurfaceVariantDark,
      onPrimary = Color.Black,
      onSecondary = Color.Black,
      onTertiary = Color.White,
      onBackground = TextPrimary,
      onSurface = TextPrimary,
      onSurfaceVariant = TextSecondary
  )

private val LightColorScheme =
  lightColorScheme(
      primary = LightCyan,
      secondary = LightGreen,
      tertiary = LightPurple,
      background = LightBackground,
      surface = SurfaceLight,
      surfaceVariant = SurfaceVariantLight,
      onPrimary = Color.White,
      onSecondary = Color.White,
      onTertiary = Color.White,
      onBackground = TextPrimaryLight,
      onSurface = TextPrimaryLight,
      onSurfaceVariant = TextSecondaryLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false, // Disabled dynamic color to force our awesome neon theme
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
