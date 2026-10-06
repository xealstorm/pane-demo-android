package com.example.panedemo.nav

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class Nav3Navigator(
    private val backStack: NavBackStack<NavKey>,
    private val onExit: () -> Unit = {},
) {
    fun navigate(destination: NavKey) {
        backStack.remove(destination)
        backStack.add(destination)
    }

    fun navigateBack(): Boolean = if (backStack.size > 1) {
        backStack.removeLastOrNull()
        true
    } else {
        onExit()
        false
    }
}
