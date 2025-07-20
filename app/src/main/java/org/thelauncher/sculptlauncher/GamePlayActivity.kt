package org.thelauncher.sculptlauncher

import android.os.Bundle
import com.mojang.minecraftpe.MainActivity
import org.thelauncher.sculptlauncher.backend.launcher.AbstractMCPE

class GamePlayActivity : MainActivity() {
    private fun getPESdk(): AbstractMCPE = LauncherApp.Companion.mAbstractMCPE

    override fun onCreate(bundle: Bundle?) {
        getPESdk().gameManager.doingWhenMCLaunching(this, bundle)
        super.onCreate(bundle)
    }
}