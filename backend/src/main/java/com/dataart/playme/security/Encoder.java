package com.dataart.playme.security;

import com.dataart.playme.util.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Component
public class Encoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence line) {
        byte[] bytesOfMessage = line.toString().getBytes(StandardCharsets.UTF_8);
        String encoding = Constants.get(Constants.Security.ENCODING_ID);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance(encoding);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e);
        }

        byte[] encodedBytes = md.digest(bytesOfMessage);
        return byteToHex(encodedBytes);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String newPasswordEncoded = encode(rawPassword);
        return encodedPassword.equals(newPasswordEncoded);
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
