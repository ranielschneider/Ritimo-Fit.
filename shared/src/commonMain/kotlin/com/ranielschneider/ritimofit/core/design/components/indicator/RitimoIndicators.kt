package com.ranielschneider.ritimofit.core.design.components.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun RitimoCircularProgress(
    modifier: Modifier = Modifier,
    color: Color = RitimoTheme.colors.primary,
    trackColor: Color = RitimoTheme.colors.surfaceVariant,
    size: Dp = 40.dp,
    strokeWidth: Dp = 4.dp
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        trackColor = trackColor,
        strokeWidth = strokeWidth,
        strokeCap = StrokeCap.Round
    )
}

@Composable
fun RitimoLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = RitimoTheme.colors.primary,
    trackColor: Color = RitimoTheme.colors.surfaceVariant,
    height: Dp = 8.dp
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RitimoTheme.shapes.pill),
        color = color,
        trackColor = trackColor,
        strokeCap = StrokeCap.Round
    )
}

enum class BadgeType {
    PRIMARY,
    SUCCESS,
    WARNING,
    ERROR,
    NEUTRAL
}

@Composable
fun RitimoStatusBadge(
    text: String,
    type: BadgeType = BadgeType.PRIMARY,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (type) {
        BadgeType.PRIMARY -> RitimoTheme.colors.primaryContainer to RitimoTheme.colors.onPrimaryContainer
        BadgeType.SUCCESS -> RitimoTheme.colors.success.copy(alpha = 0.2f) to RitimoTheme.colors.success
        BadgeType.WARNING -> RitimoTheme.colors.warning.copy(alpha = 0.2f) to RitimoTheme.colors.warning
        BadgeType.ERROR -> RitimoTheme.colors.errorContainer to RitimoTheme.colors.error
        BadgeType.NEUTRAL -> RitimoTheme.colors.surfaceVariant to RitimoTheme.colors.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RitimoTheme.shapes.pill)
            .background(backgroundColor)
            .padding(horizontal = RitimoTheme.spacing.sm, vertical = RitimoTheme.spacing.xxs),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = RitimoTheme.typography.labelSmall,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
fun RitimoLoadingIndicator(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        RitimoCircularProgress()
        if (message != null) {
            Text(
                text = message,
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant,
                modifier = Modifier.padding(top = RitimoTheme.spacing.md)
            )
        }
    }
}
