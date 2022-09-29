package com.dl.playfun.widget.dialog.loading;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dl.playfun.R;
import com.dl.playfun.ui.base.BaseDialog;
import com.tencent.qcloud.tuicore.custom.CustomDrawableUtils;

import java.lang.reflect.Field;

/**
 * Author: 彭石林
 * Time: 2022/9/27 18:00
 * Description: This is DialogLoading
 */
public class DialogLoading extends BaseDialog {

    View contentView;

    public DialogLoading(Context context) {
        super(context);
        initView(context);
    }

    void initView(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_mp_loading, null);
        FrameLayout flLayout = contentView.findViewById(R.id.fl_layout);
        if(flLayout!=null){
            CustomDrawableUtils.generateDrawable(flLayout, getColorFromResource(R.color.picture_color_eb),
                    8,null,null,null,null,
                    null,null,null,null,null,null);
        }
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    Integer getColorFromResource(Integer resourceId) {
        if (resourceId==null) {
            return null;
        } else {
            return getContext().getResources().getColor(resourceId);
        }
    }
    public void show() {
        //设置背景透明,去四个角
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(contentView);
        //设置宽度充满屏幕
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setBackgroundDrawable(null);
        window.setDimAmount(0f);
        // 解决 状态栏变色的bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    @SuppressLint("PrivateApi")
                    Class<?> decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                    Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                    field.setAccessible(true);
                    // 去掉高版本蒙层改为透明
                    field.setInt(window.getDecorView(), Color.TRANSPARENT);
                } catch (Exception ignored) {
                }
            }
        }
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
