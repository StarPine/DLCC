package com.fine.friendlycc.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/2/19 14:48
 * Description: This is AutoSize
 */
public class AutoSizeUtils {
    public static final int WIDTH_DP = 0x25;
    public static final int WIDTH_PT = 0x35;
    public static final int HEIGHT_PT = 0x45;

    private static final float widthSize = 360f;
    private static final float heightSize = 640f;

    @IntDef({WIDTH_DP, WIDTH_PT, HEIGHT_PT})
    @Retention(RetentionPolicy.CLASS)
    @interface ScreenMode {
    }

    private AutoSizeUtils() {
    }

    private static List<Field> sMetricsFields;
    private static DisplayMetrics systemDm;


    public static Resources applyAdapt(final Resources resources) {

        return applyAdaptSize(resources,true);
    }

    /**
    * @Desc TODO(适配规则类)
    * @author 彭石林
    * @parame resources 资源 Resources
    * @parame OnWidth 是否已宽度进行适配
    * @return android.content.res.Resources
    * @Date 2022/8/1
    */
    public static Resources applyAdapt(final Resources resources,boolean OnWidth){
        return applyAdaptSize(resources,OnWidth);
    }

    private static Resources applyAdaptSize(final Resources resources,boolean OnWidth){
        final float autoDesignSize = OnWidth ? widthSize : heightSize;
        DisplayMetrics activityDm = resources.getDisplayMetrics();
        if (null == systemDm) {
            systemDm = Resources.getSystem().getDisplayMetrics();
        }
        change(WIDTH_DP, resources, activityDm, systemDm, autoDesignSize);
        //兼容其他手机
        if (sMetricsFields == null) {
            sMetricsFields = new ArrayList<>();
            Class resCls = resources.getClass();
            Field[] declaredFields = resCls.getDeclaredFields();
            while (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
                        field.setAccessible(true);
                        DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
                            sMetricsFields.add(field);
                            change(WIDTH_DP, resources, tmpDm, systemDm, autoDesignSize);
                        }
                    }
                }
                resCls = resCls.getSuperclass();
                if (resCls != null) {
                    declaredFields = resCls.getDeclaredFields();
                } else {
                    break;
                }
            }
        } else {
            for (Field metricsField : sMetricsFields) {
                try {
                    DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
                    if (dm != null) change(WIDTH_DP, resources, dm, systemDm, autoDesignSize);
                } catch (Exception e) {
//                    Log.e("ScreenHelper", "applyMetricsFields: " + e);
                }
            }
        }
        return resources;
    }

    private static void change(@ScreenMode int screenMode, final Resources resources, DisplayMetrics activityDm, DisplayMetrics systemDm, float size) {
        switch (screenMode) {
            case WIDTH_DP:
                adaptWidthPixels(resources, activityDm, systemDm, size);
                break;
            case HEIGHT_PT:
                adaptHeightXdpi(resources, size, systemDm);
                break;
            case WIDTH_PT:
                adaptWidthXdpi(resources, size, systemDm);
                break;
        }
    }

    private static void adaptWidthPixels(Resources resources, DisplayMetrics activityDm, DisplayMetrics systemDm, float designWidthPixels) {
        //确保设备在横屏和竖屏的显示大小,确保 dp 的大小值
        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //横屏以屏幕的宽度来适配，确保 dp 的大小值
            activityDm.density = activityDm.widthPixels * 1.0f / designWidthPixels;
        } else {
            //竖屏以屏幕的高度来适配
            activityDm.density = activityDm.heightPixels * 1.0f / designWidthPixels;
        }
        //确保字体的显示大小
        activityDm.scaledDensity = activityDm.density * (systemDm.scaledDensity / systemDm.density);
        //确保设置的 dpi
        activityDm.densityDpi = (int) (160 * activityDm.density);
        Configuration activityConfiguration = resources.getConfiguration();
        setScreenSizeDp(activityConfiguration,(int)widthSize,(int)heightSize);
    }

    /**
     * Adapt for the horizontal screen, and call it in [android.app.Activity.getResources].
     */
    private static void adaptWidthXdpi(Resources resources, float designWidth, DisplayMetrics systemDm) {
        resources.getDisplayMetrics().xdpi = (systemDm.widthPixels * 72f) / designWidth;
    }

    /**
     * Adapt for the vertical screen, and call it in [android.app.Activity.getResources].
     */
    private static void adaptHeightXdpi(Resources resources, float designHeight, DisplayMetrics systemDm) {
        resources.getDisplayMetrics().xdpi = (systemDm.heightPixels * 72f) / designHeight;
    }

    /**
     * @param resources The resources.
     */
    public static void closeAdapt(Resources resources) {
        DisplayMetrics activityDm = resources.getDisplayMetrics();
        if (null == systemDm) {
            systemDm = Resources.getSystem().getDisplayMetrics();
        }
        resetResources(activityDm, systemDm);
        //兼容其他手机
        if (sMetricsFields == null) {
            sMetricsFields = new ArrayList<>();
            Class resCls = resources.getClass();
            Field[] declaredFields = resCls.getDeclaredFields();
            while (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
                        field.setAccessible(true);
                        DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
                            sMetricsFields.add(field);
                            resetResources(tmpDm, systemDm);
                        }
                    }
                }
                resCls = resCls.getSuperclass();
                if (resCls != null) {
                    declaredFields = resCls.getDeclaredFields();
                } else {
                    break;
                }
            }
        } else {
            for (Field metricsField : sMetricsFields) {
                try {
                    DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
                    if (dm != null) resetResources(dm, systemDm);
                } catch (Exception e) {
//                    Log.e("ScreenHelper", "applyMetricsFields: " + e);
                }
            }
        }
    }

    private static void resetResources(DisplayMetrics activityDm, DisplayMetrics systemDm) {
        activityDm.xdpi = systemDm.xdpi;
        activityDm.density = systemDm.density;
        activityDm.scaledDensity = systemDm.scaledDensity; //确保字体的显示大小
        activityDm.densityDpi = systemDm.densityDpi;//确保设置的 dpi
    }


    private static DisplayMetrics getMetricsFromField(final Resources resources, final Field field) {
        try {
            return (DisplayMetrics) field.get(resources);
        } catch (Exception e) {
//            Log.e("ScreenHelper", "getMetricsFromField: " + e);
            return null;
        }
    }

    /**
     * Configuration赋值
     *
     * @param configuration  {@link Configuration}
     * @param screenWidthDp  {@link Configuration#screenWidthDp}
     * @param screenHeightDp {@link Configuration#screenHeightDp}
     */
    private static void setScreenSizeDp(Configuration configuration, int screenWidthDp, int screenHeightDp) {
        configuration.screenWidthDp = screenWidthDp;
        configuration.screenHeightDp = screenHeightDp;
    }
}
