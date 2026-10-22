package com.ranielschneider.ritimofit.core.design.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun RitimoAmbientBackground(
    modifier: Modifier = Modifier,
    intensity: Float = 1f,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = RitimoTheme.colors.isDark
    val baseBackground = RitimoTheme.colors.background

    val primaryLimeLight = RitimoTheme.colors.primary.copy(alpha = 0.08f * intensity)
    val secondaryTechLight = RitimoTheme.colors.secondary.copy(alpha = 0.05f * intensity)
    val lightGraphiteCenter = Color(0xFFF1F3F5)
    val motionCurveColor = RitimoTheme.colors.primary.copy(alpha = 0.04f * intensity)

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val width = size.width
                val height = size.height

                drawRect(color = baseBackground)

                if (!isDark) {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                primaryLimeLight,
                                primaryLimeLight.copy(alpha = primaryLimeLight.alpha * 0.4f),
                                Color.Transparent
                            ),
                            center = Offset(width * 0.85f, height * 0.18f),
                            radius = width * 0.95f
                        )
                    )

                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                secondaryTechLight,
                                secondaryTechLight.copy(alpha = secondaryTechLight.alpha * 0.3f),
                                Color.Transparent
                            ),
                            center = Offset(width * 0.15f, height * 0.82f),
                            radius = width * 0.9f
                        )
                    )

                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                lightGraphiteCenter.copy(alpha = 0.4f),
                                Color.Transparent
                            ),
                            center = Offset(width * 0.5f, height * 0.45f),
                            radius = height * 0.6f
                        )
                    )

                    val path1 = Path().apply {
                        moveTo(-width * 0.2f, height * 0.35f)
                        cubicTo(
                            width * 0.3f, height * 0.2f,
                            width * 0.7f, height * 0.55f,
                            width * 1.2f, height * 0.4f
                        )
                    }

                    val path2 = Path().apply {
                        moveTo(-width * 0.1f, height * 0.68f)
                        cubicTo(
                            width * 0.4f, height * 0.5f,
                            width * 0.8f, height * 0.85f,
                            width * 1.3f, height * 0.7f
                        )
                    }

                    val sweepGradient1 = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            motionCurveColor,
                            motionCurveColor.copy(alpha = motionCurveColor.alpha * 0.3f),
                            Color.Transparent
                        ),
                        start = Offset(0f, height * 0.2f),
                        end = Offset(width, height * 0.6f)
                    )

                    val sweepGradient2 = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            secondaryTechLight.copy(alpha = secondaryTechLight.alpha * 0.5f),
                            Color.Transparent
                        ),
                        start = Offset(0f, height * 0.5f),
                        end = Offset(width, height * 0.9f)
                    )

                    drawPath(
                        path = path1,
                        brush = sweepGradient1,
                        style = Stroke(width = width * 0.25f)
                    )

                    drawPath(
                        path = path2,
                        brush = sweepGradient2,
                        style = Stroke(width = width * 0.18f)
                    )
                }
            },
        content = content
    )
}
