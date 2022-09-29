package com.aliyun.svideo.beauty.queen.adapter.holder;

import android.content.Context;
import android.view.View;


public abstract class BaseCaptionViewHolder {
    private final View mItemView;
    private final Context mContext;

    public BaseCaptionViewHolder(Context context) {
        mContext = context;
        this.mItemView = onCreateView(context);
    }

    public Context getContext() {
        return mContext;
    }

    public View getItemView() {
        return mItemView;
    }

    public abstract View onCreateView(Context context);

    public abstract void onBindViewHolder();



}
