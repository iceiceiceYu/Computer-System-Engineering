package main.utility;

import main.exception.ErrorCode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MD5Util {
    static MessageDigest MD5;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ErrorCode(ErrorCode.MD5_INIT_FAILED);
        }
    }

    public static String encrypt(byte[] bytes) {
        return stringEncrypt(Objects.requireNonNull(arrayEncrypt(bytes)));
    }

    private static String arrayEncrypt(byte[] bytes) {
        byte[] digest = MD5.digest(bytes);
        StringBuilder stringBuilder = byte2Hex(digest);
        return stringBuilder.toString();
    }

    private static String stringEncrypt(String string) {
        byte[] bytes = string.getBytes();
        byte[] digest = MD5.digest(bytes);
        StringBuilder stringBuilder = byte2Hex(digest);
        return stringBuilder.toString();
    }

    private static StringBuilder byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (byte b : bytes) {
            stringBuilder.append(hexDigits[(b >> 4) & 15]);
            stringBuilder.append(hexDigits[(b & 15)]);
        }
        return stringBuilder;
    }
}
