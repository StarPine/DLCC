package com.fine.friendlycc.ui.mine.webdetail;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.viewmodel.BaseViewModel;

/**
 * @author wulei
 */
public class WebDetailViewModel extends BaseViewModel<AppRepository> {

    public WebDetailViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

}