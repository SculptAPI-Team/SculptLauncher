package com.mojang.minecraftpe;

interface CrashManagerOwner {
    SessionInfo findSessionInfoForCrash(CrashManager crashManager, String str);

    String getCachedDeviceId(CrashManager crashManager);

    void notifyCrashUploadCompleted(CrashManager crashManager, SessionInfo sessionInfo);
}