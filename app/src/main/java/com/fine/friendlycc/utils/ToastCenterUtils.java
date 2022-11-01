package com.fine.friendlycc.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.ColorUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;

import java.lang.ref.WeakReference;


/**
 * Author: 彭石林
 * Time: 2021/9/23 19:44
 * Description: This is ToastCenterUtils
 */
public class ToastCenterUtils {

    private static Toast sToast;
    private static int gravity = Gravity.CENTER;
    private static int xOffset = 0;
    private static int yOffset = -100;//\(int) (64 * AppContext.instance().getResources().getDisplayMetrics().density + 0.5);
    private static WeakReference<View> sViewWeakReference;

    private ToastCenterUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 设置吐司位置
     *
     * @param gravity 位置
     * @param xOffset x偏移
     * @param yOffset y偏移
     */
    public static void setGravity(int gravity, int xOffset, int yOffset) {
        ToastCenterUtils.gravity = gravity;
        ToastCenterUtils.xOffset = xOffset;
        ToastCenterUtils.yOffset = yOffset;
    }

    /**
     * 获取吐司view
     *
     * @return view
     */
    public static View getView() {
        if (sViewWeakReference != null) {
            final View view = sViewWeakReference.get();
            if (view != null) {
                return view;
            }
        }
        if (sToast != null) return sToast.getView();
        return null;
    }


    /**
     * 设置吐司view
     *
     * @param view 视图
     */
    public static void setView(@Nullable View view) {
        sViewWeakReference = view == null ? null : new WeakReference<>(view);
    }


    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    public static void showShort(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShort(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShort(@StringRes int resId, Object... args) {
        show(resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShort(String format, Object... args) {
        show(format, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     */
    private static void show(@StringRes int resId, int duration) {
        show(AppContext.instance().getResources().getText(resId).toString(), duration);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(@StringRes int resId, int duration, Object... args) {
        show(String.format(AppContext.instance().getResources().getString(resId), args), duration);
    }

    /**
     * 显示吐司
     *
     * @param format   格式
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static void show(CharSequence text, int duration) {
        cancel();
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ColorUtils.getColor(R.color.white));
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sToast = Toast.makeText(AppContext.instance(), spannableString, duration);
        View view = sToast.getView();
        if(view!=null){
            view.setBackground(AppContext.instance().getDrawable(R.drawable.toast_center_back));
        }
        //view.setBackgroundColor(ColorUtils.getColor(R.color.hud_background));
        sToast.setGravity(gravity, xOffset, yOffset);
        sToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    public static void showToast(@StringRes int ResId) {
        Context context = AppContext.instance();
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        //初始化布局控件
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.message);
        //为控件设置属性
        mTextView.setText(ResId);
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    public static void showToast(String message) {
        Context context = AppContext.instance();
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        //初始化布局控件
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.message);
        //为控件设置属性
        mTextView.setText(message);
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
}