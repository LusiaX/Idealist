package idealist.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:07:23
 */
public abstract class CodecUtils {
    private static final char[] CHAR_64_MAP;

    static {
        CHAR_64_MAP = new char[64];
        for (int i = 0; i < 10; i++) {
            CHAR_64_MAP[i] = (char) ('0' + i);
        }
        for (int i = 10; i < 36; i++) {
            CHAR_64_MAP[i] = (char) ('a' + i - 10);
        }
        for (int i = 36; i < 62; i++) {
            CHAR_64_MAP[i] = (char) ('A' + i - 36);
        }
        CHAR_64_MAP[62] = '+';
        CHAR_64_MAP[63] = '-';
    }

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacSHA1(String encryptKey, String encryptText) {
        try {
            byte[] data = encryptKey.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);

            byte[] text = encryptText.getBytes(StandardCharsets.UTF_8);
            return Base64.getEncoder().encodeToString(mac.doFinal(text));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String encodeURL(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeURL(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hexTo64(String hex) {
        StringBuilder r = new StringBuilder();
        int index;
        int[] buff = new int[3];
        int l = hex.length();
        for (int i = 0; i < l; i++) {
            index = i % 3;
            buff[index] = Integer.parseInt("" + hex.charAt(i), 16);
            if (index == 2) {
                r.append(CHAR_64_MAP[buff[0] << 2 | buff[1] >>> 2]);
                r.append(CHAR_64_MAP[(buff[1] & 3) << 4 | buff[2]]);
            }
        }
        return r.toString();
    }
}
