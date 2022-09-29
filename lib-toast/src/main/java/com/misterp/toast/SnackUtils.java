package com.misterp.toast;

import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Author: 彭石林
 * Time: 2022/8/29 11:40
 * Description: This is Snackbar
 */
public class SnackUtils {
    private static SnackUtils INSTANCE = null;
    AtomicReference<Snackbar> snackToastAtomicReference;

    public static SnackUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (SnackUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SnackUtils();
                }
            }
        }
        return INSTANCE;
    }

    public static void showCenterShort(final View view, final String resValue){
        Snackbar snackbar = Snackbar.make(view,"",Snackbar.LENGTH_SHORT);
        // 设置SnackbarView的padding都为0，避免上图中出现黑色边框背景的情况
        snackbar.getView().setPadding(0,0,0,0);
        // 将SnackbarView的背景颜色设置为透明，避免在自定义布局中有圆角或者自适应宽度的时候显示一块黑色背景的情况
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        // 获取到Snackbar.getView获取的Snackbar的view
        Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snackbar.getView();
        // 新建一个LayoutParams将SnackbarView的LayoutParams的宽高自适应内容
        FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置新的元素位置
        // Gravity有许多属性，基本上可以满足大众需求， 我们这里设置了处于屏幕的中央
        fl.gravity = Gravity.CENTER;
        // 将新的LayoutParams设置给SnackbarView
        snackbarView.setLayoutParams(fl);
        //加载Toast布局
        View toastRoot = LayoutInflater.from(snackbar.getContext()).inflate(R.layout.snackbar_custom_layout,null);
        //初始化布局控件
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.message);
        //为控件设置属性
        mTextView.setText(resValue);
        // 将获取的自定义布局view添加到SnackbarView中
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            snackbar.getView().setElevation(9999);
        }
        // 将获取的自定义布局view添加到SnackbarView中
        snackbarView.addView(toastRoot);
        snackbar.show();

    }
}
