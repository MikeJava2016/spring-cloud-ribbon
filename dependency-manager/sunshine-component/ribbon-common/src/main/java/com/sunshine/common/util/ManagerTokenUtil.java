package com.sunshine.common.util;

public class ManagerTokenUtil {

    // 存储当前请求者token
    private static ThreadLocal<String> threadLocalUid = new ThreadLocal<String>();

    public static void setUid(String uid) {
        threadLocalUid.set(uid);
    }

    public static void removeUid() {
        threadLocalUid.remove();
    }

    public static String getThreadToken() {
        return threadLocalUid.get();
    }

    /**
     * 获取当前请求者userId
     */
    public static Long getCurrentUserId() {
        return  Long.parseLong(threadLocalUid.get()) ;
    }
}
