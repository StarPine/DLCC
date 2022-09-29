package com.dl.playfun.ui.mine.webdetail;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.viewmodel.BaseViewModel;

/**
 * @author wulei
 */
public class WebDetailViewModel extends BaseViewModel<AppRepository> {

    public WebDetailViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

}