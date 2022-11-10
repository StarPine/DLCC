package com.fine.friendlycc.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.fine.friendlycc.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 平台使用规范对话框
 *
 * @author wulei
 */
public class AgreementDialog extends BaseDialogFragment implements View.OnClickListener {

    int maxSeconds = 5;
    private TextView tvContent;
    private TextView tvAgreement;
    private Button btnAgree;
    private AgreementDialogListener agreementDialogListener;

    public AgreementDialogListener getArgeementDialogListener() {
        return agreementDialogListener;
    }

    public void setArgeementDialogListener(AgreementDialogListener agreementDialogListener) {
        this.agreementDialogListener = agreementDialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setLayout(mWidthAndHeight[0] - ConvertUtils.dp2px(88), ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_agreement;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvContent = view.findViewById(R.id.tv_content);
        tvAgreement = view.findViewById(R.id.tv_agreement);
        btnAgree = view.findViewById(R.id.btn_agree);

        btnAgree.setOnClickListener(this);

        tvAgreement.setHighlightColor(Color.parseColor("#00000000"));
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreement.setText(generateSp());

        startCountDown();
    }

    @SuppressLint("StringFormatMatches")
    public void startCountDown() {
        btnAgree.setEnabled(false);
        Flowable.intervalRange(0, maxSeconds + 1, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    btnAgree.setText(String.format(getString(R.string.playcc_agree_second), maxSeconds - aLong.intValue()));
                })
                .doOnComplete(() -> {
                    btnAgree.setText(getString(R.string.playcc_dialog_agree));
                    btnAgree.setEnabled(true);
                })
                .subscribe();
    }

    private SpannableString generateSp() {

        SpannableString spannableString = new SpannableString(getString(R.string.playcc_check_protocol_criterion));
        ClickableSpan agreementClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                if (agreementDialogListener != null) {
                    agreementDialogListener.onUserAgreementClick(AgreementDialog.this);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#49AAE1"));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        ClickableSpan regulationClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                if (agreementDialogListener != null) {
                    agreementDialogListener.onUsageSpecificationClick(AgreementDialog.this);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#49AAE1"));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        spannableString.setSpan(agreementClickableSpan, 5, 9, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(regulationClickableSpan, 12, 16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
//
//        String highlight1 = "@qmui";
//        String highlight2 = "#qmui#";
//        int start = 0, end;
//        int index;
//        while ((index = text.indexOf(highlight1, start)) > -1) {
//            end = index + highlight1.length();
//            sp.setSpan(new QMUITouchableSpan(tv,
//                    R.attr.app_skin_span_normal_text_color,
//                    R.attr.app_skin_span_pressed_text_color,
//                    R.attr.app_skin_span_normal_bg_color,
//                    R.attr.app_skin_span_pressed_bg_color) {
//                @Override
//                public void onSpanClick(View widget) {
//                    Toast.makeText(getContext(), "click @qmui", Toast.LENGTH_SHORT).show();
//                }
//            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            start = end;
//        }
//
//        start = 0;
//        while ((index = text.indexOf(highlight2, start)) > -1) {
//            end = index + highlight2.length();
//            sp.setSpan(new QMUITouchableSpan(tv,
//                    R.attr.app_skin_span_normal_text_color,
//                    R.attr.app_skin_span_pressed_text_color,
//                    R.attr.app_skin_span_normal_bg_color,
//                    R.attr.app_skin_span_pressed_bg_color) {
//                @Override
//                public void onSpanClick(View widget) {
//                    Toast.makeText(getContext(), "click #qmui#", Toast.LENGTH_SHORT).show();
//                }
//            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            start = end;
//        }
//        return sp;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_agree) {
            if (agreementDialogListener != null) {
                agreementDialogListener.onAcceptClick(this);
            } else {
                dismiss();
            }
        }
    }

    public interface AgreementDialogListener {

        void onAcceptClick(AgreementDialog dialog);

        void onUserAgreementClick(AgreementDialog dialog);

        void onUsageSpecificationClick(AgreementDialog dialog);
    }
}
