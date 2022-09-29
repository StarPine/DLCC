package com.dl.playfun.ui.mine.vipprivilege;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.viewmodel.BaseViewModel;

/**
 * @author wulei
 */
public class VipPrivilegeViewModel extends BaseViewModel<AppRepository> {

    public VipPrivilegeViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);

    }

}