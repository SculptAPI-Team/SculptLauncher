package com.mojang.minecraftpe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PowerManager;

/* loaded from: classes.dex */
public class ThermalMonitor extends BroadcastReceiver {
    private Context mContext;
    private boolean mLowPowerModeEnabled = false;

    public ThermalMonitor(Context context) {
        this.mContext = context;
        context.registerReceiver(this, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
        readPowerMode(context);
    }

    protected void finalize() {
        this.mContext.unregisterReceiver(this);
    }

    public boolean getLowPowerModeEnabled() {
        return this.mLowPowerModeEnabled;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        readPowerMode(context);
    }

    private void readPowerMode(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mLowPowerModeEnabled = ((PowerManager) context.getSystemService("power")).isPowerSaveMode();
        }
    }
}
