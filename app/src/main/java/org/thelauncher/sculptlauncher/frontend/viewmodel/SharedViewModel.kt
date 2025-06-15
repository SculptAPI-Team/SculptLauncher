package org.thelauncher.sculptlauncher.frontend.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.thelauncher.sculptlauncher.LauncherApp

class SharedViewModel : ViewModel() {
    var shouldShowUI: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        shouldShowUI.update { checkInstalled() }
    }

    private fun checkInstalled(): Boolean {
        return try {
            LauncherApp.mAbstractMCPE.minecraftInfo
                .minecraftPackageContext.packageName == "com.mojang.minecraftpe"
        } catch (_: Exception) {
            false
        }
    }
}