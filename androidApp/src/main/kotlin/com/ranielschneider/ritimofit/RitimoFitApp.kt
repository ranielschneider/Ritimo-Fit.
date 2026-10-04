package com.ranielschneider.ritimofit

import android.app.Application
import com.ranielschneider.ritimofit.di.initKoin
import org.koin.android.ext.koin.androidContext

class RitimoFitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@RitimoFitApp)
        }
    }
}
