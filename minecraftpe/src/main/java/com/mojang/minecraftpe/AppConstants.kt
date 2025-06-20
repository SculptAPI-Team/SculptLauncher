package com.mojang.minecraftpe

import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.util.Log

object AppConstants {
    var ANDROID_BUILD: String? = null

    @JvmField
    var ANDROID_VERSION: String? = null

    @JvmField
    var APP_PACKAGE: String? = null

    @JvmField
    var APP_VERSION: Int = 0

    @JvmField
    var APP_VERSION_NAME: String? = null

    @JvmField
    var PHONE_MANUFACTURER: String? = null

    @JvmField
    var PHONE_MODEL: String? = null
    private val loadIdentifiersTask: AsyncTask<Void?, Any?, String?>? = null

    @JvmStatic
    fun loadFromContext(context: Context) {
        Log.i("SculptLauncher", "CrashManager: AppConstants loadFromContext started")
        ANDROID_VERSION = Build.VERSION.RELEASE
        ANDROID_BUILD = Build.DISPLAY
        PHONE_MODEL = Build.MODEL
        PHONE_MANUFACTURER = Build.MANUFACTURER
        loadPackageData(context)
    }

    private fun loadPackageData(context: Context) {
        try {
            val packageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            APP_PACKAGE = packageInfo.packageName
            APP_VERSION = packageInfo.versionCode
            APP_VERSION_NAME = packageInfo.versionName
            Log.i("SculptLauncher", "CrashManager: AppConstants loadFromContext finished succesfully")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.w(
                "SculptLauncher",
                "CrashManager: Exception thrown when accessing the package info",
                e
            )
        }
    }
}