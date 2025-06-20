package org.thelauncher.sculptlauncher.frontend.activity

import android.os.Bundle
import com.mojang.minecraftpe.MainActivity
import org.thelauncher.sculptlauncher.LauncherApp
import org.thelauncher.sculptlauncher.backend.launcher.AbstractMCPE
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class MinecraftActivity : MainActivity() {
    private fun getPESdk(): AbstractMCPE = LauncherApp.Companion.mAbstractMCPE

    override fun onCreate(savedInstanceState: Bundle?) {
        getPESdk().gameManager.doingWhenMCLaunching(this, savedInstanceState)
        super.onCreate(savedInstanceState)
    }
}