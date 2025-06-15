package com.microsoft.xal.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaHasher {
    private MessageDigest md = MessageDigest.getInstance("SHA-256");

    public ShaHasher() throws NoSuchAlgorithmException {
    }

    public void AddBytes(byte[] buffer) {
        md.update(buffer);
    }

    public byte[] SignHash() {
        return md.digest();
    }
}