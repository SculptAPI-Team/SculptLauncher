package com.mojang.minecraftpe;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Pair;

import com.appboy.ui.inappmessage.jsinterface.AppboyInAppMessageHtmlUserJavascriptInterface;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

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