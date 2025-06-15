package org.thelauncher.sculptlauncher.backend.launcher

import android.content.res.AssetManager
import android.os.Bundle
import com.mojang.minecraftpe.MainActivity
import org.thelauncher.sculptlauncher.backend.launcher.util.NativeUtil

class GameController internal constructor(private val mAbstractMCPE: AbstractMCPE) {
    val gameAssets: AssetManager = mAbstractMCPE.minecraftInfo.assets

    fun doingWhenMCLaunching(activity: MainActivity, savedInstanceState: Bundle?) {
        GameAssetOverrider.addAssetOverride(
            activity.assets,
            mAbstractMCPE.minecraftInfo.minecraftPackageContext.packageResourcePath
        )
        NativeUtil.setValues(activity)
        // TODO: 对于关闭安全模式的，加载nMOD资产
    }

    fun doingWhenMCFinished(activity: MainActivity) {
        // TODO: 对于关闭安全模式并加载了nMOD的，在游戏结束时执行的内容
    }
}