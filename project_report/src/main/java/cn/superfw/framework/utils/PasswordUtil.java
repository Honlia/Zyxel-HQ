package cn.superfw.framework.utils;

import java.util.Random;

public class PasswordUtil {
    private static final String pwdStr = "0123456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";


    public static String rand(int length) {

        String randStr = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randStr += pwdStr.charAt(random.nextInt(pwdStr.length()));
        }
        return randStr;
    }

}
