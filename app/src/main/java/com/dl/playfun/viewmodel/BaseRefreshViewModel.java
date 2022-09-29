package com.dl.playfun.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.dl.playfun.app.AppConfig;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.observable.RefreshLoadMoreUIChangeObservable;
import com.dl.playfun.ui.mine.webview.WebViewFragment;

import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public abstract class BaseRefreshViewModel<M extends BaseModel> extends BaseViewModel<M> {

    public RefreshLoadMoreUIChangeObservable uc = new RefreshLoadMoreUIChangeObservable();
    protected int currentPage = 1;
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(() -> {
        currentPage = 1;
        loadDatas(currentPage);
    });
    public BindingCommand onLoadMoreCommand = new BindingCommand(() -> nextPage());

    //商店入口
    public BindingCommand shopOnClickCommand = new BindingCommand(() -> {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("link", ((AppRepository)model).readApiConfigManagerEntity().getPlayFunWebUrl() + "/shop");
            start(WebViewFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    public BaseRefreshViewModel(@NonNull Application application, M model) {
        super(application, model);
    }

    protected void startRefresh() {
        uc.startRefreshing.call();
    }

    protected void nextPage() {
        currentPage++;
        loadDatas(currentPage);
    }

    public abstract void loadDatas(int page);

    /**
     * 停止下拉刷新或加载更多动画
     */
    protected void stopRefreshOrLoadMore() {
        if (currentPage == 1) {
            uc.finishRefreshing.call();
        } else {
            uc.finishLoadmore.call();
        }
    }
}
