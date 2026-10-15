package com.ranielschneider.ritimofit.core.design.components.indicator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun RitimoWaterBottle(
    consumedMl: Int,
    dailyGoalMl: Int,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    val remainingMl = (dailyGoalMl - consumedMl).coerceAtLeast(0)
    val fillFraction = (remainingMl.toFloat() / dailyGoalMl.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val animatedFillFraction by animateFloatAsState(
        targetValue = fillFraction,
        animationSpec = tween(durationMillis = 600)
    )

    val consumedPercentage = ((consumedMl.toFloat() / dailyGoalMl.coerceAtLeast(1).toFloat()) * 100).toInt()

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = this.size.width
            val height = this.size.height

            val capWidth = width * 0.35f
            val capHeight = height * 0.08f
            val capLeft = (width - capWidth) / 2f

            val neckWidth = width * 0.45f
            val neckHeight = height * 0.10f
            val neckLeft = (width - neckWidth) / 2f

            val bodyLeft = width * 0.15f
            val bodyWidth = width * 0.70f
            val bodyTop = capHeight + neckHeight
            val bodyHeight = height - bodyTop - 8.dp.toPx()

            // 1. Draw Cap
            drawRoundRect(
                color = Color(0xFFD4FF00),
                topLeft = Offset(capLeft, 4.dp.toPx()),
                size = Size(capWidth, capHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // 2. Draw Bottle Outline Path
            val bottleTopY = capHeight + 4.dp.toPx()
            val bottleBottomY = bodyTop + bodyHeight + 8.dp.toPx()
            val totalBottleHeight = bottleBottomY - bottleTopY

            val bottlePath = Path().apply {
                moveTo(neckLeft, bottleTopY)
                lineTo(neckLeft + neckWidth, bottleTopY)
                lineTo(bodyLeft + bodyWidth, bodyTop)
                lineTo(bodyLeft + bodyWidth, bodyTop + bodyHeight)
                quadraticTo(
                    bodyLeft + bodyWidth, bottleBottomY,
                    bodyLeft + bodyWidth - 12.dp.toPx(), bottleBottomY
                )
                lineTo(bodyLeft + 12.dp.toPx(), bottleBottomY)
                quadraticTo(
                    bodyLeft, bottleBottomY,
                    bodyLeft, bodyTop + bodyHeight
                )
                lineTo(bodyLeft, bodyTop)
                close()
            }

            // Glass Bottle Background
            drawPath(
                path = bottlePath,
                color = Color(0xFF1E222A).copy(alpha = 0.8f)
            )

            // 3. Draw Water Liquid Fill (Remaining level from top of neck to bottom)
            val liquidTop = bottleTopY + (totalBottleHeight * (1f - animatedFillFraction))
            val fillHeightPx = bottleBottomY - liquidTop

            if (fillHeightPx > 0) {
                val waterBrush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00E5FF),
                        Color(0xFF00838F)
                    )
                )

                clipPath(bottlePath) {
                    drawRect(
                        brush = waterBrush,
                        topLeft = Offset(0f, liquidTop),
                        size = Size(width, fillHeightPx)
                    )
                }
            }

            // 4. Bottle Outer Glass Border
            drawPath(
                path = bottlePath,
                color = Color(0xFFD4FF00).copy(alpha = 0.6f),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Percentage Text Center Overlay
        Text(
            text = "$consumedPercentage%",
            style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = if (animatedFillFraction > 0.45f) Color.Black else RitimoTheme.colors.onSurface
        )
    }
}
