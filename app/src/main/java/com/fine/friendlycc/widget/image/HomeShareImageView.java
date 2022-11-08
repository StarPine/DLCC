package com.fine.friendlycc.widget.image;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Author: 彭石林
 * Time: 2021/12/23 10:39
 * Description: This is HomeShareImageView
 */
public class HomeShareImageView extends androidx.appcompat.widget.AppCompatImageView {

    private final Context context;
    private final int screenHeight;
    private final int screenWidth;
    private final int top;
    private final int bottom;
    private final int start;
    private float startX;
    private float startY;
    private boolean isOnclick;

    public HomeShareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeShareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        screenHeight = getScreenHeight(context);
        screenWidth = getScreenWidth(context);
        top = dp2px(context, 20);
        bottom = dp2px(context, 100);
        start = dp2px(context, 280);
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int dp2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float dp2px_f(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            //layoutParams.leftMargin = getLeft() + offsetX;
            layoutParams.topMargin = screenHeight - start;
            setLayoutParams(layoutParams);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int touchSlop = 2;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isOnclick = true;
                startX = event.getRawX();
                startY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX();
                float Y = event.getRawY();
                // 偏移量
                int offsetX = (int) (x - startX);
                int offsetY = (int) (Y - startY);
                if (Math.abs(offsetY) > touchSlop) {
                    isOnclick = false;
                }
                // 增量更新
                int margin = getTop() + offsetY;
                int marginLeft = getLeft() + offsetX;
                if (marginLeft < 50) {
                    marginLeft = 50;
                }
                if (marginLeft > (screenWidth - getWidth())) {
                    marginLeft = screenWidth - getWidth();
                }
                if (margin > top && margin < (screenHeight - bottom)) {
                    //layout(getLeft(), getTop()+offsetY, getRight(),getBottom() + offsetY);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                    layoutParams.leftMargin = marginLeft;
                    layoutParams.topMargin = margin;
                    setLayoutParams(layoutParams);
                }
                // 更新起始点
                startX = x;
                startY = Y;
                break;

            case MotionEvent.ACTION_UP:
                if (isOnclick) {
                    performClick();
                }
                break;
        }
        return true;
    }
}