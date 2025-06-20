package com.mojang.minecraftpe

interface CrashManagerOwner {
    fun findSessionInfoForCrash(crashManager: CrashManager?, str: String?): SessionInfo?

    fun getCachedDeviceId(crashManager: CrashManager?): String?

    fun notifyCrashUploadCompleted(crashManager: CrashManager?, sessionInfo: SessionInfo?)
}