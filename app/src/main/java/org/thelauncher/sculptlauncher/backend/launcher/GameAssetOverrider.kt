package org.thelauncher.sculptlauncher.backend.launcher

import android.content.res.AssetManager

class GameAssetOverrider private constructor() {
    val assetManager: AssetManager = AssetManager::class.java.newInstance()

    fun addAssetOverride(packageResourcePath: String) {
        val method =
            AssetManager::class.java.getMethod("addAssetPath", String::class.java)
        method.invoke(assetManager, packageResourcePath)
    }

    companion object {
        private var mInstance: GameAssetOverrider? = null

        @JvmStatic
        fun addAssetOverride(mgr: AssetManager, packageResourcePath: String) {
            val method =
                AssetManager::class.java.getMethod("addAssetPath", String::class.java)
            method.invoke(mgr, packageResourcePath)
        }

        @JvmStatic
        val instance: GameAssetOverrider
            get() {
                if (mInstance == null) return GameAssetOverrider().also {
                    mInstance = it
                }
                return mInstance!!
            }

        @JvmStatic
        fun newInstance() {
            mInstance = GameAssetOverrider()
        }
    }
}