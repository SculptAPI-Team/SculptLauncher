package com.xbox.httpclient;

import java.nio.ByteBuffer;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public final class HttpClientWebSocket extends WebSocketListener {
    private static final OkHttpClient OK_CLIENT = new OkHttpClient();
    private final Headers.Builder headers = new Headers.Builder();
    private final long owner;
    private WebSocket socket;

    public native void onBinaryMessage(ByteBuffer byteBuffer);

    public native void onClose(int i);

    @Override // okhttp3.WebSocketListener
    public void onClosing(WebSocket webSocket, int i, String str) {
    }

    public native void onFailure();

    public native void onMessage(String str);

    public native void onOpen();

    HttpClientWebSocket(long j) {
        this.owner = j;
    }

    public void addHeader(String str, String str2) {
        this.headers.add(str, str2);
    }

    public void connect(String str, String str2) {
        addHeader("Sec-WebSocket-Protocol", str2);
        this.socket = OK_CLIENT.newWebSocket(new Request.Builder().url(str).headers(this.headers.build()).build(), this);
    }

    public boolean sendMessage(String str) {
        return this.socket.send(str);
    }

    public boolean sendBinaryMessage(ByteBuffer byteBuffer) {
        return this.socket.send(ByteString.of(byteBuffer));
    }

    public void disconnect(int i) {
        this.socket.close(i, null);
    }

    @Override // okhttp3.WebSocketListener
    public void onOpen(WebSocket webSocket, Response response) {
        onOpen();
    }

    @Override // okhttp3.WebSocketListener
    public void onFailure(WebSocket webSocket, Throwable th, Response response) {
        onFailure();
    }

    @Override // okhttp3.WebSocketListener
    public void onClosed(WebSocket webSocket, int i, String str) {
        onClose(i);
    }

    @Override // okhttp3.WebSocketListener
    public void onMessage(WebSocket webSocket, String str) {
        onMessage(str);
    }

    @Override // okhttp3.WebSocketListener
    public void onMessage(WebSocket webSocket, ByteString byteString) {
        onBinaryMessage(byteString.asByteBuffer());
    }

    protected void finalize() {
        this.socket.cancel();
    }
}
