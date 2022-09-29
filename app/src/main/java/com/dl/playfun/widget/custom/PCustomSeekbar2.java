package com.dl.playfun.widget.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.dl.playfun.R;

import java.util.ArrayList;

/**
 * Author: 彭石林
 * Time: 2022/6/20 10:26
 * Description: This is PCustomSeekbar
 */
public class PCustomSeekbar2 extends View {
    private final String TAG = "CustomSeekbar";
    private final int[] colors = new int[]{R.color.pseekbar_process, R.color.pseekbar_process_off, R.color.pseekbar_process_off2};//进度条的橙色,进度条的灰色,字体的灰色
    private int currentViewWidth;
    private int currentViewHeight;
    private int thumbHeight = 0;
    private final int upX = 0;
    private Paint mPaint;
    private Paint mProcessPaint;
    private Paint mProcessOffPaint;
    private Paint mTextPaint;
    private Paint mTextPaint2;
    private Paint mTextPaint3;
    private Paint buttonPaint;
    private Canvas canvas;
    private Bitmap bitmap;
    private Bitmap thumb;
    private Bitmap spot;
    private Bitmap spot_on;
    private Bitmap spot_off;
    private int cur_sections = 0;
    private int bitMapHeight = 38;//第一个点的起始位置起始，图片的长宽是76，所以取一半的距离
    private int textMove = 60;//字与下方点的距离，因为字体字体是40px，再加上10的间隔
    //绘制线的高
    private int lineHeight = 0;
    //每个线节点的宽
    private int processWidth = 0;
    //每个item宽
    private int itemWidth = 0;
    private int textSize;
    private ArrayList<String> section_title;
    private int maxIndex = 0;

    private MeasureWidthCallBack measureWidthCallBack;

    public PCustomSeekbar2(Context context) {
        super(context);
    }

