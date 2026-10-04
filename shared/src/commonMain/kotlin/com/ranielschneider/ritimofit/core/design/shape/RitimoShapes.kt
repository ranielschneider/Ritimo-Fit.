package com.ranielschneider.ritimofit.core.design.shape

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class RitimoShapes(
    val small: CornerBasedShape = RoundedCornerShape(8.dp),
    val medium: CornerBasedShape = RoundedCornerShape(16.dp),
    val large: CornerBasedShape = RoundedCornerShape(24.dp),
    val extraLarge: CornerBasedShape = RoundedCornerShape(32.dp),
    val pill: CornerBasedShape = RoundedCornerShape(100.dp)
)

val LocalRitimoShapes = staticCompositionLocalOf { RitimoShapes() }
