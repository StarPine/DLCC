package com.fine.friendlycc.bean;

import android.content.Intent;

/**
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/11 12:02
 * 修改备注：
 */
public class RestartActivityBean {
    private Intent intent;

    public RestartActivityBean(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}