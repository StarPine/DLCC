package com.dl.playfun.kl.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dl.playfun.entity.CallingVideoTryToReconnectEvent;
import com.dl.playfun.event.CallVideoUserEnterEvent;
import com.dl.playfun.utils.LogUtils;
import com.dl.playfun.widget.dialog.TraceDialog;
import com.dl.playfun.R;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.model.TRTCCalling;
import com.tencent.liteav.trtccalling.model.impl.UserModel;
import com.tencent.liteav.trtccalling.model.impl.base.CallingInfoManager;
import com.tencent.liteav.trtccalling.model.util.AvatarConstant;
import com.tencent.liteav.trtccalling.model.util.EventHandler;
import com.tencent.liteav.trtccalling.model.util.ImageLoader;
import com.tencent.liteav.trtccalling.ui.base.BaseTUICallView;
import com.tencent.liteav.trtccalling.ui.base.VideoLayoutFactory;
import com.tencent.liteav.trtccalling.ui.videocall.videolayout.TRTCVideoLayout;
import com.tencent.liteav.trtccalling.ui.videocall.videolayout.TRTCVideoLayoutManager;
import com.tencent.trtc.TRTCCloudDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.goldze.mvvmhabit.bus.RxBus;

public class JMTUICallVideoView extends BaseTUICallView {

    private static final int MAX_SHOW_INVITING_USER = 4;
    private static final String TAG = "TUICallVideoView";
    /**
     * 拨号相关成员变量
     */
    private final List<UserModel> mCallUserInfoList = new ArrayList<>(); // 呼叫方
    private final Map<String, UserModel> mCallUserModelMap = new HashMap<>();
    private final boolean mIsMuteMic = false;
    private final boolean mIsCameraOpen = true;
    private final boolean mIsAudioMode = false;
    private final boolean mIsCalledClick = false;  // 被叫方点击转换语音
    private TRTCVideoLayoutManager mLayoutManagerTrtc;
    private Group mInvitingGroup;
    private LinearLayout mImgContainerLl;
    private TextView mTimeTv;
    private View mShadeSponsor;
    private Runnable mTimeRunnable;
    private int mTimeCount;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;
    private UserModel mSponsorUserInfo;                      // 被叫方
    private List<UserModel> mOtherInvitingUserInfoList;
    private boolean mIsHandsFree = true;
    private boolean mIsFrontCamera = true;
    private boolean isStartRemoteView = false;
    private boolean isChatting = false;  // 是否已经接通
    private int roomId = 0;
    //断网总时间
    int disconnectTime = 0;

    public JMTUICallVideoView(Context context, TUICalling.Role role, String[] userIDs, String sponsorID, String groupID, boolean isFromGroup,VideoLayoutFactory videoLayoutFactory) {
        super(context, role, TUICalling.Type.VIDEO, userIDs, sponsorID, groupID, isFromGroup);
        mLayoutManagerTrtc.initVideoFactory(videoLayoutFactory);
    }

    public JMTUICallVideoView(Context context, TUICalling.Role role, String[] userIDs, String sponsorID, String groupID, boolean isFromGroup, int roomId,VideoLayoutFactory videoLayoutFactory) {

        this(context, role, userIDs, sponsorID, groupID, isFromGroup,videoLayoutFactory);
        this.roomId = roomId;

    }

    @Override
    protected void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.jm_trtccalling_videocall_activity_call_main, this);
        mLayoutManagerTrtc = findViewById(R.id.trtc_layout_manager);
        mInvitingGroup = findViewById(R.id.group_inviting);
        mImgContainerLl = findViewById(R.id.ll_img_container);
        mTimeTv = findViewById(R.id.tv_time);
        mShadeSponsor = findViewById(R.id.shade_sponsor);
