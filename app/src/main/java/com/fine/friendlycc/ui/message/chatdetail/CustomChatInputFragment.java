package com.fine.friendlycc.ui.message.chatdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fine.friendlycc.R;
import com.tencent.qcloud.tuikit.tuichat.ui.view.input.BaseInputFragment;

/**
 *
 */
public class CustomChatInputFragment extends BaseInputFragment {

    public static boolean isAdmin = false;
    private TextView tvPicture;
    private TextView tvVideo;
    private TextView tvBurn;
    private TextView tvRedPackage;
    private TextView tvCoinRedPackage;
    private TextView tvMic;
    private LinearLayout bom_layout;
    private CustomChatInputFragmentListener customChatInputFragmentListener;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tv_action_picture) {
                customChatInputFragmentListener.onPictureActionClick();
            } else if (id == R.id.tv_action_video) {
                customChatInputFragmentListener.onVideoActionClick();
            }  else if (id == R.id.tv_action_burn) {
                customChatInputFragmentListener.onBurnActionClick();
            } else if (id == R.id.tv_action_redpackage) {
                customChatInputFragmentListener.onRedPackageActionClick();
            } else if (id == R.id.tv_action_coin_redpackage) {
                customChatInputFragmentListener.onCoinRedPackageActionClick();
            } else if (id == R.id.tv_action_mic) {
                customChatInputFragmentListener.onMicActionClick();
            }
        }
    };

    public CustomChatInputFragmentListener getCustomChatInputFragmentListener() {
        return customChatInputFragmentListener;
    }

    public void setCustomChatInputFragmentListener(CustomChatInputFragmentListener customChatInputFragmentListener) {
        this.customChatInputFragmentListener = customChatInputFragmentListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.fragment_custom_chat_input, container, false);
        tvPicture = baseView.findViewById(R.id.tv_action_picture);
        tvVideo = baseView.findViewById(R.id.tv_action_video);
        tvBurn = baseView.findViewById(R.id.tv_action_burn);
        tvRedPackage = baseView.findViewById(R.id.tv_action_redpackage);
        tvCoinRedPackage = baseView.findViewById(R.id.tv_action_coin_redpackage);
        tvMic = baseView.findViewById(R.id.tv_action_mic);

        bom_layout = baseView.findViewById(R.id.bom_layout);
//        if (isAdmin) {
//            bom_layout.setVisibility(View.GONE);
//        } else {
//            bom_layout.setVisibility(View.VISIBLE);
//        }

        tvPicture.setOnClickListener(onClickListener);
        tvVideo.setOnClickListener(onClickListener);
        tvBurn.setOnClickListener(onClickListener);
        tvRedPackage.setOnClickListener(onClickListener);
        tvCoinRedPackage.setOnClickListener(onClickListener);
        tvMic.setOnClickListener(onClickListener);

        return baseView;
    }

    public interface CustomChatInputFragmentListener {
        void onPictureActionClick();

        void onVideoActionClick();

        void onBurnActionClick();

        void onRedPackageActionClick();

        void onCoinRedPackageActionClick();

        void onMicActionClick();
    }
}
