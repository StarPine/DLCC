package com.fine.friendlycc.widget.recyclerview.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.R;

/**
 * Author: 彭石林
 * Time: 2022/7/28 16:25
 * Description: This is BannerRecyclerView
 */
public class BannerRecyclerView extends RecyclerView {

    private int autoPlayDuration;//刷新间隔时间
    //播放
    private int WHAT_AUTO_PLAY = 1000;
    //当前下标
    private int currentIndex;
    //轮班banner辅助类
    private BannerLayoutManager mLayoutManager;

    private boolean isAutoPlaying = true;
    int itemSpace;
    float centerScale;
    float moveSpeed;

    private boolean hasInit;
    private int bannerSize = 1;
    private boolean isPlaying = false;

    public BannerRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public BannerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MisterPBannerLayout);
        autoPlayDuration = a.getInt(R.styleable.MisterPBannerLayout_interval, 4000);
        isAutoPlaying = a.getBoolean(R.styleable.MisterPBannerLayout_autoPlaying, true);
        itemSpace = a.getInt(R.styleable.MisterPBannerLayout_itemSpace, 20);
        centerScale = a.getFloat(R.styleable.MisterPBannerLayout_centerScale, 1.2f);
        moveSpeed = a.getFloat(R.styleable.MisterPBannerLayout_moveSpeed, 1.0f);
        a.recycle();
        mLayoutManager = new BannerLayoutManager(getContext(), OrientationHelper.HORIZONTAL);
        mLayoutManager.setItemSpace(itemSpace);
        mLayoutManager.setCenterScale(centerScale);
        mLayoutManager.setMoveSpeed(moveSpeed);
        setLayoutManager(mLayoutManager);
        new CenterSnapHelper().attachToRecyclerView(this);
    }

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                if (currentIndex == mLayoutManager.getCurrentPosition()) {
                    ++currentIndex;
                   // Log.e("当前向下个页面的坐标","==========="+currentIndex);
                    //修正列表再次填充时。获取的子view 不为null在执行滚动
                    try{
                        scrollToPosition(currentIndex);
                    }catch (Exception ignored){

                    }
                    mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
                }
            }
            return false;
        }
    });

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    public synchronized void setPlaying(boolean playing) {
        if (isAutoPlaying && hasInit) {
            if (!isPlaying && playing) {
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
                isPlaying = true;
            } else if (isPlaying && !playing) {
                mHandler.removeMessages(WHAT_AUTO_PLAY);
                isPlaying = false;
            }
        }
    }


    /**
     * 设置轮播数据集
     */
    public void setAdapter(Adapter adapter) {
        hasInit = false;
        if(adapter==null){
            return;
        }
        if( adapter.getItemCount()<=0){
            return;
        }
        //如果存在历史adapter不为null。并且数量大雨0 则暂停轮播
        if(getAdapter()!=null && getAdapter().getItemCount()>0){
            mHandler.removeMessages(WHAT_AUTO_PLAY);
        }
        super.setAdapter(adapter);
        bannerSize = adapter.getItemCount();
        mLayoutManager.setInfinite(bannerSize >= 3);
        currentIndex = mLayoutManager.getCurrentPosition();
        hasInit = true;
        setPlaying(true);
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dx != 0) {
                    setPlaying(false);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int first = mLayoutManager.getCurrentPosition();
                if (currentIndex != first) {
                    currentIndex = first;
                }
                if (newState == SCROLL_STATE_IDLE) {
                    setPlaying(true);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        setPlaying(visibility == VISIBLE);
    }

}
