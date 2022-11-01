package com.fine.friendlycc.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import com.fine.friendlycc.R;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by ld on 2017/7/25.
 */

public class NineGridLockView extends View {
    private final Context context;
    private final float mDensity;
    private final int mCount = 3;
    private final ArrayList<RectF> mListRectFs;//圆的外形矩形
    private final ArrayList<Point> mListCircle;//外圆的圆心
    private final LinkedHashSet<Integer> mSetPoints;//记录需要连线的外圆圆心点在mListCircle中的索引值，LinkedHashSet线性不可重复 集合，FIFO
    private final Paint mPaint;
    private final Paint miniPaint;//画笔
    private final float mRadius;//外圆半径
    private final float mMinRadius;//内圆半径
    private final float mStrokeWidth = 10; //绘制时的画笔宽度
    int index_point = 0;
    private Point mMovePoint; //记录手指移动时的点
    private Back back;

    public NineGridLockView(Context context) {
        this(context, null);
    }

    public NineGridLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mDensity = getContext().getResources().getDisplayMetrics().density;
        mListRectFs = new ArrayList<>();
        mListCircle = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setStrokeWidth(dp(4));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mRadius = dp(10);
        mPaint.setColor(context.getResources().getColor(R.color.divider));

        miniPaint = new Paint();
        miniPaint.setColor(context.getResources().getColor(R.color.divider));
        miniPaint.setAntiAlias(true);
        miniPaint.setStyle(Paint.Style.FILL);
        miniPaint.setStrokeWidth(dp(4));
        mMinRadius = dp(10);
        mSetPoints = new LinkedHashSet<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAll_Cicle(canvas);
        draw_line(canvas);
    }

    private int dp(int dp) {
        return (int) (dp * mDensity + 0.5f);
    }

    public void setBack(Back back) {
        this.back = back;
    }

    //绘制 连接线
    void draw_line(Canvas canvas) {
        Point p1 = null;
        Point p2 = null;
        //从结合中获取 move过的点
        for (int index : mSetPoints) {
            //当第一个点为null的时候 给第一个点赋值
            if (p1 == null) {
                p1 = mListCircle.get(index);
                //然后 给第二个点赋值  两点一条线
            } else if (p2 == null) {
                p2 = mListCircle.get(index);
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mPaint);
                //p1 挪到p2
                p1 = p2;
                //接下来就是第三个点了 乃至更多点
            } else {
                //p2重新赋值  两点一条线
                p2 = mListCircle.get(index);
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mPaint);
                //p1 挪到p2
                p1 = p2;
            }

        }

        //绘制实时连线
        if (mMovePoint != null && p1 != null) {
            canvas.drawLine(p1.x, p1.y, mMovePoint.x, mMovePoint.y, mPaint);
        }
    }

    //绘制 外圆 内圆
    void drawAll_Cicle(Canvas canvas) {
        for (int i = 0; i < mListRectFs.size(); i++) {
            Point point = mListCircle.get(i);
            //如果move过的 就换颜色
            if (mSetPoints.contains(i)) {
                canvas.drawCircle(point.x, point.y, mRadius, miniPaint);
                canvas.drawCircle(point.x, point.y, mMinRadius, mPaint);

            } else {
                canvas.drawCircle(point.x, point.y, mRadius, mPaint);
                canvas.drawCircle(point.x, point.y, mMinRadius, miniPaint);
            }

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float w = Math.min(getMeasuredWidth(), getMeasuredHeight());
        //      -(--)-(--)-(--)-
        //      -(--)-(--)-(--)-
        //      -(--)-(--)-(--)-
        float padingsss = (getMeasuredWidth() * 1.00f - dp(60)) / 4;

        float rectWH = mRadius * 2;
        mListRectFs.clear();
        mListCircle.clear();
        for (int i = 0; i < mCount; i++) {
            for (int j = 0; j < mCount; j++) {
                RectF rectF = new RectF((3 * i + 1) * mRadius, (3 * j + 1) * mRadius, (3 * i + 3) * mRadius, (3 * j + 3) * mRadius);
                mListRectFs.add(rectF);
                Point point = new Point(((int) padingsss * (i + 1)) + ((int) mRadius) + (2 * i * (int) mRadius), (int) padingsss * (j + 1));
//                Log.i("debug","w位置x："+((int) padingsss * (i + 1)) + ((int) mRadius)+(2*i * (int)mRadius));
//                Log.i("debug","padingsss："+padingsss);

                mListCircle.add(point);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float point_x = event.getX();
        float point_y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                break;
            case MotionEvent.ACTION_UP:
                if (back != null) {
                    StringBuffer sb = new StringBuffer();
                    for (Integer integer : mSetPoints) {
                        sb.append(integer);
                    }
                    back.backPassword(sb.toString());
                }
                CountDownTimer timer = new CountDownTimer(400, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        mMovePoint = null;
                        mSetPoints.clear();
                        invalidate();
                    }
                }.start();
                break;
            case MotionEvent.ACTION_MOVE:

                int index = touchIndex(point_x, point_y);
                if (mMovePoint == null) {
                    mMovePoint = new Point((int) point_x, (int) point_y);
                } else {
                    mMovePoint.set((int) point_x, (int) point_y);
                }
                if (index != -1) {
                    boolean isHas = false;
                    for (Integer integer : mSetPoints) {
                        if (index == integer) {
                            isHas = true;
                        }
                    }
                    if (!isHas) {
                        Vibrator vibrator = (Vibrator) this.context.getSystemService(VIBRATOR_SERVICE);
                        vibrator.vibrate(100);
                    }
                    mSetPoints.add(index);
                    if (index_point != mSetPoints.size()) {
                        index_point = mSetPoints.size();
                        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    }
                }
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }


    /**
     * 判断触摸点在哪个item上
     *
     * @param x
     * @param y
     * @return
     */
    private int touchIndex(float x, float y) {
        for (int i = 0; i < mListCircle.size(); i++) {
            Point p = mListCircle.get(i);
            if (isCollision(x, y, p.x, p.y, mRadius)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 点和圆形碰撞检测
     *
     * @param x1     手指接触点
     * @param y1     手指接触点
     * @param x2     外圆
     * @param y2     外圆
     * @param radius 半径
     * @return
     */
    private boolean isCollision(float x1, float y1, float x2, float y2, float radius) {
        // 如果点和圆心距离小于或等于半径则认为发生碰撞
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= (radius * 3);
    }

    public interface Back {
        void backPassword(String password);
    }
}