package com.microsoft.xal.browser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Base64;
import androidx.browser.customtabs.CustomTabsService;
import androidx.core.os.EnvironmentCompat;
import com.microsoft.xal.logging.XalLogger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BrowserSelector {
    private static final Map<String, String> customTabsAllowedBrowsers;

    static {
        HashMap map = new HashMap();
        customTabsAllowedBrowsers = map;
        map.put("com.android.chrome", "OJGKRT0HGZNU+LGa8F7GViztV4g=");
        map.put("org.mozilla.firefox", "kg9Idqale0pqL0zK9l99Kc4m/yw=");
        map.put("com.microsoft.emmx", "P2QOJ59jvOpxCCrn6MfvotoBTK0=");
        map.put("com.sec.android.app.sbrowser", "nKUXDzgZGd/gRG/NqxixmhQ7MWM=");
    }

    public static BrowserSelectionResult selectBrowser(Context context, boolean z) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        String str;
        XalLogger xalLogger = new XalLogger("BrowserSelector");
        try {
            BrowserSelectionResult.BrowserInfo browserInfoUserDefaultBrowserInfo = userDefaultBrowserInfo(context, xalLogger);
            boolean z2 = false;
            if (z) {
                str = "inProcRequested";
            } else if (browserInfoImpliesNoUserDefault(browserInfoUserDefaultBrowserInfo)) {
                str = "noDefault";
            } else {
                String str2 = browserInfoUserDefaultBrowserInfo.packageName;
                if (!browserSupportsCustomTabs(context, str2)) {
                    xalLogger.Important("selectBrowser() Default browser does not support custom tabs.");
                    str = "CTNotSupported";
                } else if (!browserAllowedForCustomTabs(context, xalLogger, str2)) {
                    xalLogger.Important("selectBrowser() Default browser supports custom tabs, but is not allowed.");
                    str = "CTSupportedButNotAllowed";
                } else {
                    xalLogger.Important("selectBrowser() Default browser supports custom tabs and is allowed.");
                    str = "CTSupportedAndAllowed";
                    z2 = true;
                }
            }
            BrowserSelectionResult browserSelectionResult = new BrowserSelectionResult(browserInfoUserDefaultBrowserInfo, str, z2);
            xalLogger.close();
            return browserSelectionResult;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (th != null) {
                    try {
                        xalLogger.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                } else {
                    xalLogger.close();
                }
                throw th2;
            }
        }
    }

    private static BrowserSelectionResult.BrowserInfo userDefaultBrowserInfo(Context context, XalLogger xalLogger) throws PackageManager.NameNotFoundException {
        String str;
        ResolveInfo resolveInfoResolveActivity = context.getPackageManager().resolveActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://microsoft.com")), 65536);
        String str2 = resolveInfoResolveActivity == null ? null : resolveInfoResolveActivity.activityInfo.packageName;
        if (str2 == null) {
            xalLogger.Important("userDefaultBrowserInfo() No default browser resolved.");
            return new BrowserSelectionResult.BrowserInfo("none", 0, "none");
        }
        if (str2.equals("android")) {
            xalLogger.Important("userDefaultBrowserInfo() System resolved as default browser.");
            return new BrowserSelectionResult.BrowserInfo("android", 0, "none");
        }
        int i = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str2, 0);
            i = packageInfo.versionCode;
            str = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            xalLogger.Error("userDefaultBrowserInfo() Error in getPackageInfo(): " + e);
            str = EnvironmentCompat.MEDIA_UNKNOWN;
        }
        xalLogger.Important("userDefaultBrowserInfo() Found " + str2 + " as user's default browser.");
        return new BrowserSelectionResult.BrowserInfo(str2, i, str);
    }

    private static boolean browserInfoImpliesNoUserDefault(BrowserSelectionResult.BrowserInfo browserInfo) {
        return browserInfo.versionCode == 0 && browserInfo.versionName.equals("none");
    }

    private static boolean browserAllowedForCustomTabs(Context context, XalLogger xalLogger, String str) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        PackageInfo packageInfo = null;
        String str2 = customTabsAllowedBrowsers.get(str);
        if (str2 == null) {
            return false;
        }
        try {
            packageInfo = context.getPackageManager().getPackageInfo(str, 64);
        } catch (PackageManager.NameNotFoundException e) {
            xalLogger.Error("browserAllowedForCustomTabs() Error in getPackageInfo(): " + e);
        }
        if (packageInfo == null) {
            xalLogger.Important("No package info found for package: " + str);
            return false;
        }
        for (Signature signature : packageInfo.signatures) {
            if (hashFromSignature(signature).equals(str2)) {
                return true;
            }
        }
        return false;
    }

    private static boolean browserSupportsCustomTabs(Context context, String str) {
        Iterator<ResolveInfo> it = context.getPackageManager().queryIntentServices(new Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION), 0).iterator();
        while (it.hasNext()) {
            if (it.next().serviceInfo.packageName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private static String hashFromSignature(Signature signature) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(signature.toByteArray());
        return Base64.encodeToString(messageDigest.digest(), 2);
    }
}
