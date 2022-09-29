package com.dl.playfun.utils;

import android.app.Activity;
import android.app.Dialog;

import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;

public class ImmersionBarUtils {

    public static void setupStatusBar(Fragment fragment, boolean isDarkFont, boolean keyboardEnable) {
        ImmersionBar.with(fragment)
                .statusBarDarkFont(isDarkFont, 0.2f)
                .autoNavigationBarDarkModeEnable(true)
                .keyboardEnable(keyboardEnable)
                .init();
    }

    public static void setupStatusBar(Activity activity, boolean isDarkFont, boolean keyboardEnable) {
        ImmersionBar.with(activity)
                .statusBarDarkFont(isDarkFont, 0.2f)
                .autoNavigationBarDarkModeEnable(true)
                .keyboardEnable(keyboardEnable)
                .init();
    }

    public static void setupStatusBar(Activity activity, Dialog dialog, boolean isDarkFont, boolean keyboardEnable) {
        ImmersionBar.with(activity, dialog)
                .statusBarDarkFont(isDarkFont, 0.2f)
                .autoNavigationBarDarkModeEnable(true)
                .keyboardEnable(keyboardEnable)
                .init();
    }


}
