package com.fine.friendlycc.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author geyifeng
 * @date 2019/4/14 4:59 PM
 */
public class Utils {

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat formatday = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat formatYYMMSS = new SimpleDateFormat("yyyyMMdd");


    public static boolean isManilaApp(Context context) {
        return context.getApplicationInfo().packageName.contains("manila");
    }

    public static Integer[] getWidthAndHeight(Window window) {
        if (window == null) {
            return null;
        }
        Integer[] integer = new Integer[2];
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        } else {
            window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        integer[0] = dm.widthPixels;
        integer[1] = dm.heightPixels;
        return integer;
    }


    public static String getPic() {
        Random random = new Random();
        return "http://106.14.135.179/ImmersionBar/" + random.nextInt(40) + ".jpg";
    }

    public static ArrayList<String> getPics() {
        return getPics(4);
    }

    public static ArrayList<String> getPics(int num) {
        ArrayList<String> pics = new ArrayList<>();
        Random random = new Random();

        do {
            String s = "http://106.14.135.179/ImmersionBar/" + random.nextInt(40) + ".jpg";
            if (!pics.contains(s)) {
                pics.add(s);
            }
        } while (pics.size() < num);
        return pics;
    }

    public static String getFullPic() {
        Random random = new Random();
        return "http://106.14.135.179/ImmersionBar/phone/" + random.nextInt(40) + ".jpeg";
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isJSON2(String str) {
        boolean result = false;
        try {
            new Gson().fromJson(str, Map.class);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param val
     * @return
     */
    public static boolean isEngNumber(String val) {
        String pattern = "^[\\w]+$";
        return Pattern.matches(pattern, val);
    }

    /**
     * ???????????????N?????????????????????
     *
     * @param date
     * @param day
     * @return
     * @throws ParseException
     */
    public static Date addDate(Date date, long day) {
        long time = date.getTime(); // ??????????????????????????????
        day = day * 24 * 60 * 60 * 1000; // ????????????????????????????????????
        time += day; // ???????????????????????????
        return new Date(time); // ???????????????????????????
    }

}
