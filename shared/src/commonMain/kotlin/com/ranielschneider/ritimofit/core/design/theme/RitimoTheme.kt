package com.ranielschneider.ritimofit.core.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.ranielschneider.ritimofit.core.design.color.DarkRitimoColors
import com.ranielschneider.ritimofit.core.design.color.LightRitimoColors
import com.ranielschneider.ritimofit.core.design.color.LocalRitimoColors
import com.ranielschneider.ritimofit.core.design.color.RitimoColors
import com.ranielschneider.ritimofit.core.design.color.toMaterialColorScheme
import com.ranielschneider.ritimofit.core.design.shape.LocalRitimoShapes
import com.ranielschneider.ritimofit.core.design.shape.RitimoShapes
import com.ranielschneider.ritimofit.core.design.spacing.LocalRitimoSpacing
import com.ranielschneider.ritimofit.core.design.spacing.RitimoSpacing
import com.ranielschneider.ritimofit.core.design.typography.LocalRitimoTypography
import com.ranielschneider.ritimofit.core.design.typography.RitimoTypography
import com.ranielschneider.ritimofit.core.design.typography.toMaterialTypography

@Composable
fun RitimoTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colors = if (isDark) DarkRitimoColors else LightRitimoColors
    val typography = RitimoTypography()
    val spacing = RitimoSpacing()
    val shapes = RitimoShapes()

    CompositionLocalProvider(
        LocalRitimoColors provides colors,
        LocalRitimoTypography provides typography,
        LocalRitimoSpacing provides spacing,
        LocalRitimoShapes provides shapes
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(),
            typography = typography.toMaterialTypography(),
            content = content
        )
    }
}

object RitimoTheme {
    val colors: RitimoColors
        @Composable
        @ReadOnlyComposable
        get() = LocalRitimoColors.current

    val typography: RitimoTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalRitimoTypography.current

    val spacing: RitimoSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalRitimoSpacing.current

    val shapes: RitimoShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalRitimoShapes.current
}
