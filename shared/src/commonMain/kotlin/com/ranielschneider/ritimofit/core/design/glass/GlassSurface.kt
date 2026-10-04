package com.ranielschneider.ritimofit.core.design.glass

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RitimoTheme.shapes.large,
    backgroundColor: Color = RitimoTheme.colors.glassBackground,
    borderColor: Color = RitimoTheme.colors.glassBorder,
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val borderGradient = Brush.verticalGradient(
        colors = listOf(
            borderColor,
            borderColor.copy(alpha = borderColor.alpha * 0.2f)
        )
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape)
            .border(BorderStroke(borderWidth, borderGradient), shape),
        content = content
    )
}
