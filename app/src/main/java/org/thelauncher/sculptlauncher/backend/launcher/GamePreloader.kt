package org.thelauncher.sculptlauncher.backend.launcher

import android.content.Context
import android.os.Bundle
import android.util.Log
import org.thelauncher.sculptlauncher.backend.launcher.util.LibraryLoader
import org.thelauncher.sculptlauncher.backend.launcher.util.SplitParser

class GamePreloader @JvmOverloads constructor(
    private val mAbstractMCPE: AbstractMCPE,
    private val preloadListener: GamePreloadListener = GamePreloadListener(),
) {
    val bundle = Bundle()

    fun preload(context: Context) {
        SplitParser.parse(context)
        val libPath = mAbstractMCPE.minecraftInfo.minecraftPackageNativeLibraryDir
        preloadListener.onStart()
        // Load native libs
        preloadListener.onLoadNativeLibs()
        // load fmod
        preloadListener.onLoadFModLib()
        LibraryLoader.loadFMod(libPath)
        // load cpp shared
        preloadListener.onLoadCppSharedLib()
        LibraryLoader.loadCppShared(libPath)
        // load game lib
        preloadListener.onLoadMinecraftPELib()
        LibraryLoader.loadMinecraftPE(libPath)
        // load launcher lib
        preloadListener.onLoadGameLauncherLib()
        LibraryLoader.loadLauncher(libPath)
        // load supporting lib
        preloadListener.onLoadPESdkLib()
        LibraryLoader.loadNModAPI(libPath)
        // finished
        preloadListener.onFinishedLoadingNativeLibs()
        // TODO: 在安全模式关闭下，加载nMod的库
        bundle.putString("nmod_data", null)
        // job done
        preloadListener.onFinish(bundle)
    }

    open class GamePreloadListener {
        open fun onStart() {
            Log.e(TAG, "onStart()")
        }

        open fun onLoadNativeLibs() {
            Log.e(TAG, "onLoadNativeLibs()")
        }

        open fun onLoadSubstrateLib() {
            Log.e(TAG, "onLoadSubstrateLib()")
        }

        open fun onLoadXHookLib() {
            Log.e(TAG, "onLoadXHookLib()")
        }

        open fun onLoadXHookSkyColorLib() {
            Log.e(TAG, "onLoadXHookSkyColorLib()")
        }

        fun onLoadGameLauncherLib() {
            Log.e(TAG, "onLoadGameLauncherLib()")
        }

        open fun onLoadFModLib() {
            Log.e(TAG, "onLoadFModLib()")
        }

        open fun onLoadMinecraftPELib() {
            Log.e(TAG, "onLoadMinecraftPELib()")
        }

        fun onLoadCppSharedLib() {
            Log.e(TAG, "onLoadCppSharedLib()")
        }

        open fun onLoadPESdkLib() {
            Log.e(TAG, "onLoadPESdkLib()")
        }

        open fun onFinishedLoadingNativeLibs() {
            Log.e(TAG, "onFinishedLoadingNativeLibs()")
        }

//    fun onStartLoadingAllNMods() {
//        Log.e(TAG, "onStartLoadingAllNMods()")
//    }

        //        public void onNModLoaded(NMod nmod) {
        //            Log.e(TAG, "onNModLoaded()");
        //        }
        //
        //        public void onFailedLoadingNMod(NMod nmod) {
        //            Log.e(TAG, "onFailedLoadingNMod()");
        //        }
//    fun onFinishedLoadingAllNMods() {
//        Log.e(TAG, "onFinishedLoadingAllNMods()")
//    }

        open fun onFinish(bundle: Bundle) {
            Log.e(TAG, "onFinish()")
        }

        companion object {
            const val TAG: String = "SculptLauncher-Preloader"
        }
    }
}