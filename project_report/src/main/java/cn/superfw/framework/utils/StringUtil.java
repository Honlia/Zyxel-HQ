package cn.superfw.framework.utils;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串处理帮助类
 * @author chenchao
 * @since 1.0
 */
public class StringUtil extends StringUtils {
    /**
     * 获取uuid类型的字符串
     * @return uuid字符串
     */
    public static String getPrimaryKey() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断字符串是否为空
     * @param str 字符串
     * @return 是否为空标识
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否为非空
     * @param str 字符串
     * @return 是否为非空标识
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 根据字符串数组返回逗号分隔的字符串值
     * @param strArray 字符串数组
     * @return 逗号分隔的字符串
     */
    public static String getStringByArray(String... strArray) {
        if(strArray == null) return "";
        StringBuilder buffer = new StringBuilder(strArray.length * 10);
        for(String str : strArray) {
            buffer.append(str).append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    /**
     * xml内容特殊符号替换
     * @param xml xml字符串
     * @return 替换后的xml
     */
    public static String textXML(String xml) {
        if(xml == null) return "";
        String content = xml;
        content = content.replaceAll("<", "&lt;");
        content = content.replaceAll(">", "&gt;");
        content = content.replaceAll("\"", "&quot;");
        content = content.replaceAll("\n", "</br>");
        return content;
    }

    /**
     * 简单字符串匹配方法，支持匹配类型为：
     * *what *what* what*
     * @param pattern 匹配模式
     * @param str 字符串
     * @return 是否匹配
     */
    public static boolean simpleMatch(String pattern, String str) {
        if (pattern == null || str == null) {
            return false;
        }
        int firstIndex = pattern.indexOf('*');
        if (firstIndex == -1) {
            return pattern.equals(str);
        }
        if (firstIndex == 0) {
            if (pattern.length() == 1) {
                return true;
            }
            int nextIndex = pattern.indexOf('*', firstIndex + 1);
            if (nextIndex == -1) {
                return str.endsWith(pattern.substring(1));
            }
            String part = pattern.substring(1, nextIndex);
            int partIndex = str.indexOf(part);
            while (partIndex != -1) {
                if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                    return true;
                }
                partIndex = str.indexOf(part, partIndex + 1);
            }
            return false;
        }
        return (str.length() >= firstIndex &&
                pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) &&
                simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }
}
