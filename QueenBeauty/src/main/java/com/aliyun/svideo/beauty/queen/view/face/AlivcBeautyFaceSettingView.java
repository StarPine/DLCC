package com.aliyun.svideo.beauty.queen.view.face;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aliyun.svideo.beauty.queen.R;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyDetailClickListener;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyFaceLevelChangeListener;

/**
 * 美颜等级设置
 */
public class AlivcBeautyFaceSettingView extends FrameLayout {
    private RadioGroup mRgAdvancedGroup;
    private OnBeautyFaceLevelChangeListener mOnBeautyFaceLevelChangeListener;
    private OnBeautyDetailClickListener mOnBeautyDetailClickListener;
    private int mCurrentLevel;

    public AlivcBeautyFaceSettingView(Context context) {
        this(context, null);
    }

    public AlivcBeautyFaceSettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public AlivcBeautyFaceSettingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.alivc_queen_beauty_level_view, this);
        TextView mTvCopyright = findViewById(R.id.tv_def_copyright);
        ImageView mBtBeautyDetail = findViewById(R.id.iv_beauty_detail);
        mRgAdvancedGroup = findViewById(R.id.beauty_advanced_group);
        mBtBeautyDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBeautyDetailClickListener != null){
                    mOnBeautyDetailClickListener.onDetailClick(mCurrentLevel);
                }

            }
        });

        mRgAdvancedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mOnBeautyFaceLevelChangeListener != null) {
                    mCurrentLevel = switchIdToLevel(checkedId);
                    mOnBeautyFaceLevelChangeListener.onLevelChanged(mCurrentLevel);
                }

            }
        });
        initCopyRight(mTvCopyright);
    }


    /**
     * 设置默认值
     *
     * @param level [0,5]
     */
    public void setDefaultSelect(int level) {
        if (level < 0 || level > 5) {
            return;
        }
        mCurrentLevel = level;
        advancedCheck(level);

    }

    private void advancedCheck(int position) {
        int advanceId;
        switch (position) {
            case 0:
                advanceId = R.id.beauty_advanced_0;
                break;
            case 1:
                advanceId = R.id.beauty_advanced_1;
                break;

            case 2:
                advanceId = R.id.beauty_advanced_2;
                break;

            case 3:
                advanceId = R.id.beauty_advanced_3;
                break;

            case 4:
                advanceId = R.id.beauty_advanced_4;
                break;

            case 5:
                advanceId = R.id.beauty_advanced_5;
                break;

            default:
                advanceId = R.id.beauty_advanced_3;
                break;
        }
        if (mRgAdvancedGroup != null) {
            mRgAdvancedGroup.check(advanceId);
        }
    }

    private int switchIdToLevel(int checkedId) {
        int index = 0;
        if (checkedId == R.id.beauty_advanced_0) {
            index = 0;
        } else if (checkedId == R.id.beauty1 || checkedId == R.id.beauty_advanced_1) {
            index = 1;
        } else if (checkedId == R.id.beauty2 || checkedId == R.id.beauty_advanced_2) {
            index = 2;
        } else if (checkedId == R.id.beauty3 || checkedId == R.id.beauty_advanced_3) {
            index = 3;
        } else if (checkedId == R.id.beauty4 || checkedId == R.id.beauty_advanced_4) {
            index = 4;
        } else if (checkedId == R.id.beauty5 || checkedId == R.id.beauty_advanced_5) {
            index = 5;
        }
        return index;
    }

    private void initCopyRight(TextView tvCopyright) {
        if (tvCopyright != null) {
            tvCopyright.setVisibility(VISIBLE);
            String copyright = getContext().getResources().getString(R.string.alivc_base_beauty_queen_copyright);
            tvCopyright.setText(copyright);
        }
    }

    public void setOnBeautyFaceLevelChangeListener(OnBeautyFaceLevelChangeListener onBeautyFaceLevelChangeListener) {
        this.mOnBeautyFaceLevelChangeListener = onBeautyFaceLevelChangeListener;
    }

    public void setOnBeautyDetailClickListener(OnBeautyDetailClickListener onBeautyDetailClickListener) {
        mOnBeautyDetailClickListener = onBeautyDetailClickListener;
    }
}
