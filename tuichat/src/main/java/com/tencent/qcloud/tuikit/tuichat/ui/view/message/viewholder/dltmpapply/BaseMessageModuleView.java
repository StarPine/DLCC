package com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder.dltmpapply;

import android.content.Context;

import com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder.CustomDlTempMessageHolder;

/**
 * Author: 彭石林
 * Time: 2022/9/20 11:32
 * Description: This is BaseMessageModuleView
 */
public abstract class BaseMessageModuleView {

    public final CustomDlTempMessageHolder customDlTempMessageHolder;

    public BaseMessageModuleView(CustomDlTempMessageHolder customDlTempMessageHolder) {
        this.customDlTempMessageHolder = customDlTempMessageHolder;
    }

    public Context getContext(){
        return customDlTempMessageHolder.getContext();
    }

    public int dp2px(Context context, float dpValue) {
        final float densityScale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * densityScale + 0.5f);
    }
}
