package com.fine.friendlycc.ui.mine.vipprivilege;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.viewmodel.BaseViewModel;

/**
 * @author wulei
 */
public class VipPrivilegeViewModel extends BaseViewModel<AppRepository> {

    public VipPrivilegeViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);

    }

}