package com.dl.playfun.observable;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

public class RefreshLoadMoreUIChangeObservable {
    //下拉刷新开始
    public SingleLiveEvent<Void> startRefreshing = new SingleLiveEvent<>();
    //下拉刷新完成
    public SingleLiveEvent<Void> finishRefreshing = new SingleLiveEvent<>();
    //上拉加载完成
    public SingleLiveEvent<Void> finishLoadmore = new SingleLiveEvent<>();

}
