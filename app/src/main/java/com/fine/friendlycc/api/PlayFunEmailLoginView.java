package com.fine.friendlycc.api;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fine.friendlycc.R;

/**
 * Author: 彭石林
 * Time: 2022/4/21 10:45
 * Description: This is PlayFunEmailLoginView
 */
public class PlayFunEmailLoginView extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_email_login_activity);
    }
}
