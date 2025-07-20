package com.microsoft.xbox.telemetry.utc;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityManager;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.microsoft.xbox.idp.interop.Interop;
import com.microsoft.xbox.idp.interop.XboxLiveAppConfig;
import com.microsoft.xbox.telemetry.helpers.UTCLog;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class CommonData {
    private static final String DEFAULTSANDBOX = "RETAIL";
    private static final String DEFAULTSERVICES = "none";
    private static final String EVENTVERSION = "1.1";
    private static final String UNKNOWNAPP = "UNKNOWN";
    private static final String UNKNOWNUSER = "0";
    public String eventVersion;
    private static NetworkType netType = getNetworkConnection();
    private static String staticDeviceModel = getDeviceModel();
    private static String staticAppName = getAppName();
    private static String staticOSLocale = getDeviceLocale();
    private static String staticAccessibilityInfo = getAccessibilityInfo();
    private static UUID applicationSession = UUID.randomUUID();
    public String deviceModel = staticDeviceModel;
    public String xsapiVersion = "1.0";
    public String appName = staticAppName;
    public String clientLanguage = staticOSLocale;
    public int network = netType.getValue();
    public String sandboxId = getSandboxId();
    public String appSessionId = getApplicationSession();
    public String userId = UNKNOWNUSER;
    public String titleDeviceId = get_title_telemetry_device_id();
    public String titleSessionId = get_title_telemetry_session_id();
    public HashMap<String, Object> additionalInfo = new HashMap<>();
    private String accessibilityInfo = staticAccessibilityInfo;

    private static native String get_title_telemetry_device_id();

    private static native String get_title_telemetry_session_id();

    public CommonData(int i) {
        this.eventVersion = String.format("%s.%s", EVENTVERSION, Integer.valueOf(i));
    }

    public static String getApplicationSession() {
        return applicationSession.toString();
    }

    private static String getDeviceModel() {
        String str = Build.MODEL;
        return (str == null || str.isEmpty()) ? UNKNOWNAPP : str.replace("|", "");
    }

    private static String getAppName() {
        try {
            Context applicationContext = Interop.getApplicationContext();
            return applicationContext != null ? applicationContext.getApplicationInfo().packageName : UNKNOWNAPP;
        } catch (Exception e) {
            UTCLog.log(e.getMessage(), new Object[0]);
            return UNKNOWNAPP;
        }
    }

    private static String getDeviceLocale() {
        try {
            Locale locale = Locale.getDefault();
            return String.format("%s-%s", locale.getLanguage(), locale.getCountry());
        } catch (Exception e) {
            UTCLog.log(e.getMessage(), new Object[0]);
            return null;
        }
    }

    private static String getSandboxId() {
        try {
            return new XboxLiveAppConfig().getSandbox();
        } catch (Exception e) {
            UTCLog.log(e.getMessage(), new Object[0]);
            return DEFAULTSANDBOX;
        }
    }

    private enum NetworkType {
        UNKNOWN(0),
        WIFI(1),
        CELLULAR(2),
        WIRED(3);

        private int value = 0;

        public int getValue() {
            return this.value;
        }

        public void setValue(int i) {
            this.value = i;
        }

        NetworkType(int i) {
            setValue(i);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0048 A[Catch: Exception -> 0x004d, TRY_LEAVE, TryCatch #0 {Exception -> 0x004d, blocks: (B:6:0x000c, B:8:0x001e, B:10:0x0021, B:12:0x0029, B:20:0x0039, B:21:0x003e, B:22:0x0043, B:23:0x0048), top: B:29:0x000c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static com.microsoft.xbox.telemetry.utc.CommonData.NetworkType getNetworkConnection() {
        /*
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r0 = com.microsoft.xbox.telemetry.utc.CommonData.netType
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r1 = com.microsoft.xbox.telemetry.utc.CommonData.NetworkType.UNKNOWN
            if (r0 != r1) goto L5c
            android.content.Context r0 = com.microsoft.xbox.idp.interop.Interop.getApplicationContext()
            if (r0 == 0) goto L5c
            android.content.Context r0 = com.microsoft.xbox.idp.interop.Interop.getApplicationContext()     // Catch: java.lang.Exception -> L4d
            java.lang.String r1 = "connectivity"
            java.lang.Object r0 = r0.getSystemService(r1)     // Catch: java.lang.Exception -> L4d
            android.net.ConnectivityManager r0 = (android.net.ConnectivityManager) r0     // Catch: java.lang.Exception -> L4d
            android.net.NetworkInfo r0 = r0.getActiveNetworkInfo()     // Catch: java.lang.Exception -> L4d
            if (r0 != 0) goto L21
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r0 = com.microsoft.xbox.telemetry.utc.CommonData.netType     // Catch: java.lang.Exception -> L4d
            return r0
        L21:
            android.net.NetworkInfo$State r1 = r0.getState()     // Catch: java.lang.Exception -> L4d
            android.net.NetworkInfo$State r2 = android.net.NetworkInfo.State.CONNECTED     // Catch: java.lang.Exception -> L4d
            if (r1 != r2) goto L5c
            int r0 = r0.getType()     // Catch: java.lang.Exception -> L4d
            if (r0 == 0) goto L48
            r1 = 1
            if (r0 == r1) goto L43
            r1 = 6
            if (r0 == r1) goto L48
            r1 = 9
            if (r0 == r1) goto L3e
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r0 = com.microsoft.xbox.telemetry.utc.CommonData.NetworkType.UNKNOWN     // Catch: java.lang.Exception -> L4d
            com.microsoft.xbox.telemetry.utc.CommonData.netType = r0     // Catch: java.lang.Exception -> L4d
            goto L5c
        L3e:
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r0 = com.microsoft.xbox.telemetry.utc.CommonData.NetworkType.WIRED     // Catch: java.lang.Exception -> L4d
            com.microsoft.xbox.telemetry.utc.CommonData.netType = r0     // Catch: java.lang.Exception -> L4d
            goto L5c
        L43:
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r0 = com.microsoft.xbox.telemetry.utc.CommonData.NetworkType.WIFI     // Catch: java.lang.Exception -> L4d
            com.microsoft.xbox.telemetry.utc.CommonData.netType = r0     // Catch: java.lang.Exception -> L4d
            goto L5c
        L48:
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r0 = com.microsoft.xbox.telemetry.utc.CommonData.NetworkType.CELLULAR     // Catch: java.lang.Exception -> L4d
            com.microsoft.xbox.telemetry.utc.CommonData.netType = r0     // Catch: java.lang.Exception -> L4d
            goto L5c
        L4d:
            r0 = move-exception
            java.lang.String r0 = r0.getMessage()
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.microsoft.xbox.telemetry.helpers.UTCLog.log(r0, r1)
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r0 = com.microsoft.xbox.telemetry.utc.CommonData.NetworkType.UNKNOWN
            com.microsoft.xbox.telemetry.utc.CommonData.netType = r0
        L5c:
            com.microsoft.xbox.telemetry.utc.CommonData$NetworkType r0 = com.microsoft.xbox.telemetry.utc.CommonData.netType
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.xbox.telemetry.utc.CommonData.getNetworkConnection():com.microsoft.xbox.telemetry.utc.CommonData$NetworkType");
    }

    private static String getAccessibilityInfo() {
        try {
            Context applicationContext = Interop.getApplicationContext();
            if (applicationContext == null) {
                return "";
            }
            AccessibilityManager accessibilityManager = (AccessibilityManager) applicationContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
            HashMap map = new HashMap();
            map.put("isenabled", Boolean.valueOf(accessibilityManager.isEnabled()));
            String id = "none";
            for (AccessibilityServiceInfo accessibilityServiceInfo : accessibilityManager.getEnabledAccessibilityServiceList(-1)) {
                id = id.equals("none") ? accessibilityServiceInfo.getId() : id + String.format(";%s", accessibilityServiceInfo.getId());
            }
            map.put("enabledservices", id);
            return new GsonBuilder().serializeNulls().create().toJson(map);
        } catch (Exception e) {
            UTCLog.log(e.getMessage(), new Object[0]);
            return "";
        }
    }

    public String ToJson() {
        try {
            return new GsonBuilder().serializeNulls().create().toJson(this);
        } catch (JsonIOException e) {
            UTCLog.log("UTCJsonSerializer", "Error in json serialization" + e.toString());
            return "";
        }
    }

    public String GetAdditionalInfoString() {
        try {
            return new GsonBuilder().serializeNulls().create().toJson(this.additionalInfo);
        } catch (JsonIOException e) {
            UTCLog.log("UTCJsonSerializer", "Error in json serialization" + e.toString());
            return null;
        }
    }
}
