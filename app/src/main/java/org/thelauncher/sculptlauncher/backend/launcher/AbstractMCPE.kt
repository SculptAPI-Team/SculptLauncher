package org.thelauncher.sculptlauncher.backend.launcher

import android.content.Context

class AbstractMCPE(context: Context) {
    val minecraftInfo: GamePathProvider = GamePathProvider(context)
    val gameManager: GameController = GameController(this)
}