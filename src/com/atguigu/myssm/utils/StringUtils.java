package com.atguigu.myssm.utils;

/**
 * @author Neo
 * @version 1.0
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
