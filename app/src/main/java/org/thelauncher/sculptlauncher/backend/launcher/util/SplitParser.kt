package org.thelauncher.sculptlauncher.backend.launcher.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import org.jetbrains.annotations.Contract
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

@SuppressLint("StaticFieldLeak")
object SplitParser {
    private val minecraftLibs = arrayOf("libminecraftpe.so", "libc++_shared.so", "libfmod.so")
    private val aBI: String = Build.SUPPORTED_ABIS[0]

    @SuppressLint("StaticFieldLeak")
    var mContext: Context? = null

    fun parse(context: Context) {
        mContext = context

        val lib = File(mContext!!.cacheDir.path + "/lib")
        if (!lib.exists()) {
            lib.mkdir()
        }

        val arm64 = File("$lib/${aBI}")
        if (!arm64.exists()) {
            arm64.mkdir()
        }

        try {
            if (isAppBundle) {
                val splitPath =
                    minecraftApplicationInfo.splitPublicSourceDirs?.let { listOf(*it) }
                        ?.get(0)
                val buffer = ByteArray(2048)
                for (so in minecraftLibs) {
                    val `is` =
                        ZipFile(splitPath).getInputStream(ZipEntry("lib/${aBI}/$so"))
                    val fos = FileOutputStream("$arm64/$so")
                    do {
                        val numbered = `is`.read(buffer)
                        if (numbered <= 0) {
                            break
                        }
                        fos.write(buffer, 0, numbered)
                    } while (true)
                    fos.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @get:Contract(pure = true)
    val isAppBundle: Boolean
        get() = minecraftContext.applicationInfo.splitPublicSourceDirs != null && minecraftContext.applicationInfo.splitPublicSourceDirs!!.isNotEmpty()

    private val minecraftApplicationInfo: ApplicationInfo
        get() {
            return mContext!!.packageManager.getPackageInfo(
                "com.mojang.minecraftpe",
                0
            ).applicationInfo!!
        }

    val minecraftContext: Context
        get() {
            return mContext!!.createPackageContext(
                "com.mojang.minecraftpe",
                Context.CONTEXT_IGNORE_SECURITY or Context.CONTEXT_INCLUDE_CODE
            )
        }
}