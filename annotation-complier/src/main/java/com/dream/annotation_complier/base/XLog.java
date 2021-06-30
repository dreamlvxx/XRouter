package com.dream.annotation_complier.base;


public class XLog {
    private static Boolean isDebug = true;

    public static void setDebug(Boolean isDebug) {
        XLog.isDebug = isDebug;
    }

    public static Boolean getDebug() {
        return isDebug;
    }
}
