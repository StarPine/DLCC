package com.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Author: 彭石林
 * Time: 2021/10/12 16:16
 * Description: This is CustonMideaSource
 */
public class CustonMideaSource extends ImageView {

    private final int screenWidth;
    private final int screenHeight;
    /**
     * 按下时浮层x坐标
     */
    float downViewX = 0;
    //浮动按钮按下时x坐标
    private float downX;
    //浮动按钮按下时y坐标
    private float downY;
    //记录最后拖动的  上、左距离
    private int lastTop = 0;
    private int lastLeft = 0;

    private final Context context;

    public CustonMideaSource(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustonMideaSource(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        screenWidth = getScreenWidth(context);
        screenHeight = getScreenHeight(context);
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
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
//            //layoutParams.leftMargin = getLeft() + offsetX;
//            layoutParams.topMargin = screenHeight - start;
//            setLayoutParams(layoutParams);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float movex = 0;
        float movey = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                downX = event.getX();
                downY = event.getY();
                downViewX = this.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动
                //移动的距离
                float moveX = event.getX() - downX;// event.getX() 移动的X距离
                float moveY = event.getY() - downY;// event.getY() 移动的Y距离
                //当前view= X,Y坐标
                float viewX = this.getX();
                float viewY = this.getY();
                //view的宽高
                int viewHeigth = this.getWidth();
                int viewWidth = this.getHeight();
                //X当超出屏幕,取最大值
                if (viewX + moveX + viewWidth > screenWidth) {
                    //靠右
                    lastLeft = (screenWidth - viewWidth);
                } else if (viewX + moveX <= 0) {
                    //靠右
                    lastLeft = (0);
                } else {
                    //正常
                    lastLeft = (int) (viewX + moveX);
                }
                //Y当超出屏幕,取最大值
                if (viewY + moveY + viewHeigth > screenHeight) {
                    //靠下
                    lastTop = (screenHeight - viewHeigth);
                } else if (viewY + moveY <= 0) {
                    //靠上
                    lastTop = (0);
                } else {
                    //正常
                    lastTop = (int) (viewY + moveY);
                }
                this.setY(lastTop);
                this.setX(lastLeft);
                break;
            case MotionEvent.ACTION_UP:
                // Log.e("X轴", lastTop + "=======" + lastLeft);
                //setParentLayout(this);
                //避免滑出触发点击事件
                if ((int) (event.getRawX() - movex) != 0
                        || (int) (event.getRawY() - movey) != 0) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
    //获取距离
    private static float getDistance(MotionEvent event) {//获取两点间距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}