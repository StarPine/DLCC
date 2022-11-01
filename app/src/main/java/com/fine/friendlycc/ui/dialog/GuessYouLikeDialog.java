package com.fine.friendlycc.ui.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fine.friendlycc.app.GlideEngine;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.entity.LikeRecommendEntity;
import com.fine.friendlycc.entity.RecommendUserEntity;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.R;

/**
 * 猜你喜欢对话框
 *
 * @author wulei
 */
public class GuessYouLikeDialog extends BaseDialogFragment implements View.OnClickListener {

    private ImageView ivAvatar1;
    private ImageView ivAvatar2;
    private ImageView ivAvatar3;
    private ImageView ivAvatar4;
    private TextView tvName1;
    private TextView tvName2;
    private TextView tvName3;
    private TextView tvName4;
    private TextView tvAddress1;
    private TextView tvAddress2;
    private TextView tvAddress3;
    private TextView tvAddress4;
    private Button btnCall;
    private TextView tvClose;

    private LikeRecommendEntity likeRecommendEntity;

    private GuessYouLikeDialogListener guessYouLikeDialogListener;

    public static GuessYouLikeDialog newInstance(LikeRecommendEntity likeRecommendEntity) {
        GuessYouLikeDialog dialog = new GuessYouLikeDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("like_recommend_entity", likeRecommendEntity);
        dialog.setArguments(bundle);
        return dialog;
    }

    public GuessYouLikeDialogListener getGuessYouLikeDialogListener() {
        return guessYouLikeDialogListener;
    }

    public void setGuessYouLikeDialogListener(GuessYouLikeDialogListener guessYouLikeDialogListener) {
        this.guessYouLikeDialogListener = guessYouLikeDialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        likeRecommendEntity = (LikeRecommendEntity) getArguments().getSerializable("like_recommend_entity");
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
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
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_guess_you_like;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAvatar1 = view.findViewById(R.id.iv_avatar1);
        ivAvatar2 = view.findViewById(R.id.iv_avatar2);
        ivAvatar3 = view.findViewById(R.id.iv_avatar3);
        ivAvatar4 = view.findViewById(R.id.iv_avatar4);
        tvName1 = view.findViewById(R.id.tv_name1);
        tvName2 = view.findViewById(R.id.tv_name2);
        tvName3 = view.findViewById(R.id.tv_name3);
        tvName4 = view.findViewById(R.id.tv_name4);
        tvAddress1 = view.findViewById(R.id.tv_addres1);
        tvAddress2 = view.findViewById(R.id.tv_addres2);
        tvAddress3 = view.findViewById(R.id.tv_addres3);
        tvAddress4 = view.findViewById(R.id.tv_addres4);
        btnCall = view.findViewById(R.id.btn_call);
        tvClose = view.findViewById(R.id.tv_close);

        btnCall.setOnClickListener(this);
        tvClose.setOnClickListener(this);

        try {
            RecommendUserEntity user1 = likeRecommendEntity.getUser().get(0);
            GlideEngine.createGlideEngine().loadImage(getContext(), StringUtil.getFullThumbImageUrl(user1.getAvatar()), ivAvatar1);
            tvName1.setText(user1.getNickname());
            tvAddress1.setText(String.format(getString(R.string.playfun_age_and_city), user1.getAge(), user1.getCityName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            RecommendUserEntity user2 = likeRecommendEntity.getUser().get(1);
            GlideEngine.createGlideEngine().loadImage(getContext(), StringUtil.getFullThumbImageUrl(user2.getAvatar()), ivAvatar2);
            tvName2.setText(user2.getNickname());
            tvAddress2.setText(String.format(getString(R.string.playfun_age_and_city), user2.getAge(), user2.getCityName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            RecommendUserEntity user3 = likeRecommendEntity.getUser().get(2);
            GlideEngine.createGlideEngine().loadImage(getContext(), StringUtil.getFullThumbImageUrl(user3.getAvatar()), ivAvatar3);
            tvName3.setText(user3.getNickname());
            tvAddress3.setText(String.format(getString(R.string.playfun_age_and_city), user3.getAge(), user3.getCityName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            RecommendUserEntity user4 = likeRecommendEntity.getUser().get(3);
            GlideEngine.createGlideEngine().loadImage(getContext(), StringUtil.getFullThumbImageUrl(user4.getAvatar()), ivAvatar4);
            tvName4.setText(user4.getNickname());
            tvAddress4.setText(String.format(getString(R.string.playfun_age_and_city), user4.getAge(), user4.getCityName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (likeRecommendEntity.getPrice() > 0) {
            if (Injection.provideDemoRepository().readUserData().getSex() == 1) {
                btnCall.setText(String.format(getString(R.string.playfun_call_female_im), likeRecommendEntity.getPrice()));
            } else {
                btnCall.setText(String.format(getString(R.string.playfun_call_male_im), likeRecommendEntity.getPrice()));
            }
            tvClose.setVisibility(View.VISIBLE);
        } else {
            tvClose.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_close) {
            if (guessYouLikeDialogListener != null) {
                guessYouLikeDialogListener.onCloseClick(this);
            } else {
                dismiss();
            }
        } else if (view.getId() == R.id.btn_call) {
            if (guessYouLikeDialogListener != null) {
                guessYouLikeDialogListener.onCallClick(this, likeRecommendEntity);
            } else {
                dismiss();
            }
        }
    }

    public interface GuessYouLikeDialogListener {

        void onCallClick(GuessYouLikeDialog dialog, LikeRecommendEntity entity);

        void onCloseClick(GuessYouLikeDialog dialog);

    }
}
