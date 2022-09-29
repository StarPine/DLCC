package com.aliyun.svideo.beauty.queen.view;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.aliyun.svideo.base.BaseChooser;
import com.aliyun.svideo.base.widget.PagerSlidingTabStrip;
import com.aliyun.svideo.beauty.queen.R;
import com.aliyun.svideo.beauty.queen.adapter.RaceViewPagerAdapter;
import com.aliyun.svideo.beauty.queen.adapter.holder.QueenFaceViewHolder;
import com.aliyun.svideo.beauty.queen.adapter.holder.QueenSkinViewHolder;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyChooserCallback;

public class QueenBeautyChooser extends BaseChooser {
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabPageIndicator;
    private LinearLayout llBlank;
    private OnBeautyChooserCallback mOnBeautyChooserCallback;
    private int mBeautyFaceLevel = 3;
    private int mBeautySkinLevel = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.QUDemoFullStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alivc_queen_dialog_chooser_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTabPageIndicator = (PagerSlidingTabStrip) view.findViewById(R.id.alivc_dialog_indicator);
        mViewPager = (ViewPager) view.findViewById(R.id.alivc_dialog_container);
        llBlank = view.findViewById(R.id.ll_blank);
        mTabPageIndicator.setTextColorResource(R.color.aliyun_svideo_tab_text_color_selector);
        mTabPageIndicator.setTabViewId(R.layout.aliyun_svideo_layout_tab_top);
        RaceViewPagerAdapter raceViewPagerAdapter = new RaceViewPagerAdapter();
        QueenFaceViewHolder queenFaceViewHolder = new QueenFaceViewHolder(view.getContext(), mBeautyFaceLevel, mOnBeautyChooserCallback);
        QueenSkinViewHolder queenSkinViewHolder = new QueenSkinViewHolder(view.getContext(), mBeautySkinLevel, mOnBeautyChooserCallback);
        raceViewPagerAdapter.addViewHolder(queenFaceViewHolder);
        raceViewPagerAdapter.addViewHolder(queenSkinViewHolder);
        mViewPager.setOffscreenPageLimit(raceViewPagerAdapter.size());
        mViewPager.setAdapter(raceViewPagerAdapter);
        mTabPageIndicator.setViewPager(mViewPager);
    }


    @Override
    public void onStart() {
        super.onStart();
        llBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    解决crash:java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                    原因:after onSaveInstanceState invoke commit,而 show 会触发 commit 操作
                    fragment is added and its state has already been saved，
                    Any operations that would change saved state should not be performed if this method returns true
                */
                if (isStateSaved()) {
                    return;
                }
                if (mOnBeautyChooserCallback != null) {
                    mOnBeautyChooserCallback.onChooserBlankClick();
                }
            }
        });
    }

    public void show(FragmentManager manager, String tag, int beautyFaceLevel, int beautySkinLevel) {
        mBeautyFaceLevel = beautyFaceLevel;
        mBeautySkinLevel = beautySkinLevel;
        super.show(manager, tag);
    }

    public void setOnBeautyChooserCallback(OnBeautyChooserCallback mOnBeautyChooserCallback) {
        this.mOnBeautyChooserCallback = mOnBeautyChooserCallback;
    }


}
