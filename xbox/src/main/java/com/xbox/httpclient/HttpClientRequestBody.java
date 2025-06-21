package com.xbox.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

public final class HttpClientRequestBody extends RequestBody {
    private final long callHandle;
    private final long contentLength;
    private final MediaType contentType;

    private final class NativeInputStream extends InputStream {
        private final long callHandle;
        private long offset = 0;

        private native int nativeRead(long j, long j2, byte[] bArr, long j3, long j4) throws IOException;

        public NativeInputStream(long j) {
            this.callHandle = j;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            byte[] bArr = new byte[1];
            read(bArr);
            return bArr[0];
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            Objects.requireNonNull(bArr);
            if (i < 0 || i2 < 0 || i + i2 > bArr.length) {
                throw new IndexOutOfBoundsException();
            }
            if (i2 == 0) {
                return 0;
            }
            int iNativeRead = nativeRead(this.callHandle, this.offset, bArr, i, i2);
            if (iNativeRead == -1) {
                return -1;
            }
            this.offset += iNativeRead;
            return iNativeRead;
        }

        @Override // java.io.InputStream
        public long skip(long j) throws IOException {
            this.offset += j;
            return j;
        }
    }

    public HttpClientRequestBody(long j, String str, long j2) {
        this.callHandle = j;
        this.contentType = str != null ? MediaType.parse(str) : null;
        this.contentLength = j2;
    }

    @Override // okhttp3.RequestBody
    public MediaType contentType() {
        return this.contentType;
    }

    @Override // okhttp3.RequestBody
    public long contentLength() {
        return this.contentLength;
    }

    @Override // okhttp3.RequestBody
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        bufferedSink.writeAll(Okio.source(new NativeInputStream(this.callHandle)));
    }
}
