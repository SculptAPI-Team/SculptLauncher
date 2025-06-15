package org.thelauncher.sculptlauncher

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import org.thelauncher.sculptlauncher.backend.launcher.AbstractMCPE

class LauncherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        mAbstractMCPE = AbstractMCPE(this)
    }

    override fun getAssets(): AssetManager {
        return mAbstractMCPE.minecraftInfo.assets
    }

    companion object {
        lateinit var mAbstractMCPE: AbstractMCPE
        @SuppressLint("StaticFieldLeak") lateinit var context: Context
    }
}