//        mSwitchCameraImg = findViewById(R.id.switch_camera);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initData();
        initListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimeCount();
        mTimeHandlerThread.quit();
    }

    private void initData() {
        // 初始化成员变量
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());
        try {
            PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.MICROPHONE).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    initViewData();
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                    if(isDestroyed()){
                        return;
                    }
                    TraceDialog.getInstance(mContext)
                            .setCannelOnclick(dialog -> {
                                mTRTCCalling.reject();
                                ToastUtils.showShort(R.string.trtccalling_tips_start_camera_audio);
                                finish();
                            })
                            .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                                @Override
                                public void confirm(Dialog dialog) {
                                    PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.MICROPHONE).callback(new PermissionUtils.FullCallback() {
                                        @Override
                                        public void onGranted(List<String> permissionsGranted) {
                                            initViewData();
                                        }

                                        @Override
                                        public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                                            mTRTCCalling.reject();
                                            ToastUtils.showShort(R.string.trtccalling_tips_start_camera_audio);
                                            finish();
                                        }
                                    }).request();
                                }
                            }).AlertCallAudioPermissions().show();
                }
            }).request();
        } catch (Exception e) {

        }

    }

    private void initViewData() {
        if (mRole == TUICalling.Role.CALLED) {
            // 作为被叫
            if (!TextUtils.isEmpty(mSponsorID)) {
                mSponsorUserInfo = new UserModel();
                mSponsorUserInfo.userId = mSponsorID;
                mSponsorUserInfo.userAvatar = AvatarConstant.USER_AVATAR_ARRAY[new Random().nextInt(AvatarConstant.USER_AVATAR_ARRAY.length)];
            }
            showWaitingResponseView();
        } else {
            // 主叫方
            if (mUserIDs != null) {
                for (String userId : mUserIDs) {
                    UserModel userModel = new UserModel();
                    userModel.userId = userId;
                    mCallUserInfoList.add(userModel);
                    mCallUserModelMap.put(userModel.userId, userModel);
                }
                showInvitingView();
                startInviting();
            }
        }
    }

    private void initListener() {

//        mSwitchCameraImg.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mIsCameraOpen) {
//                    ToastUtils.showShort(R.string.trtccalling_switch_camera_hint);
//                    return;
//                }
//                mIsFrontCamera = !mIsFrontCamera;
//                mTRTCCalling.switchCamera(mIsFrontCamera);
//                mSwitchCameraImg.setActivated(mIsFrontCamera);
//                ToastUtils.showLong(R.string.trtccalling_toast_switch_camera);
//            }
//        });
    }

    private void startInviting() {
        final List<String> list = new ArrayList<>();
        for (UserModel userInfo : mCallUserInfoList) {
            list.add(userInfo.userId);
        }
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.MICROPHONE).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudView(mSelfModel.userId);
                if (null != layout) {
                    mTRTCCalling.openCamera(true, layout.getVideoView());
                }
                mTRTCCalling.groupCall(roomId, list, TRTCCalling.TYPE_VIDEO_CALL, "");
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                ToastUtils.showShort(R.string.trtccalling_tips_start_camera_audio);
                finish();
            }
        }).request();
    }

    @Override
    public void onError(int code, String msg) {
        //发生了错误，报错并退出该页面
        ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_call_error_msg, code, msg));
        stopCameraAndFinish();
    }

    @Override
    public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {
    }

    @Override
    public void onGroupCallInviteeListUpdate(List<String> userIdList) {
    }

    @Override
    public void onUserEnter(final String userId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //用户进入房间
                showCallingView();
                //发送订阅事件通知有人加入了视频聊天房
                RxBus.getDefault().post(new CallVideoUserEnterEvent(userId));
                UserModel userModel = new UserModel();
                userModel.userId = userId;
                mCallUserModelMap.put(userId, userModel);
                TRTCVideoLayout videoLayout = showVideoView(userModel);
                if (!isStartRemoteView){//没有拉流时，重新拉流
                    onUserVideoAvailable(userId,true);
                }
                loadUserInfo(userModel, videoLayout);
            }
        });
    }

    //查询昵称和头像
    private void loadUserInfo(final UserModel userModel, TRTCVideoLayout layout) {
        if (null == userModel || null == layout) {
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
                        if (isDestroyed()) {
                            return;
                        }
                        layout.setUserName(userModel.userName);
                        ImageLoader.loadImage(mContext, layout.getHeadImg(), userModel.userAvatar,
                                com.tencent.liteav.trtccalling.R.drawable.trtccalling_ic_avatar);
                    }
                });
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtils.showLong(mContext.getString(com.tencent.liteav.trtccalling.R.string.trtccalling_toast_search_fail, msg));
            }
        });
    }

    @Override
    public void onUserLeave(final String userId) {
        Log.i("JM_trtc", "onUserLeave: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //1. 回收界面元素
                mLayoutManagerTrtc.recyclerCloudViewView(userId);
                //2. 删除用户model
                UserModel userInfo = mCallUserModelMap.remove(userId);
                if (userInfo != null) {
                    mCallUserInfoList.remove(userInfo);
                }
            }
        });
    }

    @Override
    public void onReject(final String userId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCallUserModelMap.containsKey(userId)) {
                    // 进入拒绝环节
                    //1. 回收界面元素
                    mLayoutManagerTrtc.recyclerCloudViewView(userId);
                    //2. 删除用户model
                    UserModel userInfo = mCallUserModelMap.remove(userId);
                    if (userInfo != null) {
                        mCallUserInfoList.remove(userInfo);
                        ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_user_reject_call, userInfo.userName));
                    }
                }
            }
        });
    }

    @Override
    public void onNoResp(final String userId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCallUserModelMap.containsKey(userId)) {
                    // 进入无响应环节
                    //1. 回收界面元素
                    mLayoutManagerTrtc.recyclerCloudViewView(userId);
                    //2. 删除用户model
                    UserModel userInfo = mCallUserModelMap.remove(userId);
                    if (userInfo != null) {
                        mCallUserInfoList.remove(userInfo);
                        ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_user_not_response, userInfo.userName));
                    }
                }
            }
        });
    }

    @Override
    public void onLineBusy(String userId) {
        if (mCallUserModelMap.containsKey(userId)) {
            // 进入无响应环节
            //1. 回收界面元素
            mLayoutManagerTrtc.recyclerCloudViewView(userId);
            //2. 删除用户model
            UserModel userInfo = mCallUserModelMap.remove(userId);
            if (userInfo != null) {
                mCallUserInfoList.remove(userInfo);
                ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_user_busy, userInfo.userName));
            }
        }
    }

    @Override
    public void onCallingCancel() {
        if (mSponsorUserInfo != null) {
            ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_user_cancel_call, mSponsorUserInfo.userName));
        }
        stopCameraAndFinish();
    }

    @Override
    public void onCallingTimeout() {
        if (mSponsorUserInfo != null) {
            ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_user_timeout, mSponsorUserInfo.userName));
        }
        stopCameraAndFinish();
    }

    @Override
    public void onCallEnd() {
        Log.i("JM_trtc", "onCallEnd: ");
        if (mSponsorUserInfo != null) {
            ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_user_end, mSponsorUserInfo.userName));
        }
        stopCameraAndFinish();
    }

    @Override
    public void onUserVideoAvailable(final String userId, final boolean isVideoAvailable) {
        //有用户的视频开启了
        TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudView(userId);
        if (layout != null) {
            layout.setVideoAvailable(isVideoAvailable);
            if (isVideoAvailable) {
                isStartRemoteView = true;
                mTRTCCalling.startRemoteView(userId, layout.getVideoView());
            } else {
                isStartRemoteView = false;
                mTRTCCalling.stopRemoteView(userId);
            }
        } else {

        }
    }

    @Override
    public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {

    }

    @Override
    public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
        for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
            String userId = entry.getKey();
            TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudView(userId);
            if (layout != null) {
                layout.setAudioVolumeProgress(entry.getValue());
            }
        }
    }

    @Override
    public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
        //两秒回调一次
        if (localQuality.quality == 6 || remoteQuality.isEmpty()) {
            disconnectTime++;
            if (disconnectTime > 30 || (remoteQuality.isEmpty() && disconnectTime >15)) {
                hangup();
            }
        }else {
            disconnectTime  = 0;
        }
        updateNetworkQuality(localQuality, remoteQuality);
    }

    @Override
    public void onTryToReconnect() {
        LogUtils.i("onTryToReconnect: vidoe");
        RxBus.getDefault().post(new CallingVideoTryToReconnectEvent());
    }

    @Override
    public void onSwitchToAudio(boolean success, String message) {
//

    }

    private void enableHandsFree(boolean enable) {
        mIsHandsFree = enable;
        mTRTCCalling.setHandsFree(mIsHandsFree);
    }

    /**
     * 被叫方
     * 等待接听界面
     */
    public void showWaitingResponseView() {
        //1. 展示自己的画面
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        final TRTCVideoLayout videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);
        mTRTCCalling.openCamera(true, videoLayout.getVideoView());
        //2. 展示对方的头像和蒙层
        visibleSponsorGroup(true);
        CallingInfoManager.getInstance().getUserInfoByUserId(mSponsorUserInfo.userId, new CallingInfoManager.UserCallback() {
            @Override
            public void onSuccess(UserModel model) {
                mSponsorUserInfo.userName = model.userName;
                mSponsorUserInfo.userAvatar = model.userAvatar;
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_search_fail, msg));
            }
        });
        //3. 展示电话对应界面
        //3. 设置对应的listener

        //4. 展示其他用户界面
        showOtherInvitingUserView();
    }

    private void visibleSponsorGroup(boolean visible) {
        if (visible) {
            mShadeSponsor.setVisibility(View.VISIBLE);
        } else {
            mShadeSponsor.setVisibility(View.GONE);
        }
    }

    /**
     * 主叫方调用
     * 展示邀请列表
     */
    public void showInvitingView() {
        //1. 展示自己的界面
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        final TRTCVideoLayout videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);

        //2. 设置底部栏
