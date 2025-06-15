package org.thelauncher.sculptlauncher.backend

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRouter() {
    @Serializable
    data object Main: AppRouter()
}