    public PCustomSeekbar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public PCustomSeekbar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private static Bitmap drawableToBitamp(Drawable drawable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap drawableToBitamp(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PCustomSeekBar);
        cur_sections = 0;
        bitmap = Bitmap.createBitmap(900, 900, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        Drawable thumbSrc = array.getDrawable(R.styleable.PCustomSeekBar_thumbSrc);
        thumbHeight = Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgThumbWidth, 0));
        thumbSrc.setBounds(0, 0, Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgThumbWidth, 0)), thumbHeight);
        thumb = drawableToBitamp(thumbSrc, Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgThumbWidth, 0)), thumbHeight);
        //thumb.setWidth(Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgThumbWidth,0)));
        //thumb.setHeight(Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgThumbHeight,0)));

        Drawable spotSrc = array.getDrawable(R.styleable.PCustomSeekBar_imgSpotSrc);
        spot = drawableToBitamp(spotSrc);
        //spot.setWidth(Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgSpotWidth,0)));
        //spot.setHeight(Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgSpotHeight,0)));

        Drawable spot_onSrc = array.getDrawable(R.styleable.PCustomSeekBar_imgSpotOnSrc);
        spot_on = drawableToBitamp(spot_onSrc);
        Drawable spot_offSrc = array.getDrawable(R.styleable.PCustomSeekBar_imgSpotOffSrc);
        spot_off = drawableToBitamp(spot_offSrc);
        //spot_on.setWidth(Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgSpotOnWidth,0)));
        //spot_on.setHeight(Math.round(array.getDimension(R.styleable.PCustomSeekBar_imgSpotOnHeight,0)));
        bitMapHeight = thumbHeight / 2;
        textMove = bitMapHeight + 22;
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);//锯齿不显示
        mPaint.setStrokeWidth(10);
        mProcessPaint = new Paint(Paint.DITHER_FLAG);
        mProcessPaint.setAntiAlias(true);//锯齿不显示
        mProcessPaint.setStrokeWidth(10);
        mProcessOffPaint = new Paint(Paint.DITHER_FLAG);
        mProcessOffPaint.setAntiAlias(true);//锯齿不显示
        mProcessOffPaint.setStrokeWidth(10);
        mTextPaint = new Paint(Paint.DITHER_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(getContext().getResources().getColor(R.color.black));

        mTextPaint2 = new Paint(Paint.DITHER_FLAG);
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setTextSize(textSize);
        mTextPaint2.setColor(getContext().getResources().getColor(R.color.pseekbar_process));

        mTextPaint3 = new Paint(Paint.DITHER_FLAG);
        mTextPaint3.setAntiAlias(true);
        mTextPaint3.setTextSize(textSize);
        mTextPaint3.setColor(getContext().getResources().getColor(R.color.pseekbar_process_off2));


        buttonPaint = new Paint(Paint.DITHER_FLAG);
        buttonPaint.setAntiAlias(true);
        lineHeight = dp2px(context, 5);
        array.recycle();
    }

    /**
     * 实例化后调用，设置bar的段数和文字
     */
    public void initData(ArrayList<String> section, int max) {
        if (section != null && section.size() > 0) {
            section_title = section;
            this.maxIndex = max;
        }
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        currentViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        currentViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(currentViewWidth, currentViewHeight);
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (section_title != null) {
            //背景色白色
            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAlpha(0);
            int size = section_title.size();
            itemWidth = spot_on.getWidth();
            int widths = (itemWidth / 2);

            mPaint.setAlpha(255);
            mPaint.setColor(getContext().getResources().getColor(colors[1]));
            mProcessPaint.setColor(getContext().getResources().getColor(colors[0]));
            mProcessOffPaint.setColor(getContext().getResources().getColor(colors[2]));

            //有内容后。应该计算出每个item节点的宽  view宽度 / 数量 =得到每节的长度
            // 在减去item的宽。得到实际每节的绘制宽（每节的长度应当减去 item节点的宽度。避免线盖住节点。视图重叠）
            int paddingWidth = getPaddingStart() + getPaddingEnd() + dp2px(getContext(), 15);
            //每个线节点的宽
            int itemWidthTwo = itemWidth * 2;
            int processWidth = (getMeasuredWidth() - paddingWidth - dp2px(getContext(), 10) - (itemWidth * size)) / size;
            processWidth = processWidth + (processWidth / 2) - itemWidth;


            int barLineWidth = 0;
            if (measureWidthCallBack != null) {
                barLineWidth = (processWidth *  (maxIndex)) + ((maxIndex ) * itemWidth) + paddingWidth;
                measureWidthCallBack.measureWidthCall(barLineWidth);
            }

            //第一条线
           // canvas.drawLine((itemWidth * 2) + widths, 0, barLineWidth, 0, mProcessOffPaint);
            for (int count = 0; count < section_title.size(); count++) {
                int startX = 0;
                //默认的下标绘制0 < 当前进度
                if (count == 0) {
                    if (count > maxIndex) {
                        //加行距
                        canvas.drawBitmap(spot_off, itemWidth + widths, thumbHeight + 2, mProcessOffPaint);
                        //第一条线
                        canvas.drawLine(itemWidthTwo + widths, thumbHeight, processWidth + itemWidthTwo + widths, thumbHeight + lineHeight, mProcessOffPaint);
                    } else {
                        //加行距
                        canvas.drawBitmap(cur_sections < count ? spot : spot_on, itemWidth + widths, thumbHeight + 2, cur_sections < count ? mPaint : mProcessPaint);
                        //第一条线
                        canvas.drawLine(itemWidthTwo + widths, thumbHeight + lineHeight, processWidth + itemWidthTwo + widths, thumbHeight + lineHeight, cur_sections > count ? mProcessPaint : mPaint);
                    }

                } else {
                    startX = (count) * (itemWidth + processWidth);
                    int lineLeftWidth = startX + processWidth;
                    if (count > maxIndex) {
                        //绘制节点
                        canvas.drawBitmap(spot_off, widths + startX + itemWidth, thumbHeight + 2, cur_sections < count ? mPaint : mProcessOffPaint);
                        if (count != section_title.size() - 1) {
                            canvas.drawLine(startX + widths + itemWidthTwo, thumbHeight + lineHeight, lineLeftWidth + widths + itemWidthTwo, thumbHeight + lineHeight, mProcessOffPaint);
                        }
                    } else {
                        //绘制节点
                        canvas.drawBitmap(cur_sections < count ? spot : spot_on, widths + startX + itemWidth, thumbHeight + 2, cur_sections < count ? mPaint : mProcessPaint);
                        if (count != section_title.size() - 1) {
                            if (count > maxIndex - 1) {
                                canvas.drawLine(startX + widths + itemWidthTwo, thumbHeight + lineHeight, lineLeftWidth + widths + itemWidthTwo, thumbHeight + lineHeight, mProcessOffPaint);
                            } else {
                                canvas.drawLine(startX + widths + itemWidthTwo, thumbHeight + lineHeight, lineLeftWidth + widths + itemWidthTwo, thumbHeight + lineHeight, cur_sections > count ? mProcessPaint : mPaint);
                            }
                        }
                    }

                }
                float startTextX = mTextPaint.measureText(section_title.get(count));
                if (count == 0) {
                    canvas.drawText(section_title.get(count), (itemWidth + widths), (thumbHeight * 2) + thumbHeight + 7, mTextPaint);
                } else if (count == maxIndex) {
                    canvas.drawText(section_title.get(count), (startX +widths+itemWidthTwo) - startTextX, (thumbHeight * 2) + thumbHeight + 7, mTextPaint);
                } else if (count == section_title.size() - 1) {
                    canvas.drawText(section_title.get(count), (startX +widths+itemWidthTwo) - startTextX, (thumbHeight * 2) + thumbHeight + 7, mTextPaint3);
                }
//                else if (count == cur_sections) {
//                    canvas.drawText(section_title.get(count), (startX + (itemWidthTwo)) - (startTextX/2), (thumbHeight * 2) + thumbHeight + 7, mTextPaint2);
//                }
            }
        }
    }

    //设置进度
    public void setProgress(int progress) {
        cur_sections = progress;
        invalidate();
    }

    public interface SlideOnTouchCallback {
        void onTouchIndex(int currentIndex);
    }

    public void setMeasureWidthCallBack(MeasureWidthCallBack callBack) {
        measureWidthCallBack = callBack;
    }

    public interface MeasureWidthCallBack {
        void measureWidthCall(int width);
    }

}
