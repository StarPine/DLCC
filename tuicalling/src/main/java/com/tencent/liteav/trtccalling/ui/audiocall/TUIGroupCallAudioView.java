package com.tencent.liteav.trtccalling.ui.audiocall;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.liteav.trtccalling.R;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.model.TRTCCalling;
import com.tencent.liteav.trtccalling.model.impl.UserModel;
import com.tencent.liteav.trtccalling.model.impl.base.CallingInfoManager;
import com.tencent.liteav.trtccalling.model.impl.base.TRTCLogger;
import com.tencent.liteav.trtccalling.model.util.ImageLoader;
import com.tencent.liteav.trtccalling.ui.audiocall.audiolayout.TRTCGroupAudioLayout;
import com.tencent.liteav.trtccalling.ui.audiocall.audiolayout.TRTCGroupAudioLayoutManager;
import com.tencent.liteav.trtccalling.ui.base.BaseTUICallView;
import com.tencent.qcloud.tuicore.util.PermissionRequester;

import java.util.List;
import java.util.Map;

public class TUIGroupCallAudioView extends BaseTUICallView {
    private static final String TAG = "TUIGroupCallAudioView";

    private ImageView                   mImageMute;
    private ImageView                   mImageHangup;
    private LinearLayout                mLayoutMute;
    private LinearLayout                mLayoutHangup;
    private ImageView                   mImageHandsFree;
    private LinearLayout                mLayoutHandsFree;
    private ImageView                   mImageDialing;
    private LinearLayout                mLayoutDialing;
    private TRTCGroupAudioLayoutManager mLayoutManagerTRTC;
    private Group                       mGroupInviting;
    private LinearLayout                mLayoutImgContainer;
    private TextView                    mTextTime;
    private TextView                    mInvitedTag;
    private TextView                    mTvHangup;

