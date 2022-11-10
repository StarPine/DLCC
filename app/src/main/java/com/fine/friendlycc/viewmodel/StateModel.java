package com.fine.friendlycc.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.widget.emptyview.EmptyState;
import com.fine.friendlycc.R;
import com.fine.friendlycc.exception.EmptyException;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * 页面描述：状态页面设置模型
 * <p>
 * Created by ditclear on 2017/2/24.
 */

public class StateModel extends BaseObservable {

    private final Context mContext = AppContext.instance();

    public ObservableField<String> emptyText = new ObservableField<>(StringUtils.getString(R.string.playcc_empty_state_no_data));
    public ObservableField<String> retryBtnText = new ObservableField<>("Retry");

    public ObservableField<Boolean> isUpdata = new ObservableField<>(false);

    public ObservableField<Drawable> emptyImg = new ObservableField<>();
    public ObservableField<Integer> emptyTextColor = new ObservableField<>();
    //确定
    public BindingCommand emptyRetryOnClickCommand = null;
    @SuppressLint("WrongConstant")
    @EmptyState
    private int emptyState = EmptyState.INIT;

    public void setEmptyRetryCommand(String text, BindingCommand bindingCommand) {
        if (!TextUtils.isEmpty(text)) {
            this.retryBtnText.set(text);
        }
        emptyRetryOnClickCommand = bindingCommand;
    }

    public void setEmptyRetryCommand(String empty, String btnText, BindingCommand bindingCommand) {
        emptyText.set(empty);
        if (!TextUtils.isEmpty(btnText)) {
            this.retryBtnText.set(btnText);
        }
        emptyRetryOnClickCommand = bindingCommand;
    }

    public void setEmptyRetryCommand(String text, int img, int textColor) {
        if (!TextUtils.isEmpty(text)) {
            this.emptyText.set(text);
        }
        if (textColor != -1) {
            this.emptyTextColor.set(textColor);
        }
        if (img != -1) {
            this.emptyImg.set(ResourceUtils.getDrawable(img));
        }
        this.isUpdata.set(true);
    }

    public void setEmptyBroadcastCommand(String text, int img, int textColor,String btnText,BindingCommand bindingCommand) {
        if (!TextUtils.isEmpty(text)) {
            this.emptyText.set(text);
        }
        if (textColor != -1) {
            this.emptyTextColor.set(textColor);
        }
        if (img != -1) {
            this.emptyImg.set(ResourceUtils.getDrawable(img));
        }
        if (!TextUtils.isEmpty(btnText)) {
            this.retryBtnText.set(btnText);
        }
        emptyRetryOnClickCommand = bindingCommand;
        this.isUpdata.set(true);
    }

    @SuppressLint("WrongConstant")
    public int getEmptyState() {
        return emptyState;
    }

    /**
     * 设置状态
     *
     * @param emptyState
     */
    @SuppressLint("WrongConstant")
    public void setEmptyState(@EmptyState int emptyState) {
        this.emptyState = emptyState;
        notifyChange();
    }

    /**
     * 显示进度条
     *
     * @return
     */
    @SuppressLint("WrongConstant")
    public boolean isProgress() {
        return this.emptyState == EmptyState.PROGRESS;
    }

    @SuppressLint("WrongConstant")
    public boolean isInit() {
        return this.emptyState == EmptyState.INIT;
    }

    /**
     * 根据异常显示状态
     *
     * @param e
     */
    public void bindThrowable(Throwable e) {
        if (e instanceof EmptyException) {
            @EmptyState
            int code = ((EmptyException) e).getCode();

            setEmptyState(code);
        }
    }

    @SuppressLint("WrongConstant")
    public boolean isEmpty() {
        return this.emptyState != EmptyState.NORMAL;
    }

    /**
     * 空状态信息
     *
     * @return
     */
    @SuppressLint("WrongConstant")
    @Bindable
    public String getCurrentStateLabel() {
        switch (emptyState) {
            case EmptyState.EMPTY:
                return emptyText.get();
            case EmptyState.NET_ERROR:
                return StringUtils.getString(R.string.playcc_empty_state_check_network);
            case EmptyState.NOT_AVAILABLE:
                return StringUtils.getString(R.string.playcc_empty_state_server_error);
            default:
                return StringUtils.getString(R.string.playcc_empty_state_no_data);
        }
    }

    /**
     * 空状态图片
     *
     * @return
     */
    @Bindable
    public Drawable getEmptyIconRes() {
//        switch (emptyState) {
//            case EmptyState.EMPTY:
//                return ContextCompat.getDrawable(mContext, R.drawable.ic_empty);
//            case EmptyState.NET_ERROR:
//                return ContextCompat.getDrawable(mContext, R.drawable.ic_empty);
//            case EmptyState.NOT_AVAILABLE:
//                return ContextCompat.getDrawable(mContext, R.drawable.ic_empty);
//            default:
//                return ContextCompat.getDrawable(mContext, R.drawable.ic_empty);
//        }
        return ResourceUtils.getDrawable(R.drawable.ic_empty);
    }

}