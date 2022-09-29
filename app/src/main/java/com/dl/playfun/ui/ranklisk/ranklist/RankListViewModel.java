package com.dl.playfun.ui.ranklisk.ranklist;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.viewmodel.BaseRefreshViewModel;

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
