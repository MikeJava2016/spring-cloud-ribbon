package com.sunshine.common.util;

public class ManagerTokenUtil {

    // 存储当前请求者token
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static void setThreadToken(String token) {
        threadLocal.set(token);
    }

    public static void removeThreadToken() {
        threadLocal.remove();
    }

    public static String getThreadToken() {
        return threadLocal.get();
    }

    /**
     * 获取当前请求者userId
     */
    public static Long getCurrentUserId() {
        return  Long.parseLong(threadLocal.get()) ;
    }
}