    public TUIGroupCallAudioView(Context context, TUICalling.Role role, TUICalling.Type type, String[] userIDs,
                                 String sponsorID, String groupID, boolean isFromGroup) {
        super(context, role, type, userIDs, sponsorID, groupID, isFromGroup);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (TUICalling.Role.CALLED == mRole && !mTRTCCalling.isValidInvite()) {
            TRTCLogger.w(TAG, "this invitation is invalid");
            onCallingCancel();
        }
        initListener();
        if (mRole == TUICalling.Role.CALLED) {
            // ?????????
            PermissionRequester.permission(PermissionRequester.PermissionConstants.MICROPHONE).callback(new PermissionRequester.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    showWaitingResponseView();
                }

                @Override
                public void onDenied(List<String> permissionsDenied) {
                    mTRTCCalling.reject();
                    ToastUtils.showShort(R.string.trtccalling_tips_start_audio);
                    finish();
                }
            }).request();
        } else {
            // ?????????
            showInvitingView();
            PermissionRequester.permission(PermissionRequester.PermissionConstants.MICROPHONE).callback(new PermissionRequester.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    startInviting(TRTCCalling.TYPE_AUDIO_CALL);
                }

                @Override
                public void onDenied(List<String> permissionsDenied) {
                    ToastUtils.showShort(R.string.trtccalling_tips_start_audio);
                    finish();
                }
            }).request();
        }
    }

    @Override
    protected void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.trtccalling_group_audiocall_activity_call_main, this);
        mImageMute = (ImageView) findViewById(R.id.img_mute);
        mLayoutMute = (LinearLayout) findViewById(R.id.ll_mute);
        mImageHangup = (ImageView) findViewById(R.id.img_hangup);
        mLayoutHangup = (LinearLayout) findViewById(R.id.ll_hangup);
        mImageHandsFree = (ImageView) findViewById(R.id.img_handsfree);
        mLayoutHandsFree = (LinearLayout) findViewById(R.id.ll_handsfree);
        mImageDialing = (ImageView) findViewById(R.id.img_dialing);
        mLayoutDialing = (LinearLayout) findViewById(R.id.ll_dialing);
        mLayoutManagerTRTC = (TRTCGroupAudioLayoutManager) findViewById(R.id.trtc_layout_manager);
        mGroupInviting = (Group) findViewById(R.id.group_inviting);
        mLayoutImgContainer = (LinearLayout) findViewById(R.id.ll_img_container);
        mTextTime = (TextView) findViewById(R.id.tv_time);
        mInvitedTag = (TextView) findViewById(R.id.tv_inviting_tag);
        mTvHangup = (TextView) findViewById(R.id.tv_hangup);
        setImageBackView(findViewById(R.id.img_audio_back));
    }

    private void initListener() {
        mLayoutMute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsMuteMic = !mIsMuteMic;
                mTRTCCalling.setMicMute(mIsMuteMic);
                mImageMute.setActivated(mIsMuteMic);
                ToastUtils.showLong(mIsMuteMic ? R.string.trtccalling_toast_enable_mute : R.string.trtccalling_toast_disable_mute);
                TRTCGroupAudioLayout layout = mLayoutManagerTRTC.findAudioCallLayout(mSelfModel.userId);
                if (null != layout) {
                    layout.muteMic(mIsMuteMic);
                }
            }
        });
        mLayoutHandsFree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsHandsFree = !mIsHandsFree;
                mTRTCCalling.setHandsFree(mIsHandsFree);
                mImageHandsFree.setActivated(mIsHandsFree);
                ToastUtils.showLong(mIsHandsFree ? R.string.trtccalling_toast_use_speaker : R.string.trtccalling_toast_use_handset);
            }
        });
        mImageMute.setActivated(mIsMuteMic);
        mImageHandsFree.setActivated(mIsHandsFree);
        mTRTCCalling.setHandsFree(mIsHandsFree);
    }

    @Override
    public void onUserEnter(final String userId) {
        super.onUserEnter(userId);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showCallingView();
                UserModel userModel = mCallUserModelMap.get(userId);
                TRTCGroupAudioLayout layout = mLayoutManagerTRTC.findAudioCallLayout(userId);
                TRTCLogger.d(TAG, "onUserEnter, userId=" + userId + " ,layout=" + layout);
                if (layout == null) {
                    layout = addUserToManager(userModel);
                }
                layout.stopLoading();
                loadUserInfo(userModel, layout);
                //C2C????????????:??????????????????????????????,????????????????????????
                if (null != userModel && mOtherInviteeList.contains(userModel)) {
                    mOtherInviteeList.remove(userModel);
                }
            }
        });
    }

    @Override
    public void onUserLeave(final String userId) {
        super.onUserLeave(userId);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //1. ??????????????????
                mLayoutManagerTRTC.recyclerAudioCallLayout(userId);

                //C2C????????????:?????????:???????????????"??????"?????????,????????????????????????UI??????
                if (null != mSponsorUserInfo && userId.equals(mSponsorUserInfo.userId)) {
                    for (UserModel model : mOtherInviteeList) {
                        if (null != model && !TextUtils.isEmpty(model.userId)) {
                            //????????????????????????????????????
                            mLayoutManagerTRTC.recyclerAudioCallLayout(model.userId);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onReject(final String userId) {
        super.onReject(userId);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.d(TAG, "onReject: userId = " + userId + ",mCallUserModelMap " + mCallUserModelMap);
                //??????????????????,??????????????????
                mLayoutManagerTRTC.recyclerAudioCallLayout(userId);
                UserModel model = getRemovedUserModel();
                //C2C????????????:??????????????????????????????,????????????????????????
                if (null != model && mOtherInviteeList.contains(model)) {
                    mOtherInviteeList.remove(model);
                    showOtherInvitingUserView();
                }
            }
        });
    }

    @Override
    public void onNoResp(final String userId) {
        super.onNoResp(userId);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.d(TAG, "onNoResp: userId = " + userId + ",mCallUserModelMap: " + mCallUserModelMap);
                //???????????????,??????????????????
                mLayoutManagerTRTC.recyclerAudioCallLayout(userId);
                UserModel model = getRemovedUserModel();
                //C2C????????????:??????????????????????????????,????????????????????????
                if (null != model && mOtherInviteeList.contains(model)) {
                    mOtherInviteeList.remove(model);
                    showOtherInvitingUserView();
                }
            }
        });
    }

    @Override
    public void onLineBusy(String userId) {
        super.onLineBusy(userId);
        TRTCLogger.d(TAG, "onLineBusy: userId = " + userId + ",mCallUserModelMap " + mCallUserModelMap);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //????????????,??????????????????
                mLayoutManagerTRTC.recyclerAudioCallLayout(userId);
                UserModel userInfo = getRemovedUserModel();
                //C2C????????????:????????????????????????,????????????????????????
                if (null != userInfo && mOtherInviteeList.contains(userInfo)) {
                    mOtherInviteeList.remove(userInfo);
                    showOtherInvitingUserView();
                }
            }
        });

    }

    @Override
    public void onUserAudioAvailable(String userId, boolean isAudioAvailable) {
        TRTCLogger.d(TAG, "onUserAudioAvailable, userId=" + userId + ", isAudioAvailable=" + isAudioAvailable);
        TRTCGroupAudioLayout layout = mLayoutManagerTRTC.findAudioCallLayout(userId);
        if (null != layout) {
            layout.muteMic(!isAudioAvailable);
        }
    }

    @Override
    public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
        for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
            String userId = entry.getKey();
            TRTCGroupAudioLayout layout = mLayoutManagerTRTC.findAudioCallLayout(userId);
            if (layout != null) {
                layout.setAudioVolume(entry.getValue());
            }
        }
    }

    @Override
    public void onTryToReconnect() {

    }

    /**
     * ??????????????????
     */
    public void showWaitingResponseView() {
        super.showWaitingResponseView();
        //1. ?????????????????????
        if (null == mSponsorUserInfo) {
            for (UserModel userModel : mOtherInviteeList) {
                loadUserInfo(userModel, addUserToManager(userModel));
            }
        } else {
            loadUserInfo(mSponsorUserInfo, addUserToManager(mSponsorUserInfo));
        }

        //2. ????????????????????????
        mLayoutHangup.setVisibility(View.VISIBLE);
        mLayoutDialing.setVisibility(View.VISIBLE);
        mLayoutHandsFree.setVisibility(View.GONE);
        mLayoutMute.setVisibility(View.GONE);
        //3. ???????????????listener
        mLayoutHangup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTRTCCalling.reject();
                finish();
            }
        });
        mLayoutDialing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //2.????????????
                mTRTCCalling.accept();
                mTRTCCalling.setHandsFree(mIsHandsFree);
                showCallingView();
            }
        });
        //4. ????????????????????????
        if (null != mSponsorUserInfo) {
            showOtherInvitingUserView();
        }
        mInvitedTag.setText(mContext.getString(R.string.trtccalling_invite_audio_call));
        mTvHangup.setText(R.string.trtccalling_text_reject);
    }

    /**
     * ??????????????????
     */
    public void showInvitingView() {
        super.showInvitingView();
        //1. ?????????????????????
        mLayoutManagerTRTC.setMySelfUserId(mSelfModel.userId);
        loadUserInfo(mSelfModel, addUserToManager(mSelfModel));
        //2. ?????????????????????
        for (UserModel userInfo : mCallUserInfoList) {
            TRTCGroupAudioLayout layout = addUserToManager(userInfo);
            layout.startLoading();
            loadUserInfo(userInfo, layout);
        }
        //3. ???????????????
        mLayoutHangup.setVisibility(View.VISIBLE);
        mLayoutHangup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTRTCCalling.hangup();
                finish();
            }
        });
        mLayoutDialing.setVisibility(View.GONE);
        mLayoutHandsFree.setVisibility(View.VISIBLE);
        mLayoutMute.setVisibility(View.VISIBLE);
        //4. ??????????????????????????????
