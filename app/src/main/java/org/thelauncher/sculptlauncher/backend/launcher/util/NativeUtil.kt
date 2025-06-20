package org.thelauncher.sculptlauncher.backend.launcher.util

import android.content.Context
import java.io.File

object NativeUtil {
    init {
        nativeRegisterNatives(NativeUtil::class.java)
    }

    //external fun nativeSetDataDirectory(directory: String?)
    external fun nativeDemangle(symbol_name: String?): String?
    external fun nativeRegisterNatives(cls: Class<*>?): Boolean

    @JvmStatic
    fun setValues(context: Context) {
        //nativeSetDataDirectory(context.filesDir.absolutePath + File.separator)
        //nativeSetDataDirectory("${context.getExternalFilesDir("game")}" + File.separator)
    }
}