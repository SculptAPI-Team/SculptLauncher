package org.thelauncher.sculptlauncher.frontend.activity

import android.os.Bundle
import com.mojang.minecraftpe.MainActivity
import org.thelauncher.sculptlauncher.LauncherApp
import org.thelauncher.sculptlauncher.backend.launcher.AbstractMCPE
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class MinecraftActivity : MainActivity() {
    private fun getPESdk(): AbstractMCPE = LauncherApp.Companion.mAbstractMCPE

    override fun onCreate(savedInstanceState: Bundle?) {
        getPESdk().gameManager.doingWhenMCLaunching(this, savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun getFileDataBytes(filename: String): ByteArray? {
        val asset = getPESdk().minecraftInfo.assets
        if (filename.startsWith("file:/")) {
            val needRead = filename.substring("file:/android_asset/".length)
            try {
                BufferedInputStream(asset.open(needRead)).use { bufferedInputStream ->
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(4096)

                    var bytesRead: Int
                    while ((bufferedInputStream.read(buffer).also { bytesRead = it }) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead)
                    }
                    return byteArrayOutputStream.toByteArray()
                }
            } catch (_: IOException) {
                System.err.println("无法通过assets读取这个文件，正在用回默认方法")
            }
        } else if (!filename.startsWith("/data") && !filename.startsWith("/")) {
            try {
                BufferedInputStream(asset.open(filename)).use { bufferedInputStream ->
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(4096)

                    var bytesRead: Int
                    while ((bufferedInputStream.read(buffer).also { bytesRead = it }) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead)
                    }
                    return byteArrayOutputStream.toByteArray()
                }
            } catch (_: IOException) {
                System.err.println("无法通过assets读取这个文件，正在用回默认方法")
            }
        }
        return super.getFileDataBytes(filename)
    }
}