package com.dataart.playme.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Encoder {

    public static String encode(String line) {
        byte[] bytesOfMessage = line.getBytes(StandardCharsets.UTF_8);
        String encoding = Constants.get(Constants.ENCODING_ID);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance(encoding);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e);
        }

        byte[] encodedBytes = md.digest(bytesOfMessage);
        return byteToHex(encodedBytes);
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
