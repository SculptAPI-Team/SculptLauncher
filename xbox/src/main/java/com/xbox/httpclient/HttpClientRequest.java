package com.xbox.httpclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClientRequest {
    private static final byte[] NO_BODY = new byte[0];
    private static OkHttpClient OK_CLIENT = new OkHttpClient.Builder().retryOnConnectionFailure(false).build();
    private Request okHttpRequest;
    private Request.Builder requestBuilder = new Request.Builder();

    public static boolean isNetworkAvailable(@NotNull Context context) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    public static HttpClientRequest createClientRequest() {
        return new HttpClientRequest();
    }

    public native void OnRequestCompleted(long j, HttpClientResponse httpClientResponse);

    public native void OnRequestFailed(long j, String str);

    public void setHttpUrl(String url) {
        requestBuilder = requestBuilder.url(url);
    }

    public void setHttpMethodAndBody(String str, long j, String str2, long j2) {
        RequestBody httpClientRequestBody = null;
        if (j2 == 0) {
            if (HttpPost.METHOD_NAME.equals(str) || HttpPut.METHOD_NAME.equals(str)) {
                httpClientRequestBody = RequestBody.create(NO_BODY, str2 != null ? MediaType.parse(str2) : null);
            }
        } else {
            httpClientRequestBody = new HttpClientRequestBody(j, str2, j2);
        }
        this.requestBuilder.method(str, httpClientRequestBody);
    }

    public void setHttpHeader(String name, String value) {
        requestBuilder = requestBuilder.addHeader(name, value);
    }

    public void doRequestAsync(final long j) {
        OK_CLIENT.newCall(this.requestBuilder.build()).enqueue(new Callback() { // from class: com.xbox.httpclient.HttpClientRequest.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                HttpClientRequest.this.OnRequestFailed(j, iOException.getClass().getCanonicalName());
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) {
                HttpClientRequest httpClientRequest = HttpClientRequest.this;
                long j2 = j;
                httpClientRequest.OnRequestCompleted(j2, new HttpClientResponse(j2, response));
            }
        });
    }
}
