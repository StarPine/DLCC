package com.aliyun.svideo.beauty.queen.view.skin;

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
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyShapeParams;
import com.aliyun.svideo.beauty.queen.constant.QueenBeautyConstants;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyParamsChangeListener;
import com.aliyun.svideo.beauty.queen.inteface.OnBlankViewClickListener;


/**
 * 美型微调view
 *
 * @author xlx
 */
public class BeautyShapeDetailSettingView extends LinearLayout {
    /**
     * 美颜美肌参数, 包括磨皮, 美白, 红润, 大眼, 瘦脸
     */

    private BeautySeekBar mSeekBar;
    private OnBlankViewClickListener mOnBlankViewClickListener;
    private OnBeautyParamsChangeListener mOnBeautyParamsChangeListener;
    private QueenBeautyShapeParams mQueenBeautyShapeParams;
    private int mCheckedPosition = QueenBeautyConstants.CUT_FACE;
    private TextView mTvBack;

    public BeautyShapeDetailSettingView(Context context) {
        this(context, null);

    }

    public BeautyShapeDetailSettingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeautyShapeDetailSettingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.alivc_queen_beauty_shape_detail_layout, this);
        mTvBack = findViewById(R.id.tv_back);
        mSeekBar = findViewById(R.id.beauty_seekbar);
        mSeekBar.setMin(-100f);
        mSeekBar.setBackSeekMin(-100);
        View blankView = findViewById(R.id.blank_view);
        ImageView mIvReset = findViewById(R.id.iv_reset);
        RadioGroup rgBeautyFaceGroup = findViewById(R.id.beauty_detail_shape_group);
        rgBeautyFaceGroup.check(R.id.beauty_cut_face);

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
                if (checkedId == R.id.beauty_cut_face) {
                    // 窄脸
                    mSeekBar.setMin(-100f);
                    mSeekBar.setBackSeekMin(-100);
                    mCheckedPosition = QueenBeautyConstants.CUT_FACE;

                } else if (checkedId == R.id.beauty_thin_face) {
                    // 瘦脸
                    mSeekBar.setMin(0);
                    mSeekBar.setBackSeekMin(0);
                    mCheckedPosition = QueenBeautyConstants.THIN_FACE;

                } else if (checkedId == R.id.beauty_long_face) {
                    // 脸长
                    mSeekBar.setMin(-100f);
                    mSeekBar.setBackSeekMin(-100);
                    mCheckedPosition = QueenBeautyConstants.LONG_FACE;

                } else if (checkedId == R.id.beauty_lower_jaw) {
                    // 缩下巴
                    mSeekBar.setMin(-100f);
                    mSeekBar.setBackSeekMin(-100);
                    mCheckedPosition = QueenBeautyConstants.LOWER_JAW;

                } else if (checkedId == R.id.beauty_big_eye) {
                    // 大眼
                    mSeekBar.setMin(0);
                    mSeekBar.setBackSeekMin(0);
                    mCheckedPosition = QueenBeautyConstants.BIG_EYE;

                } else if (checkedId == R.id.beauty_thin_nose) {
                    // 瘦鼻
                    mSeekBar.setMin(-100f);
                    mSeekBar.setBackSeekMin(-100);
                    mCheckedPosition = QueenBeautyConstants.THIN_NOSE;

                } else if (checkedId == R.id.beauty_mouth_width) {
                    // 唇宽
                    mSeekBar.setMin(-100f);
                    mSeekBar.setBackSeekMin(-100);
                    mCheckedPosition = QueenBeautyConstants.MOUTH_WIDTH;

                } else if (checkedId == R.id.beauty_thin_mandible) {
                    // 下颌
                    mSeekBar.setMin(-100f);
                    mSeekBar.setBackSeekMin(-100);
                    mCheckedPosition = QueenBeautyConstants.THIN_MANDIBLE;

                } else if (checkedId == R.id.beauty_cut_cheek) {
                    // 颧骨
                    mSeekBar.setMin(-100f);
                    mSeekBar.setBackSeekMin(-100);
                    mCheckedPosition = QueenBeautyConstants.CUT_CHEEK;

                }
                changeSeekBarData(mQueenBeautyShapeParams);
            }
        });


        mSeekBar.setProgressChangeListener(new OnProgresschangeListener() {
            @Override
            public void onProgressChange(int progress) {
                if (mQueenBeautyShapeParams != null) {
                    switch (mCheckedPosition) {
                        case QueenBeautyConstants.CUT_FACE:
                            if (mQueenBeautyShapeParams.beautyCutFace == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyCutFace = progress;
                            break;

                        case QueenBeautyConstants.THIN_FACE:
                            if (mQueenBeautyShapeParams.beautyThinFace == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyThinFace = progress;
                            break;

                        case QueenBeautyConstants.LONG_FACE:
                            if (mQueenBeautyShapeParams.beautyLongFace == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyLongFace = progress;
                            break;

                        case QueenBeautyConstants.LOWER_JAW:
                            if (mQueenBeautyShapeParams.beautyLowerJaw == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyLowerJaw = progress;
                            break;

                        case QueenBeautyConstants.BIG_EYE:
                            if (mQueenBeautyShapeParams.beautyBigEye == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyBigEye = progress;
                            break;

                        case QueenBeautyConstants.THIN_NOSE:
                            if (mQueenBeautyShapeParams.beautyThinNose == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyThinNose = progress;
                            break;

                        case QueenBeautyConstants.MOUTH_WIDTH:
                            if (mQueenBeautyShapeParams.beautyMouthWidth == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyMouthWidth = progress;
                            break;

                        case QueenBeautyConstants.THIN_MANDIBLE:
                            if (mQueenBeautyShapeParams.beautyThinMandible == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyThinMandible = progress;
                            break;

                        case QueenBeautyConstants.CUT_CHEEK:
                            if (mQueenBeautyShapeParams.beautyCutCheek == progress) {
                                return;
                            }
                            mQueenBeautyShapeParams.beautyCutCheek = progress;
                            break;
                        default:
                            break;
                    }
                }

                if (mOnBeautyParamsChangeListener != null) {
                    mOnBeautyParamsChangeListener.onBeautyShapeChange(mQueenBeautyShapeParams);
                }
            }
        });

        mTvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBlankViewClickListener != null) {
                    mOnBlankViewClickListener.onBackClick();
                }
            }
        });

        mIvReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.resetProgress();
            }
        });
    }



    public void setBeautyConstants(int beautyConstants) {
        this.mCheckedPosition = beautyConstants;
    }


    public void setParams(QueenBeautyShapeParams queenBeautyShapeParams) {
        mQueenBeautyShapeParams = queenBeautyShapeParams;
        changeSeekBarData(queenBeautyShapeParams) ;
    }

    private void changeSeekBarData(QueenBeautyShapeParams queenBeautyShapeParams) {
        if (queenBeautyShapeParams == null){
            return;
        }
        switch (queenBeautyShapeParams.shapeType) {
            case QueenBeautyConstants.SHAPE_CUSTOM_FACE:
                mTvBack.setText(R.string.alivc_base_beauty_custom_face);
                break;
            case QueenBeautyConstants.SHAPE_GRACE_FACE:
                mTvBack.setText(R.string.alivc_base_beauty_grace_face);
                break;
            case QueenBeautyConstants.SHAPE_FINE_FACE:
                mTvBack.setText(R.string.alivc_base_beauty_fine_face);
                break;
            case QueenBeautyConstants.SHAPE_CELEBRITY_FACE:
                mTvBack.setText(R.string.alivc_base_beauty_celebrity_face);
                break;
            case QueenBeautyConstants.SHAPE_LOVELY_FACE:
                mTvBack.setText(R.string.alivc_base_beauty_lovely_face);
                break;
            case QueenBeautyConstants.SHAPE_BABY_FACE:
                mTvBack.setText(R.string.alivc_base_beauty_baby_face);
                break;
            default:
                mTvBack.setText(R.string.alivc_base_beauty_custom_face);
                break;
        }
        QueenBeautyShapeParams defaultShapeParams = QueenBeautyConstants.BEAUTY_SHAPE_DEFAULT_MAP.get(mQueenBeautyShapeParams.shapeType);
        if (defaultShapeParams == null){
            return;
        }
        switch (mCheckedPosition) {
            case QueenBeautyConstants.CUT_FACE:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyCutFace);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyCutCheek);
                break;

            case QueenBeautyConstants.THIN_FACE:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyThinFace);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyThinFace);
                break;

            case QueenBeautyConstants.LONG_FACE:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyLongFace);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyLongFace);
                break;

            case QueenBeautyConstants.LOWER_JAW:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyLowerJaw);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyLowerJaw);
                break;

            case QueenBeautyConstants.BIG_EYE:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyBigEye);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyBigEye);
                break;

            case QueenBeautyConstants.THIN_NOSE:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyThinNose);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyThinNose);
                break;

            case QueenBeautyConstants.MOUTH_WIDTH:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyMouthWidth);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyMouthWidth);
                break;

            case QueenBeautyConstants.THIN_MANDIBLE:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyThinMandible);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyThinMandible);
                break;

            case QueenBeautyConstants.CUT_CHEEK:
                mSeekBar.setSeekIndicator(defaultShapeParams.beautyCutCheek);
                mSeekBar.setLastProgress(mQueenBeautyShapeParams.beautyCutCheek);
                break;

            default:
                break;
        }
    }


    public void setOnBlankViewClickListener(OnBlankViewClickListener onBlankViewClickListener) {
        mOnBlankViewClickListener = onBlankViewClickListener;
    }

    public void setOnBeautyParamsChangeListener(OnBeautyParamsChangeListener onBeautyParamsChangeListener) {
        mOnBeautyParamsChangeListener = onBeautyParamsChangeListener;
    }
}
