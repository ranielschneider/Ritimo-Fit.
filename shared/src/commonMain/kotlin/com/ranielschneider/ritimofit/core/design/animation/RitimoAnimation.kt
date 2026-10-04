package com.ranielschneider.ritimofit.core.design.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween

object RitimoAnimation {
    const val DurationFast = 150
    const val DurationNormal = 300
    const val DurationSlow = 500

    val FluidEasing: Easing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)
    val EmphasizedEasing: Easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    fun <T> fastSpec() = tween<T>(durationMillis = DurationFast, easing = FluidEasing)
    fun <T> normalSpec() = tween<T>(durationMillis = DurationNormal, easing = FluidEasing)
    fun <T> slowSpec() = tween<T>(durationMillis = DurationSlow, easing = EmphasizedEasing)
}
