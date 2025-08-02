package org.thelauncher.sculptlauncher.ui.router

import kotlinx.serialization.Serializable

@Serializable
sealed class RouterIndex {
    @Serializable object HomePage: RouterIndex()
}