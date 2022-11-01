package com.fine.friendlycc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.SizeUtils;
import com.fine.friendlycc.R;

/**
 * @ClassName BasicToolBarCustom
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/7/2 11:08
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class BasicToolBarCustom extends Toolbar implements View.OnClickListener {

    private final int titleColor;
    private final int backImage;
    private ToolbarListener toolbarListener;
    private ImageView ivBack;
    private TextView tvTitle;
    private String title;

    public BasicToolBarCustom(Context context) {
        this(context, null);
    }

    public BasicToolBarCustom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public BasicToolBarCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BasicToolbar);
        title = a.getString(R.styleable.BasicToolbar_toolbar_title);
        titleColor = a.getColor(R.styleable.BasicToolbar_toolbar_title_color, context.getResources().getColor(R.color.toolbar_title_color));
        backImage = a.getResourceId(R.styleable.BasicToolbar_boolbar_back_image, R.drawable.ic_toolbar_back);
    }

    @BindingAdapter({"toolbar_title"})
    public static void setTitleText(BasicToolbar textView, String text) {
        textView.setTitle(text);
    }

    public ToolbarListener getToolbarListener() {
        return toolbarListener;
    }

    public void setToolbarListener(ToolbarListener toolbarListener) {
        this.toolbarListener = toolbarListener;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || this.tvTitle == null) {
            return;
        }
        this.title = title;
        this.tvTitle.setText(title);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ivBack = new ImageView(getContext());
        ivBack.setImageResource(backImage);
        ivBack.setPadding(SizeUtils.dp2px(10), SizeUtils.dp2px(12), SizeUtils.dp2px(10), SizeUtils.dp2px(12));
        this.addView(ivBack);

        tvTitle = new TextView(getContext());
        tvTitle.setTextColor(titleColor);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.text_size_toolbar_title));
        tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvTitle.setMaxLines(1);
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setMaxWidth(SizeUtils.dp2px(200));
        this.addView(tvTitle);

        if (title != null) {
            tvTitle.setText(title);
        } else {
            tvTitle.setText("");
        }

        ivBack.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount > 2) {
            for (int i = 0; i < childCount; i++) {
                int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
                setMeasuredDimension(widthSpecSize, heightSpecSize);
                View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setChildrenDrawingOrderEnabled(true);
        int childCount = getChildCount();
        if (childCount > 2) {
            RelativeLayout relativeView = (RelativeLayout) getChildAt(0);
            relativeView.layout(0, 0, r, b);
        }

        View backView = getChildAt(childCount > 2 ? 1 : 0);
        int backViewWidth = backView.getMeasuredWidth();
        int backViewHeight = backView.getMeasuredHeight();

        int ll = SizeUtils.dp2px(5);
        int tt = (b - backViewHeight) / 2;
        int rr = SizeUtils.dp2px(5) + backViewWidth;
        int bb = tt + backViewHeight;
        backView.layout(ll, tt, rr, bb);

        View textView = getChildAt(childCount > 2 ? 2 : 1);
        int textViewWidth = textView.getMeasuredWidth();
        int textViewHeight = textView.getMeasuredHeight();

        int textl = r / 2 - textViewWidth / 2;
        int textt = b / 2 - textViewHeight / 2;
        int textr = textl + textViewWidth;
        int textb = textr + backViewHeight;
        textView.layout(textl, textt, textr, textb);

    }

    public void hiddenBack(boolean hidden) {
        if (hidden) {
            if (ivBack != null) {
                ivBack.setVisibility(View.INVISIBLE);
            }
        } else {
            if (ivBack != null) {
                ivBack.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (toolbarListener != null) {
            toolbarListener.onBackClick(this);
        }
    }

    public interface ToolbarListener {
        /**
         * 返回按钮点击
         *
         * @param toolbar
         */
        void onBackClick(BasicToolBarCustom toolbar);
    }
}