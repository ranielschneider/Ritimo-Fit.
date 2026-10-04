package com.ranielschneider.ritimofit

import androidx.compose.ui.window.ComposeUIViewController
import com.ranielschneider.ritimofit.di.initKoinIos

fun MainViewController() = ComposeUIViewController {
    App()
}

fun startKoinIos() = initKoinIos()
