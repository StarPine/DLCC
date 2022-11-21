package com.dl.lib.util.log;

import android.os.Build;

import org.jetbrains.annotations.NotNull;

/**
* @Desc TODO(自定义日志打印规则)
* @author 彭石林
* @Date 2022/7/16
*/
public final class DebugLoggerTree extends MPTimber.DebugTree {

    private static final int MAX_TAG_LENGTH = 23;

    /**
     * 创建日志堆栈 TAG
     */
    @Override
    protected String createStackElementTag(@NotNull StackTraceElement element) {
        String tag = "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
        // 日志 TAG 长度限制已经在 Android 7.0 被移除
        if (tag.length() <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return tag;
        }
        return tag.substring(0, MAX_TAG_LENGTH);
    }
}