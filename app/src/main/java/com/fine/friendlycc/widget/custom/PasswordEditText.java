package com.fine.friendlycc.widget.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.fine.friendlycc.R;

/**
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/08/25
 *    desc   : 密码隐藏显示 EditText
 */
public final class PasswordEditText extends AppCompatEditText
        implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcher {

    /** 非空格的字符（不能输入空格） */
    public static final String REGEX_NONNULL = "\\S+";

    private Drawable mCurrentDrawable;
    private final Drawable mVisibleDrawable;
    private final Drawable mInvisibleDrawable;

    private OnTouchListener mTouchListener;
    private OnFocusChangeListener mFocusChangeListener;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    @SuppressWarnings("all")
    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText, defStyleAttr, R.style.PasswordEditText);

        int imgWidth =Math.round(a.getDimension(R.styleable.PasswordEditText_imgWidth, 0));
        int imgHeight =Math.round(a.getDimension(R.styleable.PasswordEditText_imgHeight, 0));
        int imgResOnImg = a.getResourceId(R.styleable.PasswordEditText_imgResOnImg,0);
        int imgResOffImg = a.getResourceId(R.styleable.PasswordEditText_imgResOffImg,0);

        mVisibleDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, imgResOffImg));
        mVisibleDrawable.setBounds(0, 0, imgWidth, imgHeight);

        mInvisibleDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, imgResOnImg));
        mInvisibleDrawable.setBounds(0, 0, imgWidth, imgHeight);

        mCurrentDrawable = mVisibleDrawable;

        // 密码不可见
        addInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 密码输入规则
        setInputRegex(REGEX_NONNULL);

        setDrawableVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        super.addTextChangedListener(this);
    }

    /**
     * 添加一个输入标记
     */
    public void addInputType(int type) {
        setInputType(getInputType() | type);
    }
    /**
     * 设置输入正则
     */
    public void setInputRegex(String regex) {
        if (TextUtils.isEmpty(regex)) {
            return;
        }

    }


    private void setDrawableVisible(boolean visible) {
        if (mCurrentDrawable.isVisible() == visible) {
            return;
        }

        mCurrentDrawable.setVisible(visible, false);
        Drawable[] drawables = getCompoundDrawablesRelative();
        setCompoundDrawablesRelative(
                drawables[0],
                drawables[1],
                visible ? mCurrentDrawable : null,
                drawables[3]);
    }

    private void refreshDrawableStatus() {
        Drawable[] drawables = getCompoundDrawablesRelative();
        setCompoundDrawablesRelative(
                drawables[0],
                drawables[1],
                mCurrentDrawable,
                drawables[3]);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mTouchListener = onTouchListener;
    }

    /**
     * {@link OnFocusChangeListener}
     */

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus && getText() != null) {
            setDrawableVisible(getText().length() > 0);
        } else {
            setDrawableVisible(false);
        }
        if (mFocusChangeListener != null) {
            mFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    /**
     * {@link OnTouchListener}
     */

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int x = (int) event.getX();

        // 是否触摸了 Drawable
        boolean touchDrawable = false;
        // 获取布局方向
        int layoutDirection = getLayoutDirection();
        if (layoutDirection == LAYOUT_DIRECTION_LTR) {
            // 从左往右
            touchDrawable = x > getWidth() - mCurrentDrawable.getIntrinsicWidth() - getPaddingEnd() &&
                    x < getWidth() - getPaddingEnd();
        } else if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            // 从右往左
            touchDrawable = x > getPaddingStart() &&
                    x < getPaddingStart() + mCurrentDrawable.getIntrinsicWidth();
        }

        if (mCurrentDrawable.isVisible() && touchDrawable) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mCurrentDrawable == mVisibleDrawable) {
                    mCurrentDrawable = mInvisibleDrawable;
                    // 密码可见
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    refreshDrawableStatus();
                } else if (mCurrentDrawable == mInvisibleDrawable) {
                    mCurrentDrawable = mVisibleDrawable;
                    // 密码不可见
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                    refreshDrawableStatus();
                }
                Editable editable = getText();
                if (editable != null) {
                    setSelection(editable.toString().length());
                }
            }
            return true;
        }
        return mTouchListener != null && mTouchListener.onTouch(view, event);
    }

    /**
     * {@link TextWatcher}
     */

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setDrawableVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void afterTextChanged(Editable s) {}
}