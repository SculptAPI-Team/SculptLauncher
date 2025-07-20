package com.mojang.minecraftpe;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import java.util.HashMap;
import java.util.HashSet;

public class NetworkMonitor {
    private static final int NETWORK_CATEGORY_ETHERNET = 0;
    private static final int NETWORK_CATEGORY_OTHER = 2;
    private static final int NETWORK_CATEGORY_WIFI = 1;
    private HashMap<Integer, HashSet<Network>> mAvailableNetworksPerCategory;
    private Context mContext;

    private native void nativeUpdateNetworkStatus(boolean isEthernetConnected, boolean isWifiConnected, boolean isOtherConnected);

    public NetworkMonitor(Context context) {
        this.mContext = context;
        HashMap<Integer, HashSet<Network>> map = new HashMap<>();
        this.mAvailableNetworksPerCategory = map;
        map.put(0, new HashSet<>());
        this.mAvailableNetworksPerCategory.put(1, new HashSet<>());
        this.mAvailableNetworksPerCategory.put(2, new HashSet<>());
        _addNetworkCallbacksForTransport(3, 0);
        _addNetworkCallbacksForTransport(1, 1);
        _addNetworkCallbacksForTransport(0, 2);
        _addNetworkCallbacksForTransport(2, 2);
        if (Build.VERSION.SDK_INT >= 31) {
            _addNetworkCallbacksForTransport(8, 2);
        }
    }

    private void _addNetworkCallbacksForTransport(int transport, final int networkCategory) {
        ((ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).registerNetworkCallback(_createNetworkRequestForTransport(transport), new ConnectivityManager.NetworkCallback() { // from class: com.mojang.minecraftpe.NetworkMonitor.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                ((HashSet) NetworkMonitor.this.mAvailableNetworksPerCategory.get(Integer.valueOf(networkCategory))).add(network);
                NetworkMonitor.this._updateStatus();
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                ((HashSet) NetworkMonitor.this.mAvailableNetworksPerCategory.get(Integer.valueOf(networkCategory))).remove(network);
                NetworkMonitor.this._updateStatus();
            }
        });
    }

    private NetworkRequest _createNetworkRequestForTransport(int transport) {
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        builder.addTransportType(transport);
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _updateStatus() {
        nativeUpdateNetworkStatus(!this.mAvailableNetworksPerCategory.get(0).isEmpty(), !this.mAvailableNetworksPerCategory.get(1).isEmpty(), true ^ this.mAvailableNetworksPerCategory.get(2).isEmpty());
    }
}
