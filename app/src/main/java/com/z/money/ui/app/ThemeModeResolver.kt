package com.z.money.ui.app

import com.z.money.data.ThemeMode

fun ThemeMode.resolveDarkTheme(systemInDarkTheme: Boolean): Boolean {
    return when (this) {
        ThemeMode.System -> systemInDarkTheme
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
}
