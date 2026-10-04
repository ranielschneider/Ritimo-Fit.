package com.ranielschneider.ritimofit.core.design.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class RitimoColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val background: Color,
    val backgroundGradientTop: Color,
    val backgroundGradientBottom: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceElevated: Color,
    val glassBackground: Color,
    val glassBorder: Color,
    val outline: Color,
    val outlineVariant: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val success: Color,
    val warning: Color,
    val isDark: Boolean
)

val DarkRitimoColors = RitimoColors(
    primary = Color(0xFFD4FF00), // Lime Tech
    onPrimary = Color(0xFF0E1014),
    primaryContainer = Color(0xFF222B00),
    onPrimaryContainer = Color(0xFFE2FF66),
    secondary = Color(0xFF38BDF8),
    onSecondary = Color(0xFF0E1014),
    background = Color(0xFF0E1014), // Deep Charcoal
    backgroundGradientTop = Color(0xFF14171E),
    backgroundGradientBottom = Color(0xFF0A0C0F),
    onBackground = Color(0xFFF3F4F6),
    surface = Color(0xFF15181E), // Dark Neutral Surface
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF1E222A), // Lighter Graphite
    onSurfaceVariant = Color(0xFF8E95A2),
    surfaceElevated = Color(0xFF252A34),
    glassBackground = Color(0x331E232C), // Sleek translucent dark glass
    glassBorder = Color(0x26FFFFFF), // Subtle translucent white border
    outline = Color(0xFF323844),
    outlineVariant = Color(0xFF222630),
    error = Color(0xFFEF4444),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF450A0A),
    success = Color(0xFF22C55E),
    warning = Color(0xFFF59E0B),
    isDark = true
)

val LightRitimoColors = RitimoColors(
    primary = Color(0xFF5B8C00), // Controlled Lime Green
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE5F8A6),
    onPrimaryContainer = Color(0xFF1E3500),
    secondary = Color(0xFF0284C7),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF8F9FA),
    backgroundGradientTop = Color(0xFFFFFFFF),
    backgroundGradientBottom = Color(0xFFF1F3F5),
    onBackground = Color(0xFF111827),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F2937),
    surfaceVariant = Color(0xFFE9ECEF),
    onSurfaceVariant = Color(0xFF6B7280),
    surfaceElevated = Color(0xFFFFFFFF),
    glassBackground = Color(0xE6FFFFFF),
    glassBorder = Color(0x1F000000),
    outline = Color(0xFFD1D5DB),
    outlineVariant = Color(0xFFE5E7EB),
    error = Color(0xFFDC2626),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFEE2E2),
    success = Color(0xFF16A34A),
    warning = Color(0xFFD97706),
    isDark = false
)

fun RitimoColors.toMaterialColorScheme(): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            error = error,
            onError = onError,
            errorContainer = errorContainer
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            error = error,
            onError = onError,
            errorContainer = errorContainer
        )
    }
}

val LocalRitimoColors = staticCompositionLocalOf { DarkRitimoColors }
