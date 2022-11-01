package com.fine.friendlycc.observable;

import java.util.Map;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

public class MvUIChangeObservable {

    public SingleLiveEvent<String> showHudEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Map<String, Object>> showProgressHudEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Void> dismissHudEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Void> hideKeyboardEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Map<String, Object>> startFragmentEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Map<String, Object>> startWithPopFragmentEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Map<String, Object>> startWithPopToFragmentEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Void> popFragmentEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Map<String, Object>> popToFragmentEvent = new SingleLiveEvent<>();
}
