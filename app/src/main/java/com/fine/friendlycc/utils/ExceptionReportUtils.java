package com.fine.friendlycc.utils;

import com.fine.friendlycc.BuildConfig;

public class ExceptionReportUtils {

    public static void report(Throwable t) {
        if (BuildConfig.DEBUG) {
            t.printStackTrace();
        }
    }
}
