package com.fine.friendlycc.ui.ranklisk.ranklist;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;

/**
 * @author wulei
 */
public class RankListViewModel extends BaseRefreshViewModel<AppRepository> {

    public RankListViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
    }

    @Override
    public void onLazyInitView() {
        super.onLazyInitView();
//        startRefresh();
        loadDatas(1);
    }

    @Override
    public void loadDatas(int page) {
    }

}
