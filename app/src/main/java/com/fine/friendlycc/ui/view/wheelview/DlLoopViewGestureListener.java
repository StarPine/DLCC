package com.fine.friendlycc.ui.view.wheelview;

import android.view.MotionEvent;


/**
 * 手势监听
 */
public final class DlLoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final DlWheelView wheelView;


    public DlLoopViewGestureListener(DlWheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}
