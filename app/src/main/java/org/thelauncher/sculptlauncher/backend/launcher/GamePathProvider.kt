package org.thelauncher.sculptlauncher.backend.launcher

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Build
import org.thelauncher.sculptlauncher.backend.launcher.util.SplitParser

class GamePathProvider(private val launcherContext: Context) {
    val minecraftPackageContext: Context
        get() {
            return try {
                launcherContext.createPackageContext(
                    "com.mojang.minecraftpe",
                    Context.CONTEXT_IGNORE_SECURITY or Context.CONTEXT_INCLUDE_CODE
                )
            } catch (_: PackageManager.NameNotFoundException) {
                launcherContext
            }
        }
//    val minecraftPackageContext: Context = launcherContext.createPackageContext(
//        "com.mojang.minecraftpe",
//        Context.CONTEXT_IGNORE_SECURITY or Context.CONTEXT_INCLUDE_CODE
//    )

    init {
        GameAssetOverrider.newInstance()
        GameAssetOverrider.instance.addAssetOverride(launcherContext.packageResourcePath)
    }

    val minecraftVersionName: String
        get() {
            return launcherContext.packageManager.getPackageInfo(
                minecraftPackageContext.packageName,
                PackageManager.GET_CONFIGURATIONS
            ).versionName.toString()
        }

    val minecraftPackageNativeLibraryDir: String
        get() = if (SplitParser.isAppBundle) {
            launcherContext.cacheDir.path + "/lib/" + Build.SUPPORTED_ABIS.first()
        } else {
            minecraftPackageContext.applicationInfo.nativeLibraryDir
        }

    val assetOverrideManager: GameAssetOverrider
        get() = GameAssetOverrider.instance

    val assets: AssetManager
        get() = assetOverrideManager.assetManager
}