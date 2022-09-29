package com.dl.playfun.ui.login;


import com.dl.playfun.R;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭
 * Time: 2022/7/12 10:50
 * Description: This is GoogleApiError
 */
public class GoogleApiError {
    public static void toastError(int errorCode) {
        switch (errorCode) {
            case 2:
                ToastUtils.showLong(R.string.playfun_error_google_2);
                break;
            case 3:
                ToastUtils.showLong(R.string.playfun_error_google_3);
                break;
            case 4:
                ToastUtils.showLong(R.string.playfun_error_google_4);
                break;
            case 5:
                ToastUtils.showLong(R.string.playfun_error_google_5);
                break;
            case 6:
                ToastUtils.showLong(R.string.playfun_error_google_6);
                break;
            case 7:
                ToastUtils.showLong(R.string.playfun_error_google_7);
                break;
            case 8:
                ToastUtils.showLong(R.string.playfun_error_google_8);
                break;
            case 13:
                ToastUtils.showLong(R.string.playfun_error_google_13);
                break;
            case 14:
                ToastUtils.showLong(R.string.playfun_error_google_14);
                break;
            case 15:
                ToastUtils.showLong(R.string.playfun_error_google_15);
                break;
            case 16:
                ToastUtils.showLong(R.string.playfun_error_google_16);
                break;
            case 17:
                ToastUtils.showLong(R.string.playfun_error_google_17);
                break;
            case 20:
                ToastUtils.showLong(R.string.playfun_error_google_20);
                break;
            case 21:
                ToastUtils.showLong(R.string.playfun_error_google_21);
                break;
            case 22:
                ToastUtils.showLong(R.string.playfun_error_google_22);
                break;
            case 12500:
                ToastUtils.showLong(R.string.playfun_error_google_12500);
                break;
            default:
                ToastUtils.showLong(R.string.playfun_error_google);
                break;
        }
    }
}
