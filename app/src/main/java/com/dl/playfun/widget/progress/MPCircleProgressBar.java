package com.dl.playfun.widget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import com.dl.playfun.R;

/**
 * Author: 彭石林
 * Time: 2022/9/12 18:02
 * Description: 圆形进度条控件
 */
public class MPCircleProgressBar  extends View{

    private int _current = 1, _max = 100;
    //圆弧（也可以说是圆环）的宽度
    private float _arcWidth = 30;
    //控件的宽度
    private float _width;
    private int _txtWidth;

    public MPCircleProgressBar(Context context) {
        this(context,null);
    }

    public MPCircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MPCircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MPCircleProgressBar, defStyleAttr,0);
        _arcWidth = Math.round(a.getDimension(R.styleable.MPCircleProgressBar_width, 0));
        _txtWidth = Math.round(a.getDimension(R.styleable.MPCircleProgressBar_txtWidth, 0));
        a.recycle();
    }
    public void SetCurrent(int _current) {
        this._current = _current;
        invalidate();
    }

    public void SetMax(int _max) {
        this._max = _max;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //getMeasuredWidth获取的是view的原始大小，也就是xml中配置或者代码中设置的大小
        //getWidth获取的是view最终显示的大小，这个大小不一定等于原始大小
        _width = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 对于画笔
        Paint paint = new Paint();
        // 设置抗锯齿
        paint.setAntiAlias(true);
        // 设置画笔颜色
        // 三种样式--Stroke 只要描边 Fill 填充 FILL_AND_STROKE和既有描边又有填充
        paint.setStyle(Paint.Style.STROKE);
        //设置描边宽度
        paint.setStrokeWidth(_arcWidth);
        //定义外圈员的颜色
        paint.setColor(Color.BLACK);
        paint.setAlpha(71);
        //大圆的半径
        float bigCircleRadius = _width / 2;
        //小圆的半径
        float smallCircleRadius = bigCircleRadius - _arcWidth;
        //绘制圆形进度条--获取当前控件多大，正好让进度条在这个控件区间内
        canvas.drawCircle(_width/2, _width/2, smallCircleRadius, paint);
        //重新设置描边宽度，这个宽度最好能完全盖过圆形
        paint.setStrokeWidth(_arcWidth);
        //定义限制圆弧的矩形，当前这样定义正好让圆弧和圆重合
        RectF oval = new RectF(_arcWidth, _arcWidth, _width - _arcWidth, _width - _arcWidth);
        //设置进度条(圆弧的颜色)
        paint.setColor(Color.WHITE);
        //绘制，设置进度条的度数从0开始，结束值是个变量，可以自己自由设置，来设置进度
        //true和false 代表是否使用中心点，如果true，代表连接中心点，会出现扇形的效果
        canvas.drawArc(oval, 270, 360 * _current / _max, false, paint);
        //文字的绘制
        paint.setTextSize(40);
        //设置文字宽度
        paint.setStrokeWidth(1.0f);
        //测量文字大小-提前准备个矩形
        Rect bounds = new Rect();
        String txt = _current * 100 / _max + "%";
        //测量文字的宽和高，测量的值可以根据矩形获取
        paint.getTextBounds(txt, 0, txt.length(), bounds);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        //绘制文字，计算文字的宽高进行设置
        canvas.drawText(txt, _width/2 - bounds.width() / 2,
                _width/2 + bounds.height() / 2, paint);
    }
}
