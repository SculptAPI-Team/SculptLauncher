package com.microsoft.xbox.toolkit.network;

import org.apache.http.Header;

import java.io.InputStream;

public class XLEHttpStatusAndStream {
    public InputStream stream = null;
    public int statusCode = -1;
    public String statusLine = null;
    public String redirectUrl = null;
    public Header[] headers = new Header[0];

    public void close() {
        InputStream inputStream = this.stream;
        if (inputStream != null) {
            try {
                inputStream.close();
                this.stream = null;
            } catch (Exception unused) {
            }
        }
    }
}