//        hideOtherInvitingUserView();
        mGroupInviting.setVisibility(View.VISIBLE);
        mInvitedTag.setText(mContext.getString(R.string.trtccalling_waiting_be_accepted));
        mTvHangup.setText(R.string.trtccalling_text_hangup);
    }

    /**
     * ????????????????????????
     */
    public void showCallingView() {
        super.showCallingView();
        //1. ?????????????????????
        mLayoutManagerTRTC.setMySelfUserId(mSelfModel.userId);
        TRTCGroupAudioLayout audioLayout = mLayoutManagerTRTC.findAudioCallLayout(mSelfModel.userId);
        if (null == audioLayout) {
            audioLayout = addUserToManager(mSelfModel);
            loadUserInfo(mSelfModel, audioLayout);
        }
        //2. ???????????????
        mLayoutHangup.setVisibility(View.VISIBLE);
        mLayoutDialing.setVisibility(View.GONE);
        mLayoutHandsFree.setVisibility(View.VISIBLE);
        mLayoutMute.setVisibility(View.VISIBLE);
        mTvHangup.setText(R.string.trtccalling_text_hangup);
        mLayoutHangup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTRTCCalling.hangup();
                finish();
            }
        });
        showTimeCount(mTextTime);
        //3. ????????????????????????????????????
        hideOtherInvitingUserView();
        //mSponsorUserInfo?????????,???????????????;
        //C2C??????????????????:???????????????,????????????????????????,?????????????????????loading
        if (null != mSponsorUserInfo) {
            //???????????????
            mIsInRoom = true;
            TRTCLogger.d(TAG, "showCallingView: mCallUserModelMap = " + mCallUserModelMap);
            for (Map.Entry<String, UserModel> entry : mCallUserModelMap.entrySet()) {
                UserModel model = mCallUserModelMap.get(entry.getKey());
                if (null != model && !TextUtils.isEmpty(model.userId)) {
                    TRTCGroupAudioLayout layout = mLayoutManagerTRTC.findAudioCallLayout(model.userId);
                    TRTCLogger.d(TAG, "showCallingView model=" + model.userId + " ,layout=" + layout);
                    if (layout == null) {
                        layout = addUserToManager(model);
                        layout.startLoading();
                    }
                    loadUserInfo(model, layout);
                }
            }
        }
    }

    //???????????????????????????????????????
    private void showOtherInvitingUserView() {
        if (CollectionUtils.isEmpty(mOtherInviteeList)) {
            mLayoutImgContainer.removeAllViews();
            mGroupInviting.setVisibility(mIsInRoom ? GONE : VISIBLE);
            return;
        }
        mGroupInviting.setVisibility(View.VISIBLE);
        mLayoutImgContainer.removeAllViews();
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.trtccalling_small_image_size);
        int leftMargin = getResources().getDimensionPixelOffset(R.dimen.trtccalling_small_image_left_margin);
        for (int index = 0; index < mOtherInviteeList.size(); index++) {
            final UserModel userModel = mOtherInviteeList.get(index);
            final ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            ImageLoader.loadImage(mContext, imageView, userModel.userAvatar, R.drawable.trtccalling_ic_avatar);
            mLayoutImgContainer.addView(imageView);

            CallingInfoManager.getInstance().getUserInfoByUserId(userModel.userId, new CallingInfoManager.UserCallback() {
                @Override
                public void onSuccess(UserModel model) {
                    userModel.userName = model.userName;
                    userModel.userAvatar = model.userAvatar;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageLoader.loadImage(mContext, imageView, userModel.userAvatar, R.drawable.trtccalling_ic_avatar);
                        }
                    });
                }

                @Override
                public void onFailed(int code, String msg) {
                    ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_search_fail, msg));
                }
            });
        }
    }

    private void hideOtherInvitingUserView() {
        mGroupInviting.setVisibility(View.GONE);
    }

    private TRTCGroupAudioLayout addUserToManager(UserModel userInfo) {
        TRTCGroupAudioLayout layout = mLayoutManagerTRTC.allocAudioCallLayout(userInfo.userId);
        if (layout == null) {
            return null;
        }
        TRTCLogger.d(TAG, String.format("addUserToManager, userId=%s, userName=%s, userAvatar=%s", userInfo.userId,
                userInfo.userName, userInfo.userAvatar));
        loadUserInfo(userInfo, layout);
        return layout;
    }

    //???IM???????????????????????????????????????
    private void loadUserInfo(final UserModel userModel, TRTCGroupAudioLayout layout) {
        if (null == userModel || null == layout) {
            TRTCLogger.e(TAG, "loadUserInfo error: null == userModel || null == layout");
            return;
        }
        CallingInfoManager.getInstance().getUserInfoByUserId(userModel.userId, new CallingInfoManager.UserCallback() {
            @Override
            public void onSuccess(UserModel model) {
                userModel.userName = model.userName;
                userModel.userAvatar = model.userAvatar;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.setUserName(userModel.userName);
                        ImageLoader.loadImage(mContext, layout.getImageView(), userModel.userAvatar, R.drawable.trtccalling_ic_avatar);
                    }
                });
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_search_fail, msg));
            }
        });
    }
}
