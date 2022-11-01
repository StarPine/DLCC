package com.fine.friendlycc.ui.task.record.list;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.fine.friendlycc.utils.MyScreenUtils;

import java.lang.reflect.Method;

public class MoveImage extends androidx.appcompat.widget.AppCompatImageView {
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
    private TouchEvent $touchEvent = null;

    private boolean isBottomHeight = true;
    private int BottomHeight = 0;

    public MoveImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenWidth = MyScreenUtils.getScreenWidth(context);
        screenHeight = MyScreenUtils.getScreenHeight(context);
        isBottomHeight = checkDeviceHasNavigationBar(context);
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        BottomHeight = context.getResources().getDimensionPixelSize(resourceId);

    }

    public void setTouchEventCallvack(TouchEvent touchEvent) {
        $touchEvent = touchEvent;
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
                if ($touchEvent != null) {
                    $touchEvent.ACTION_UP();
                }
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

    /**
     * 改变父布局位置以防止重绘后回到原点的问题
     */
    private void setParentLayout(View v) {
        // 每次移动都要设置其layout，不然由于父布局可能嵌套listview，当父布局发生改变冲毁（如下拉刷新时）则移动的view会回到原来的位置
        RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                v.getWidth(), v.getHeight());
        int top = this.getTop();
        int bottom = this.getBottom();
        if (isBottomHeight) {
            top -= BottomHeight * 1.7;
        }
        if (top <= 10) {
            top = BottomHeight;
        }
        lpFeedback.setMargins(this.getLeft(), top, this.getRight(), this.getBottom());
        v.setLayoutParams(lpFeedback);
    }

    //获取是否存在NavigationBar
    public boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    public interface TouchEvent {
        void ACTION_DOWN();

        void ACTION_MOVE();

        void ACTION_UP();
    }
}