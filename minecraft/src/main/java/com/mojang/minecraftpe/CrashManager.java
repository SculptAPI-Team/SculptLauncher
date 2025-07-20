package com.mojang.minecraftpe;

public class CrashManager {
    private String mCrashDumpFolder;
    private String mCrashUploadURI;
    private String mCrashUploadURIWithSentryKey;
    private String mCurrentSessionId;
    private String mExceptionUploadURI;
    private Thread.UncaughtExceptionHandler mPreviousUncaughtExceptionHandler = null;

    private static native String nativeNotifyUncaughtException();

    private String uploadCrashFile(String filePath, String sessionID, String sentryParametersJSON){
        return "";
    }

    public CrashManager(String crashDumpFolder, String currentSessionId, SentryEndpointConfig sentryEndpointConfig) {
        this.mCrashUploadURI = null;
        this.mCrashUploadURIWithSentryKey = null;
        this.mExceptionUploadURI = null;
        this.mCrashDumpFolder = null;
        this.mCurrentSessionId = null;
        this.mCrashDumpFolder = crashDumpFolder;
        this.mCurrentSessionId = currentSessionId;
        this.mCrashUploadURI = sentryEndpointConfig.url + "/api/" + sentryEndpointConfig.projectId + "/minidump/";
        StringBuilder sb = new StringBuilder();
        sb.append(this.mCrashUploadURI);
        sb.append("?sentry_key=");
        sb.append(sentryEndpointConfig.publicKey);
        this.mCrashUploadURIWithSentryKey = sb.toString();
        this.mExceptionUploadURI = sentryEndpointConfig.url + "/api/" + sentryEndpointConfig.projectId + "/store/?sentry_version=7&sentry_key=" + sentryEndpointConfig.publicKey;
    }

    public void installGlobalExceptionHandler() {
        this.mPreviousUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: com.mojang.minecraftpe.CrashManager.1
            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread t, Throwable e) {
            }
        });
    }

    public String getCrashUploadURI() {
        return this.mCrashUploadURI;
    }

    public String getExceptionUploadURI() {
        return this.mExceptionUploadURI;
    }
}