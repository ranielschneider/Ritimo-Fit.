package com.ranielschneider.ritimofit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform