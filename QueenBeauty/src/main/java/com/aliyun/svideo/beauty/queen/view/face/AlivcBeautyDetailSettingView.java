package com.aliyun.svideo.beauty.queen.view.face;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.aliyun.svideo.base.widget.beauty.listener.OnProgresschangeListener;
import com.aliyun.svideo.base.widget.beauty.seekbar.BeautySeekBar;
import com.aliyun.svideo.beauty.queen.R;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyFaceParams;
import com.aliyun.svideo.beauty.queen.constant.QueenBeautyConstants;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyParamsChangeListener;
import com.aliyun.svideo.beauty.queen.inteface.OnBlankViewClickListener;

/**
 * 美颜微调
 *
 */
public class AlivcBeautyDetailSettingView extends LinearLayout {

    private BeautySeekBar mSeekBar;
    private QueenBeautyFaceParams mQueenBeautyFaceParams;
    private int mCheckedPosition = 0;
    /**
     * 美颜美肌参数改变listener
     */
    private OnBeautyParamsChangeListener mBeautyParamsChangeListener;

    private OnBlankViewClickListener mOnBlankViewClickListener;


    public AlivcBeautyDetailSettingView(Context context) {
        this(context, null);

    }

    public AlivcBeautyDetailSettingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlivcBeautyDetailSettingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.alivc_queen_beauty_face_detail_layout, this);
        View tvBack = findViewById(R.id.tv_back);
        mSeekBar = findViewById(R.id.beauty_seekbar);
        TextView blushTv = findViewById(R.id.alivc_base_beauty_blush_textview);
        blushTv.setText(R.string.alivc_base_beauty_sharpen);
        View blankView = findViewById(R.id.blank_view);
        ImageView ivReset = findViewById(R.id.iv_reset);
        RadioGroup rgBeautyFaceGroup = findViewById(R.id.beauty_detail_group);
        rgBeautyFaceGroup.check(R.id.beauty_buffing);
        blankView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBlankViewClickListener != null) {
                    mOnBlankViewClickListener.onBlankClick();
                }
            }
        });
        rgBeautyFaceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.beauty_buffing) {
                    // 磨皮
                    mCheckedPosition = QueenBeautyConstants.BUFFING;
                } else if (checkedId == R.id.beauty_whitening) {
                    // 美白
                    mCheckedPosition = QueenBeautyConstants.WHITENING;
                } else if (checkedId == R.id.beauty_ruddy) {
                    // 锐化
                    mCheckedPosition = QueenBeautyConstants.SHARPEN;
                }
                setRecommendProgress();
                setProgress();
            }
        });

        mSeekBar.setProgressChangeListener(new OnProgresschangeListener() {
            @Override
            public void onProgressChange(int progress) {

                if (mQueenBeautyFaceParams != null) {
                    switch (mCheckedPosition) {
                        case QueenBeautyConstants.BUFFING:
                            if (mQueenBeautyFaceParams.beautyBuffing == progress) {
                                return;
                            }
                            mQueenBeautyFaceParams.beautyBuffing = progress;
                            break;

                        case QueenBeautyConstants.WHITENING:
                            if (mQueenBeautyFaceParams.beautyWhite == progress) {
                                return;
                            }
                            mQueenBeautyFaceParams.beautyWhite = progress;
                            break;

                        case QueenBeautyConstants.SHARPEN:
                            if (mQueenBeautyFaceParams.beautySharpen == progress) {
                                return;
                            }
                            mQueenBeautyFaceParams.beautySharpen = progress;
                            break;

                        default:
                            break;
                    }
                }

                if (mBeautyParamsChangeListener != null) {
                    mBeautyParamsChangeListener.onBeautyFaceChange(mQueenBeautyFaceParams);
                }
            }
        });

        tvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBlankViewClickListener != null) {
                    mOnBlankViewClickListener.onBackClick();
                }
            }
        });

        ivReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.resetProgress();
            }
        });
    }


    public void setParams(QueenBeautyFaceParams params) {
        if (params != null) {
            mQueenBeautyFaceParams = params;
            setRecommendProgress();
            setProgress();
        }
    }

    public void setProgress() {
        if (mQueenBeautyFaceParams != null) {
            switch (mCheckedPosition) {
                case QueenBeautyConstants.BUFFING:
                    mSeekBar.setLastProgress(mQueenBeautyFaceParams.beautyBuffing);
                    break;
                case QueenBeautyConstants.WHITENING:
                    mSeekBar.setLastProgress(mQueenBeautyFaceParams.beautyWhite);
                    break;
                case QueenBeautyConstants.SHARPEN:
                    mSeekBar.setLastProgress(mQueenBeautyFaceParams.beautySharpen);
                    break;
                default:
                    break;
            }
        }
    }


    private void setRecommendProgress() {
        if (mQueenBeautyFaceParams != null) {
            QueenBeautyFaceParams queenBeautyFaceParams = QueenBeautyConstants.BEAUTY_MAP.get(mQueenBeautyFaceParams.beautyLevel);
            if (queenBeautyFaceParams != null) {
                switch (mCheckedPosition) {
                    case QueenBeautyConstants.BUFFING:
                        mSeekBar.setSeekIndicator(queenBeautyFaceParams.beautyBuffing);
                        break;
                    case QueenBeautyConstants.WHITENING:
                        mSeekBar.setSeekIndicator(queenBeautyFaceParams.beautyWhite);
                        break;

                    case QueenBeautyConstants.SHARPEN:
                        mSeekBar.setSeekIndicator(queenBeautyFaceParams.beautySharpen);
                        break;
                    default:
                        break;
                }
            }
        }
    }



    public void setBeautyParamsChangeListener(OnBeautyParamsChangeListener listener) {
        mBeautyParamsChangeListener = listener;
    }


    public void setOnBlankViewClickListener(OnBlankViewClickListener listener) {
        mOnBlankViewClickListener = listener;
    }
}