//        mSwitchCameraImg.setVisibility(View.GONE);
        //3. 隐藏中间他们也在界面
        hideOtherInvitingUserView();
        //4. sponsor画面也隐藏
        visibleSponsorGroup(true);
        mSponsorUserInfo = mCallUserInfoList.get(0);
        CallingInfoManager.getInstance().getUserInfoByUserId(mSponsorUserInfo.userId, new CallingInfoManager.UserCallback() {
            @Override
            public void onSuccess(UserModel model) {
                mSponsorUserInfo.userName = model.userName;
                mSponsorUserInfo.userAvatar = model.userAvatar;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isDestroyed()) {
                            return;
                        }
                    }
                });
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtils.showLong(mContext.getString(R.string.trtccalling_toast_search_fail, msg));
            }
        });
    }

    /**
     * 展示通话中的界面
     */
    public void showCallingView() {
        super.showCallingView();
        //1. 蒙版消失
        visibleSponsorGroup(false);
        //2. 底部状态栏
//        mSwitchCameraImg.setVisibility(mIsAudioMode ? View.GONE : View.VISIBLE);
        showTimeCount();
        hideOtherInvitingUserView();
    }

    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        mTimeCount = 0;
        mTimeTv.setText(getShowTime(mTimeCount));
        if (mTimeRunnable == null) {
            mTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    mTimeCount++;
                    if (mTimeTv != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isDestroyed()) {
                                    mTimeTv.setText(getShowTime(mTimeCount));
                                }
                            }
                        });
                    }
                    mTimeHandler.postDelayed(mTimeRunnable, 1000);
                }
            };
        }
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    private void stopTimeCount() {
        mTimeHandler.removeCallbacks(mTimeRunnable);
        mTimeRunnable = null;
    }

