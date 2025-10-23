/*package com.mustafa.myapplication.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.platform.LocalContext

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use custom purple colors
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}*/
package com.mustafa.myapplication.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = RedNetflix,
    onPrimary = White,
    primaryContainer = RedNetflix,
    onPrimaryContainer = White,
    secondary = RedNetflix,
    onSecondary = White,
    secondaryContainer = RedNetflix,
    onSecondaryContainer = White,
    tertiary = RedNetflix,
    onTertiary = White,
    background = LightGray,
    onBackground = Black,
    surface = LightGray,
    onSurface = Black,
    error = ErrorRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = RedNetflix,
    onPrimary = White,
    primaryContainer = RedNetflix,
    onPrimaryContainer = White,
    secondary = RedNetflix,
    onSecondary = White,
    secondaryContainer = RedNetflix,
    onSecondaryContainer = White,
    tertiary = RedNetflix,
    onTertiary = White,
    background = DarkGray,
    onBackground = White,
    surface = DarkGray,
    onSurface = White,
    error = ErrorRed,
    onError = White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Make status bar use our primary red
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
