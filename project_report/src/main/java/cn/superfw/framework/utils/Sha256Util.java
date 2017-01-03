package cn.superfw.framework.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串哈希化用工具类
 */
public final class Sha256Util {

    private static final String SHA256 = "SHA-256";

    private static final String UTF8 = "UTF-8";

    /**
     * Private Constructor.
     */
    private Sha256Util() {
    }

    /**
     * 哈希值计算。
     *
     * @param data 数据
     * @return 哈希值
     */
    public static byte[] hash(String data) {
        if (data == null) {
            throw new RuntimeException("参数data不对。" + data);
        }
        try {
            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.update(data.getBytes(Charset.forName(UTF8).name()));
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("哈希算法不对。", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码不支持。", e);
        }
    }

    /**
     * 取得16进制哈希值字符串。
     *
     * @param data 数据
     * @return 哈希值的16进制字符串
     */
    public static String hashAsHexString(String data) {
        if (data == null) {
            throw new RuntimeException("参数data不对。" + data);
        }
        byte[] bytes = hash(data);

        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return hex.toString();
    }
}
