package com.example.panedemo.panecomparison.nav3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute : NavKey {
    @Serializable
    data object List : NavRoute

    @Serializable
    data object Product : NavRoute

    @Serializable
    data object Message : NavRoute
}
