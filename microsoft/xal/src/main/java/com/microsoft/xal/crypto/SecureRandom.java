package com.microsoft.xal.crypto;

import org.jetbrains.annotations.NotNull;

public class SecureRandom {
    @NotNull
    public static byte[] GenerateRandomBytes(int numBytes) {
        byte[] bytes = new byte[numBytes];
        new java.security.SecureRandom().nextBytes(bytes);
        return bytes;
    }
}