//    private String getShowTime(int count) {
//        return mContext.getString(R.string.trtccalling_called_time_format, count / 60, count % 60);
//    }

    private void showOtherInvitingUserView() {
        if (mOtherInvitingUserInfoList == null || mOtherInvitingUserInfoList.size() == 0) {
            return;
        }
        mInvitingGroup.setVisibility(View.VISIBLE);
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.trtccalling_small_image_size);
        int leftMargin = getResources().getDimensionPixelOffset(R.dimen.trtccalling_small_image_left_margin);
        for (int index = 0; index < mOtherInvitingUserInfoList.size() && index < MAX_SHOW_INVITING_USER; index++) {
            UserModel userInfo = mOtherInvitingUserInfoList.get(index);
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            ImageLoader.loadImage(mContext, imageView, userInfo.userAvatar, R.drawable.trtccalling_ic_avatar);
            mImgContainerLl.addView(imageView);
        }
    }

    private void hideOtherInvitingUserView() {
        mInvitingGroup.setVisibility(View.GONE);
    }

    private int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // 自己的video变成小窗口， 对方video变成全屏显示
    private TRTCVideoLayout showVideoView(final UserModel userInfo) {
        isChatting = true;
        // 添加到 TRTCVideoLayoutManager, 返回的是对方的layout
        TRTCVideoLayout videoLayout = addUserToManager(userInfo);
        if (videoLayout == null) {
            return null;
        }
        // kl 添加以下代码，一对一视频聊天的
        TRTCVideoLayout myLayout = mLayoutManagerTrtc.findCloudView(mSelfModel.userId);
        RelativeLayout.LayoutParams oriParams = (RelativeLayout.LayoutParams) myLayout.getLayoutParams();
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLayout.getLayoutParams();
        int height = dp2px(131);
        int width = dp2px(73);
        height = oriParams.height;
        width = oriParams.width;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.rightMargin = dp2px(17);
        params.topMargin = dp2px(87);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        myLayout.setLayoutParams(params);
        // -----------------
        videoLayout.setVideoAvailable(!mIsAudioMode);
        videoLayout.setRemoteIconAvailable(mIsAudioMode);
        return videoLayout;
    }

    private TRTCVideoLayout addUserToManager(UserModel userInfo) {
        TRTCVideoLayout layout = mLayoutManagerTrtc.allocCloudVideoView(userInfo.userId);
        return layout;
    }

    private void stopCameraAndFinish() {
        mTRTCCalling.closeCamera();
        finish();
    }

    // ===================kl add fellow=================
    // 挂断电话， 绑定给close按钮的，任何时候可以调用
    public void hangup() {
        if (mRole == TUICalling.Role.CALLED && !isChatting) {
            mTRTCCalling.reject();
        } else {
            mTRTCCalling.hangup();
        }
        stopCameraAndFinish();
        // 主叫还没接听的时候主动挂断
        if (mRole == TUICalling.Role.CALL && !isChatting) {
            mEventHandler.sendEmptyMessage(EventHandler.EVENT_TYPE_ACTIVE_HANGUP);
        }
    }

    public void setHandsFree(boolean mIsHandsFree){
        mTRTCCalling.setHandsFree(mIsHandsFree);
    }
    public void setMicMute(boolean isMicMute){
        mTRTCCalling.setMicMute(isMicMute);
    }

    // 接听电话， 给接听按钮用的
    public void acceptCall() {
        mTRTCCalling.accept();
        showCallingView();
    }

    public void switchCamera() {
        if (!mIsCameraOpen) {
            ToastUtils.showShort(com.tencent.liteav.trtccalling.R.string.trtccalling_switch_camera_hint);
            return;
        }
        mIsFrontCamera = !mIsFrontCamera;
        mTRTCCalling.switchCamera(mIsFrontCamera);
//        mSwitchCameraImg.setActivated(mIsFrontCamera);
        ToastUtils.showLong(com.tencent.liteav.trtccalling.R.string.trtccalling_toast_switch_camera);
    }

    public void switchVideoView() {
        mLayoutManagerTrtc.switchVideoView();
    }

    /**
     * @return void
     * @Desc TODO(是否开启自动增益补偿功能, 可以自动调麦克风的收音量到一定的音量水平)
     * @author 彭石林
     * @parame [openAGC]
     * @Date 2022/2/14
     */
    public void enableAGC(boolean openAGC) {
        mTRTCCalling.enableAGC(openAGC);
    }

    /**
     * @return void
     * @Desc TODO(回声消除器 ， 可以消除各种延迟的回声)
     * @author 彭石林
     * @parame [openAEC]
     * @Date 2022/2/14
     */
    public void enableAEC(boolean openAEC) {
        mTRTCCalling.enableAEC(openAEC);
    }

    /**
     * @return void
     * @Desc TODO(背景噪音抑制功能 ， 可探测出背景固定频率的杂音并消除背景噪音)
     * @author 彭石林
     * @parame [openANS]
     * @Date 2022/2/14
     */
    public void enableANS(boolean openANS) {
        mTRTCCalling.enableANS(openANS);
    }

}
