package com.fine.friendlycc.entity;

import android.content.Intent;

/**
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/11 12:02
 * 修改备注：
 */
public class RestartActivityEntity {
    private Intent intent;

    public RestartActivityEntity(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
