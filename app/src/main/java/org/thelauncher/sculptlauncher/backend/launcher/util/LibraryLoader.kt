package org.thelauncher.sculptlauncher.backend.launcher.util

import android.annotation.SuppressLint
import java.io.File

object LibraryLoader {
    private external fun nativeOnLauncherLoaded(libPath: String)
    private external fun nativeOnNModAPILoaded(libPath: String)

    @JvmStatic
    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadLauncher(mcLibsPath: String) {
        System.loadLibrary("sc_launcher")
        nativeOnLauncherLoaded("$mcLibsPath/libminecraftpe.so")
    }

    @JvmStatic
    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadFMod(mcLibsPath: String?) {
        System.load(File(mcLibsPath, "libfmod.so").absolutePath)
    }

    @JvmStatic
    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadMinecraftPE(mcLibsPath: String?) {
        System.load(File(mcLibsPath, "libminecraftpe.so").absolutePath)
    }

    @JvmStatic
    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadCppShared(mcLibsPath: String?) {
        System.load(File(mcLibsPath, "libc++_shared.so").absolutePath)
    }

    @JvmStatic
    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadNModAPI(mcLibsPath: String) {
        System.loadLibrary("sc_nmod")
        nativeOnNModAPILoaded("$mcLibsPath/libminecraftpe.so")
    }
}