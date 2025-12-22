package com.chamcoach.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LocalChamCoachiColors = staticCompositionLocalOf { ChamCoachiColors }

object ChamCoachiTheme {
    val colors: ChamCoachiColors
        @Composable
        get() = LocalChamCoachiColors.current
}

@Composable
fun ChamCoachiTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = ChamCoachiColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    CompositionLocalProvider(
        LocalChamCoachiColors provides colorScheme,
        content = content
    )
}