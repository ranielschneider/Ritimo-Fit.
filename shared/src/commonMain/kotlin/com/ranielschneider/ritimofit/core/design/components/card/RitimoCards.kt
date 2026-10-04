package com.ranielschneider.ritimofit.core.design.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.glass.GlassSurface
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun RitimoStandardCard(
    modifier: Modifier = Modifier,
    shape: Shape = RitimoTheme.shapes.medium,
    containerColor: Color = RitimoTheme.colors.surface,
    borderColor: Color = RitimoTheme.colors.outlineVariant,
    padding: Dp = RitimoTheme.spacing.md,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            border = BorderStroke(1.dp, borderColor),
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {
            Box(modifier = Modifier.padding(padding), content = content)
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            border = BorderStroke(1.dp, borderColor),
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {
            Box(modifier = Modifier.padding(padding), content = content)
        }
    }
}

@Composable
fun RitimoElevatedCard(
    modifier: Modifier = Modifier,
    shape: Shape = RitimoTheme.shapes.medium,
    containerColor: Color = RitimoTheme.colors.surfaceElevated,
    padding: Dp = RitimoTheme.spacing.md,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    if (onClick != null) {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.padding(padding), content = content)
        }
    } else {
        ElevatedCard(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.padding(padding), content = content)
        }
    }
}

@Composable
fun RitimoGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RitimoTheme.shapes.medium,
    backgroundColor: Color = RitimoTheme.colors.glassBackground,
    borderColor: Color = RitimoTheme.colors.glassBorder,
    padding: Dp = RitimoTheme.spacing.md,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    GlassSurface(
        modifier = cardModifier,
        shape = shape,
        backgroundColor = backgroundColor,
        borderColor = borderColor
    ) {
        Box(modifier = Modifier.padding(padding), content = content)
    }
}
