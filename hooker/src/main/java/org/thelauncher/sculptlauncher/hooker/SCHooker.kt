package org.thelauncher.sculptlauncher.hooker

import com.bytedance.shadowhook.ShadowHook

class SCHooker {
    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("sc_hooker")
            ShadowHook.init(
                ShadowHook.ConfigBuilder()
                    .setMode(ShadowHook.Mode.UNIQUE)
                    .setDebuggable(true)
                    .build()
            )
        }
    }
}