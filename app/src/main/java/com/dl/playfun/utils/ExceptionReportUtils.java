package com.dl.playfun.utils;

import com.dl.playfun.BuildConfig;

public class ExceptionReportUtils {

    public static void report(Throwable t) {
        if (BuildConfig.DEBUG) {
            t.printStackTrace();
        }
    }
}
