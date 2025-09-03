package com.chamcoach.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LocalChamCoachColors = staticCompositionLocalOf { TamaColors }

object ChamCoachTheme {
    val colors: TamaColors
        @Composable
        get() = LocalChamCoachColors.current
}

@Composable
fun ChamCoachTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = TamaColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    CompositionLocalProvider(
        LocalChamCoachColors provides colorScheme,
        content = content
    )
}