package com.mojang.minecraftpe;

import android.os.Looper;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.microsoft.xbox.service.notification.NotificationHelper;
import com.microsoft.xbox.service.notification.NotificationResult;

public class NotificationListenerService extends FirebaseMessagingService {
    private static String sDeviceRegistrationToken = "";

    native void nativePushNotificationReceived(int i, String str, String str2, String str3);

    public NotificationListenerService() {
        retrieveDeviceToken();
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (BrazeFirebaseMessagingService.handleBrazeRemoteMessage(this, remoteMessage)) {
//            return;
//        }
        if (remoteMessage.getData().get("type").startsWith("xbox")) {
            NotificationResult notificationResultTryParseXboxLiveNotification = NotificationHelper.tryParseXboxLiveNotification(remoteMessage, this);
            nativePushNotificationReceived(notificationResultTryParseXboxLiveNotification.notificationType.ordinal(), notificationResultTryParseXboxLiveNotification.title, notificationResultTryParseXboxLiveNotification.body, notificationResultTryParseXboxLiveNotification.data);
        } else {
            nativePushNotificationReceived(NotificationResult.NotificationType.Unknown.ordinal(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData().get(""));
        }
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(String str) {
        Log.i("Minecraft", "New Device Push Token: " + str);
        sDeviceRegistrationToken = str;
        //Braze.getInstance(FirebaseApp.getInstance().getApplicationContext()).setRegisteredPushToken(sDeviceRegistrationToken);
    }

    public static String getDeviceRegistrationToken() {
        if (sDeviceRegistrationToken.isEmpty()) {
            retrieveDeviceToken();
        }
        return sDeviceRegistrationToken;
    }

    private static void retrieveDeviceToken() {
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            Log.e("Minecraft", "NotificationListenerService.retrieveDeviceToken() should not run on main thread.");
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() { // from class: com.mojang.minecraftpe.NotificationListenerService.1
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public void onComplete(Task<String> task) {
                String result = task.isSuccessful() ? task.getResult() : "";
                if (result != null && !result.isEmpty()) {
                    Log.i("Minecraft", "Device Push Token: " + result);
                    String unused = NotificationListenerService.sDeviceRegistrationToken = result;
                    //Braze.getInstance(FirebaseApp.getInstance().getApplicationContext()).setRegisteredPushToken(NotificationListenerService.sDeviceRegistrationToken);
                    return;
                }
                Log.e("Minecraft", "Unable to get Firebase Messaging token, trying again...");
            }
        });
    }
}
