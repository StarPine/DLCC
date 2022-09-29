package com.custom.view.circle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.custom.view.R;

/**
 * @since : 2021/9/9
 * 作用 : 显示效果为圆形进度条，确定的实时进度条，1000ms至少更新3次
 * 仿网易云音乐app主页的圆形进度条，为用户显示当前的音乐播放进度
 */
public class CircleProgressBar extends View {

    private static final String TAG = "CircleProgressBar";

    //画笔
    private Paint mPaint;
    //圆形图片的半径
    private float mRadius;
    //圆形进度条颜色
    private int fullColor = 0x20000000;
    private int progressColor = 0xFF000000;
    //圆形进度条宽度
    private float strokeWidth = 3.6f;
    private float distanceBoundary = 8f;
    //圆形进度：音乐播放进度
    private float progress = 0;
    private RectF mRectF;


    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            Log.d(TAG, "CircleProgressBar: 读取布局文件参数");
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
            fullColor = ta.getInt(R.styleable.CircleProgressBar_color_bg,0x20000000);
            progressColor = ta.getInt(R.styleable.CircleProgressBar_color_progress,0xFF000000);
            strokeWidth = ta.getFloat(R.styleable.CircleProgressBar_width_stroke,3.6f);
            distanceBoundary = ta.getFloat(R.styleable.CircleProgressBar_distance_boundary,8f);
            ta.recycle();
        }
        fullColor = context.getResources().getColor(R.color.white);
        progressColor = context.getResources().getColor(R.color.circle_border_z);
        progress =0;
        if (mPaint == null) initPaint();
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "CircleProgressBar: 3");
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        //Log.d(TAG, "onMeasure: Width = "+width+",Height"+height);
        //由于是圆形，宽高应保持一致
        int size = Math.min(width, height);
        //Log.d(TAG, "onMeasure: size = "+size);
        mRadius = size / 2f;
        setMeasuredDimension(size, size);//测量高度和宽度
        mRectF = new RectF(strokeWidth + distanceBoundary,
                            strokeWidth + distanceBoundary,
                            width - strokeWidth - distanceBoundary,
                            height - strokeWidth - distanceBoundary);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d(TAG, "onDraw: "+mRadius+", 控件宽度 = "+getWidth()+", 控件高度 = "+getHeight());
        //画圆框进度条底框,radius = radius - 8 - strokeWidth
        canvas.drawCircle(mRadius,mRadius,mRadius - strokeWidth - distanceBoundary,mPaint);
        mPaint.setColor(progressColor);
        canvas.drawArc(mRectF,-90,progress,false,mPaint);
        mPaint.setColor(fullColor);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Log.d(TAG, "onDetachedFromWindow: ");释放资源
        if (mPaint != null) {
            mPaint.reset();
            mPaint = null;
        }
        if (mRectF != null) mRectF = null;

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(fullColor);       //设置画笔颜色
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);  //设置画笔模式为描边
        mPaint.setStrokeWidth(strokeWidth);   //设置画笔宽度
    }

    /**
     * 更新界面
     * @param progressPercentage 音乐播放的进度占总进度的百分比
     * 建议 1秒 | 1000毫秒 执行三次
     */
    public void setProgress(float progressPercentage)
    {
        if (progressPercentage < 0f || progressPercentage > 1f) return;
        this.progress = progressPercentage * 360f;
        //在事件循环的后续循环中导致失效。用此选项可使非UI线程中的视图无效
        //仅当此视图附着到窗口时,此方法可以从UI线程外部调用。</p
        postInvalidate();//绘制刷新
    }
    //重置当前进度
    public void  resetView(){
        setProgress(0);
    }
}
