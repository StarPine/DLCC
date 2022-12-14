package com.fine.friendlycc.calling.view;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.bean.AudioCallingBarrageBean;
import com.fine.friendlycc.bean.CallingInfoBean;
import com.fine.friendlycc.bean.CallingStatusBean;
import com.fine.friendlycc.bean.RestartActivityBean;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.image.CircleImageView;
import com.google.gson.Gson;
import com.tencent.custom.GiftEntity;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.ui.base.BaseTUICallView;
import com.tencent.liteav.trtccalling.ui.floatwindow.FloatWindowService;
import com.tencent.qcloud.tuicore.Status;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.util.ChatMessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;

public class AudioFloatCallView extends BaseTUICallView {
    private static final String TAG = "AudioFloatCallView";
    private ImageView maximize;
    private CircleImageView ivAvatar;
    private TextView mTextViewTimeCount;
    private boolean isRestart = false;
    private Integer roomId = 0;
    private CallingInfoBean.FromUserProfile otherUserProfile;
    private ArrayList<AudioCallingBarrageBean> audioBarrageList;


    public AudioFloatCallView(Context context, TUICalling.Role role, TUICalling.Type type, String[] userIDs,
                              String sponsorID, String groupID, boolean isFromGroup,
                              CallingInfoBean.FromUserProfile otherUserProfile, int timeCount,
                              Integer roomId, ArrayList<AudioCallingBarrageBean> audioBarrageList) {
        super(context, role, type, userIDs, sponsorID, groupID, isFromGroup);
        initData(otherUserProfile, timeCount, roomId,audioBarrageList);
    }

    private void initData(CallingInfoBean.FromUserProfile otherUserProfile, int timeCount, Integer roomId, ArrayList<AudioCallingBarrageBean> audioBarrageList) {
        Glide.with(CCApplication.instance())
                .load(StringUtil.getFullImageUrl(otherUserProfile.getAvatar()))
                .error(R.drawable.default_avatar) //???????????????????????????
                .placeholder(R.drawable.default_avatar) //??????????????????????????????
                .fallback(R.drawable.default_avatar) //url???????????????,???????????????
                .into(ivAvatar);
        this.roomId = roomId;
        this.otherUserProfile = otherUserProfile;
        this.audioBarrageList = audioBarrageList;
        showTimeCount(mTextViewTimeCount, timeCount);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initIMListener();
        initListener();
        showFloatWindow();
    }

    @Override
    protected void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.audio_floatwindow_layout, this);
        maximize = findViewById(R.id.iv_maximize);
        ivAvatar = findViewById(R.id.iv_avatar);
        mTextViewTimeCount = findViewById(R.id.tv_time);
    }

    //????????????
    private void showFloatWindow() {
        Status.mIsShowFloatWindow = true;
    }

    //????????????,??????UI?????????????????????????????????
    protected void showTimeCount(TextView view, int timeCount) {
        if (mTimeRunnable != null) {
            return;
        }
        mTimeCount = timeCount;
        if (null != view) {
            view.setText(getShowTime(++mTimeCount));
        }
        mTimeRunnable = new Runnable() {
            @Override
            public void run() {
                mTimeCount++;
                if (mTimeCount %10 ==0){
                    getRoomStatus(roomId);
                }
                Status.mBeginTime = mTimeCount;
                if (null != view) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Status.mIsShowFloatWindow) {
                                view.setText(getShowTime(mTimeCount));
                            }
                        }
                    });
                }
                mTimeHandler.postDelayed(mTimeRunnable, 1000);
            }
        };
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    //??????IM??????
    private void initIMListener() {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {//???????????????
                try {
                    if (msg != null && otherUserProfile != null) {
                        TUIMessageBean info = ChatMessageBuilder.buildMessage(msg);
                        if (info != null) {
                            if (info.getV2TIMMessage().getSender().equals(otherUserProfile.getImId())) {
                                String text = String.valueOf(info.getExtra());
                                if (StringUtil.isJSON2(text) && text.contains("type")) {//????????????????????????
                                    Map<String, Object> map_data = new Gson().fromJson(text, Map.class);
                                    //????????????
                                    if (map_data != null
                                            && map_data.get("type") != null
                                            && map_data.get("type").equals("message_gift")
                                            && map_data.get("is_accost") == null) {
                                        GiftEntity giftEntity = new Gson().fromJson(String.valueOf(map_data.get("data")), GiftEntity.class);
                                        //??????????????????
                                        showGiftBarrage(giftEntity);
                                        //??????????????????
                                        giftIncome(giftEntity);
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onRecvC2CReadReceipt(List<V2TIMMessageReceipt> receiptList) {
                super.onRecvC2CReadReceipt(receiptList);
            }

            @Override
            public void onRecvMessageRevoked(String msgID) {
                super.onRecvMessageRevoked(msgID);
            }

            @Override
            public void onRecvMessageModified(V2TIMMessage msg) {
                super.onRecvMessageModified(msg);
            }
        });
    }

    private void initListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRestart) {
                    isRestart = true;
                    Intent intent = new Intent(mContext, AudioCallChatingActivity.class);
                    intent.putExtra("fromUserId", mSponsorID);
                    intent.putExtra("toUserId", mUserIDs[0]);
                    intent.putExtra("mRole", mRole);
                    intent.putExtra("roomId", roomId);
                    intent.putExtra("timeCount", ++mTimeCount);
                    intent.putExtra("isRestart", isRestart);
                    intent.putExtra("audioCallingBarrage", GsonUtils.toJson(audioBarrageList));
                    if (isBackground(mContext)) {
                        mContext.startActivity(intent);
                    } else {
                        RxBus.getDefault().post(new RestartActivityBean(intent));
                    }
                }

            }
        });
    }


    /**
     * ??????????????????
     *
     * @param giftEntity
     */
    private void showGiftBarrage(GiftEntity giftEntity) {
        String sexText = isMale() ? StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt3) : StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt2);
        String messageText = otherUserProfile.getNickname() + " " + StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt1) + " " + sexText + " " + giftEntity.getTitle() + "x" + giftEntity.getAmount();
        AudioCallingBarrageBean barrageEntity = new AudioCallingBarrageBean(messageText ,giftEntity.getImgPath(),true);
        audioBarrageList.add(barrageEntity);
    }

    private boolean isMale(){
        return ConfigManager.getInstance().isMale();
    }

    /**
     * ??????????????????
     *
     * @param giftEntity
     */
    private void giftIncome(GiftEntity giftEntity) {
        double total = giftEntity.getAmount().intValue() * giftEntity.getProfitTwd().doubleValue();
        String itemMessage = String.format(StringUtils.getString(R.string.profit), String.format("%.2f", total));
        AudioCallingBarrageBean barrageEntity = new AudioCallingBarrageBean(itemMessage ,"",false);
        audioBarrageList.add(barrageEntity);
    }

    /**
     * ???????????????????????????
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    //??????????????????
    public void getRoomStatus(Integer roomId) {
        Injection.provideDemoRepository().getRoomStatus(roomId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CallingStatusBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingStatusBean> response) {
                        CallingStatusBean data = response.getData();
                        Integer roomStatus = data.getRoomStatus();
                        if (roomStatus != null && roomStatus != 101) {
                            onCallEnd();
                        }
                    }
                });
    }

    @Override
    public void onCallEnd() {
        super.onCallEnd();
        //????????????,?????????????????????
        if (Status.mIsShowFloatWindow) {
            FloatWindowService.stopService(mContext);
            finish();
        }
    }

}