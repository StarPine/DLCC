package com.dl.playfun.viewadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dl.playfun.R;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * Author: 彭石林
 * Time: 2022/7/26 16:40
 * Description: This is CustomRefreshHeader
 */
public class CustomRefreshHeader extends RelativeLayout implements RefreshHeader {

    private String tvContent = null;

    public CustomRefreshHeader(Context context) {
        this(context,null);
    }

    public void setTvContent(String tvContent) {
        this.tvContent = tvContent;
        if(tvContent!=null){
            if(getView()!=null){
                TextView textView = getView().findViewById(R.id.tv_content);
                if(textView!=null){
                    textView.post(()->textView.setText(tvContent));
                }
            }
        }
    }

    public CustomRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context){
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        View headerView = View.inflate(context, R.layout.smart_custon_header, null);

        addView(headerView,params);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPrimaryColors(int... colors) {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        // 尺寸定义完成
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        //这里的操作是下拉，回退时，动画的变化，
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 100;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    }
}
