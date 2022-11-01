package com.fine.friendlycc.ui.mine.webview;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.viewmodel.BaseViewModel;

/**
 * Author: 彭石林
 * Time: 2021/11/9 1:25
 * Description: This is FukubukuroViewModel
 */
public class WebViewViewModel extends BaseViewModel<AppRepository> {

    public String getToken() {
        return model.readLoginInfo().getToken();
    }

    public WebViewViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

}
