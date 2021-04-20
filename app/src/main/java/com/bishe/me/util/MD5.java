package com.bishe.me.util;

import android.support.annotation.NonNull;
import android.util.Log;
import java.security.MessageDigest;
import java.util.Locale;

/**
 * MD5加密工具类
 *
 * @author tzl
 */
public class MD5 {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 加密用户名和密码
     *
     * @param carid 用户名
     * @param pswd  密码
     * @param style 加密方式
     * @return 加密后的字符串
     */
    @NonNull
    public static String encrypt(String carid, @NonNull String pswd, String style) {
        if ("all".equalsIgnoreCase(style)) {
            return encryptStr(carid + pswd).toUpperCase(Locale.ENGLISH);
        } else if ("password".equalsIgnoreCase(style)) {
            return encryptStr(pswd).toUpperCase(Locale.ENGLISH);
        } else {
            return pswd;
        }
    }

    /**
     * 对字符串进行MD5加密
     *
     * @param s 需要加密的字符串.
     * @return 经过加密的字符串
     */
    public static String encryptStr(@NonNull String s) {
        char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] bytes = s.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            /**
             * 对加密后的字符串进行编码。 低位字节通过把每个字节无符号右移四位再映射得到；高位字节直接映射得到。
             */
            for (byte b : bytes) {
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            return null;
        }
    }

    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
        } catch (Exception e) {
            Log.e("[MD5Encode]加密异常", origin);
        }
        return resultString;
    }

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

}
