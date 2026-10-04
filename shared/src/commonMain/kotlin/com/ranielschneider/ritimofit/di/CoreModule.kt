package com.ranielschneider.ritimofit.di

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.dispatcher.DefaultAppDispatchers
import org.koin.dsl.module

val coreModule = module {
    single<AppDispatchers> { DefaultAppDispatchers() }
}
