package com.fine.friendlycc.calling.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aliyun.svideo.common.utils.FastClickUtil;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.databinding.ActivityCallVideoBinding;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.bean.CrystalDetailsConfigBean;
import com.fine.friendlycc.bean.GiftBagBean;
import com.fine.friendlycc.event.CallChatingHangupEvent;
import com.fine.friendlycc.calling.viewmodel.VideoCallViewModel;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocaleManager;
import com.fine.friendlycc.ui.dialog.GiftBagDialog;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.LogUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.dialog.MessageDetailDialog;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.fine.friendlycc.widget.image.CircleImageView;
import com.faceunity.nama.FURenderer;
import com.faceunity.nama.data.FaceUnityDataFactory;
import com.faceunity.nama.ui.FaceUnityView;
import com.google.gson.Gson;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGASoundManager;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.custom.GiftEntity;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.model.TRTCCalling;
import com.tencent.liteav.trtccalling.model.util.TUICallingConstants;
import com.tencent.liteav.trtccalling.ui.base.VideoLayoutFactory;
import com.tencent.liteav.trtccalling.ui.floatwindow.FloatCallView;
import com.tencent.qcloud.tuicore.util.ConfigManagerUtil;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.bus.RxBus;
import me.tatarka.bindingcollectionadapter2.BR;

public class CCLineVideoActivity extends BaseActivity<ActivityCallVideoBinding, VideoCallViewModel>  {

    /**
     * ????????????
     */
    protected TRTCCalling mTRTCCalling;
    private FaceUnityView mFaceUnityView;
    private FaceUnityDataFactory mFaceUnityDataFactory;
    private FURenderer mFURenderer;
    private final boolean isFuEffect = true;

    //???????????????
    private FloatCallView mFloatView;
    private VideoLayoutFactory mVideoFactory;

    private Context mContext;

    private CCTUICallVideoView mCallView;
    private RelativeLayout mContainerView;
    private View mJMView;
    private ObjectAnimator rotation;

    private CallingInviteInfo callingInviteInfo;
    //?????????UserId
    private String callUserId;
    private String toId;
    private Integer roomId;
    private TUICalling.Role role;
    private String[] userIds;

    private int mTimeCount;
    //??????10???+1
    private int mTimeTen;
    private Timer timer;

    private SVGAImageView giftEffects;

    private Runnable timerRunnable = null;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    /**
     * ????????????Manifest.xml??????????????????????????????????????????????????????????????????????????????

     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig!=null){
            LocaleManager.setLocal(this);
        }
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocal(this);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LocaleManager.setLocal(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CCApplication.isCalling = true;
        ImmersionBarUtils.setupStatusBar(this, false, true);
        if (isFuEffect && mFURenderer != null) {
            mFaceUnityDataFactory.bindCurrentRenderer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.activity_call_video;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VideoCallViewModel initViewModel() {
        //??????????????????ViewModelFactory?????????ViewModel????????????????????????????????????????????????LoginViewModel(@NonNull Application application)????????????
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(VideoCallViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        //?????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mContext = this;
        SVGASoundManager.INSTANCE.init();
        SVGAParser.Companion.shareParser().init(this);

        Intent intent = getIntent();
        role = (TUICalling.Role) intent.getExtras().get(TUICallingConstants.PARAM_NAME_ROLE);
        roomId = intent.getIntExtra("roomId", 0);
        //????????????
        userIds = intent.getExtras().getStringArray(TUICallingConstants.PARAM_NAME_USERIDS);
        if (userIds != null && userIds.length > 0) {
            toId = userIds[0];
        }
        //????????????
        callUserId = intent.getExtras().getString(TUICallingConstants.PARAM_NAME_SPONSORID);
        String userData = intent.getExtras().getString("userProfile");
        if (userData != null) {
            callingInviteInfo = new Gson().fromJson(userData, CallingInviteInfo.class);
        }
    }

    /**
     * ?????????
     */
    private void toRecharge() {
        Intent intent = new Intent(this, DialogDiamondRechargeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pop_enter_anim, 0);
    }

    @Override
    public void initData() {
        super.initData();
        hideExchangeRules();
        giftEffects = binding.giftEffects;
        mFaceUnityView = binding.fuView;
        //1.??????????????????
        mTRTCCalling = TRTCCalling.sharedInstance(this);
        mFURenderer = FURenderer.getInstance();
        mFaceUnityDataFactory = new FaceUnityDataFactory(0);
        mFaceUnityView.bindDataFactory(mFaceUnityDataFactory);
        mTRTCCalling.createCustomRenderer(this, true, isFuEffect);

        mContainerView = findViewById(R.id.container);
        mJMView = findViewById(R.id.jm_view);
        mVideoFactory = new VideoLayoutFactory(this);
        LogUtils.i("callingInviteInfo: "+callingInviteInfo);
        if (callingInviteInfo != null) {
            mCallView = new CCTUICallVideoView(this, role, userIds, callUserId, null, false, callingInviteInfo.getRoomId(),mVideoFactory) {
                @Override
                public void finish() {
                    super.finish();
                    //2??????????????????
                    if (!FastClickUtil.isFastCallFun("CallingVideoActivity")) {
                        RxBus.getDefault().post(new CallChatingHangupEvent());
                    }
                    Log.i("JM_trtc", "finish: ");
                    CCLineVideoActivity.this.finish();
                }
            };
        } else {
            mCallView = new CCTUICallVideoView(this, role, userIds, callUserId, null, false,mVideoFactory) {
                @Override
                public void finish() {
                    super.finish();
                    //2??????????????????
                    if (!FastClickUtil.isFastCallFun("CallingVideoActivity")) {
                        RxBus.getDefault().post(new CallChatingHangupEvent());
                    }
                    Log.i("JM_trtc", "finish: ");
                    CCLineVideoActivity.this.finish();
                }
            };
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mContainerView.addView(mCallView, params);
        mJMView.bringToFront();
        // ??????TRTC sponsor ????????????????????????????????????????????????????????? ???????????? my ??? other
        String myUserId = V2TIMManager.getInstance().getLoginUser();
        String otherUserId = (role == TUICalling.Role.CALL ? userIds[0] : callUserId);
        if (role == TUICalling.Role.CALL) {//????????????
            callUserId = V2TIMManager.getInstance().getLoginUser();
            viewModel.userCall = true;
            if (callingInviteInfo != null) {
                viewModel.init(callUserId, toId, role, mCallView, callingInviteInfo.getRoomId());
                viewModel.callingInviteInfoField.set(callingInviteInfo);
                if(callingInviteInfo.getPaymentRelation().getPayerUserId() == Injection.provideDemoRepository().readUserData().getId()){
                    if (!ObjectUtils.isEmpty(callingInviteInfo.getMessages()) && callingInviteInfo.getMessages().size() > 0) {
                        String valueData = "";
                        for (String value : callingInviteInfo.getMessages()) {
                            valueData += value + "\n";
                        }
                        viewModel.callHintBinding.set(valueData);
                    }
                }
            }
        } else {//????????????
            toId = V2TIMManager.getInstance().getLoginUser();
            viewModel.init(myUserId, otherUserId, role, mCallView);
            viewModel.getCallingInvitedInfo(2, callUserId);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //??????????????????
        viewModel.uc.clickCrystalExchange.observe(this, data -> {
            TraceDialog.getInstance(CCLineVideoActivity.this)
                    .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                        @Override
                        public void confirm(Dialog dialog) {
                            ConfigManagerUtil.getInstance().putExchangeRulesFlag(true);
                            viewModel.isHideExchangeRules.set(true);
                        }
                    })
                    .getCrystalExchange(data)
                    .show();
        });

        //????????????????????????
        viewModel.uc.startVideoUpSayHiAnimotor.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                if (rotation == null){
                    rotation = ObjectAnimator.ofFloat(binding.ivVideoUpSayHi, "rotation", 0.0F, 360.0F);
                }
                if (!rotation.isRunning()){
//                    rotation.setRepeatMode(ValueAnimator.RESTART);
//                    rotation.setRepeatCount(-1);
                    rotation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            viewModel.getSayHiList();
                        }
                    });
                    rotation.setDuration(800);//??????????????????
                    rotation.start();//??????????????????????????????????????????
                }

            }
        });
        //???????????????????????????
        viewModel.uc.scrollToEnd.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.rcvLayout.scrollToPosition(viewModel.adapter.getItemCount() - 1);
            }
        });
        //??????????????????????????????
        viewModel.uc.closeViewHint.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                onBackViewCLick();
            }
        });
        //????????????????????????
        viewModel.uc.acceptUserGift.observe(this, new Observer<GiftEntity>() {
            @Override
            public void onChanged(GiftEntity giftEntity) {
                viewModel.getCallingStatus(viewModel.roomId);
                try {
                    int account = giftEntity.getAmount();
                    //??????SVG??????
                    startVideoAcceptSVGAnimotion(giftEntity);
                    //??????????????????
                    startVideoAcceptBannersAnimotion(giftEntity, account);
                    //??????????????????
                    startVideoAcceptHeadAnimotion(giftEntity);
                } catch (Exception e) {

                }
            }
        });
        //????????????????????????
        viewModel.uc.sendUserGiftAnim.observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> stringObjectMap) {
                try {
                    int account = (int) stringObjectMap.get("account");
                    GiftBagBean.giftEntity giftEntity = (GiftBagBean.giftEntity) stringObjectMap.get("giftEntity");
                    //??????SVG??????
                    startVideoSendSvgAnimotion(giftEntity);
                    //??????????????????
                    startVideoSendBannersAnimotion(account, giftEntity);
                    //??????????????????
                    startVideoSendHeadAnimotion(giftEntity);
                } catch (Exception e) {

                }
            }
        });
        //????????????
        viewModel.uc.callAudioStart.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                //????????????
                TimeCallMessage();
                setTimerForCallinfo();
            }
        });
        //????????????????????????
        viewModel.uc.sendUserGiftError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isGiftSend) {
//
//                GameCoinExchargeSheetView coinRechargeSheetView = new GameCoinExchargeSheetView(CallingVideoActivity.this);
//                coinRechargeSheetView.setCallMedia(true);
//                coinRechargeSheetView.setMaleBalance(viewModel.maleBalanceMoney);
//                coinRechargeSheetView.show();
//                coinRechargeSheetView.setCoinRechargeSheetViewListener(new GameCoinExchargeSheetView.CoinRechargeSheetViewListener() {
//                    @Override
//                    public void onPaySuccess(GameCoinExchargeSheetView sheetView, CoinExchangePriceInfo sel_goodsEntity) {
//                        sheetView.dismiss();
//                        viewModel.getCallingStatus(viewModel.roomId);
//                    }
//
//                    @Override
//                    public void onPayFailed(GameCoinExchargeSheetView sheetView, String msg) {
//                        sheetView.dismiss();
//                    }
//                });
                toRecharge();
            }
        });
        //??????????????????
        viewModel.uc.callGiftBagAlert.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                if (viewModel.unitPriceList == null ||  viewModel.maleBalanceMoney == 0){
                    return;
                }
                GiftBagDialog giftBagDialog = new GiftBagDialog(mContext, true, viewModel.maleBalanceMoney, viewModel.unitPriceList.size() > 1 ? 4 : 0);
                giftBagDialog.setGiftOnClickListener(new GiftBagDialog.GiftOnClickListener() {
                    @Override
                    public void sendGiftClick(Dialog dialog, int number, GiftBagBean.giftEntity giftEntity) {
                        dialog.dismiss();
                        CCApplication.instance().logEvent(AppsFlyerEvent.videocall_send_gift);
                        viewModel.sendUserGift(dialog, giftEntity, viewModel.callingVideoInviteInfoField.get().getId(), number);
                    }

                    @Override
                    public void rechargeStored(Dialog dialog) {
                        dialog.dismiss();
                        CCApplication.instance().logEvent(AppsFlyerEvent.videocall_gift_topup);
                        viewModel.uc.sendUserGiftError.postValue(false);
                    }
                });
                giftBagDialog.show();
            }
        });
    }


    /**
     * ??????????????????????????????
     */
    private void hideExchangeRules() {
        CrystalDetailsConfigBean crystalDetailsConfig = ConfigManager.getInstance().getAppRepository().readCrystalDetailsConfig();
        boolean isHideExchangeRules = ConfigManagerUtil.getInstance().getExchangeRulesFlag();
        boolean isMale = ConfigManager.getInstance().isMale();
        if (isMale){
            if (crystalDetailsConfig.getMaleIsShow() != 1 || isHideExchangeRules){
                viewModel.isHideExchangeRules.set(true);
            }else {
                viewModel.isHideExchangeRules.set(false);
            }
        }else {
            if (crystalDetailsConfig.getFemaleIsShow() != 1 || isHideExchangeRules){
                viewModel.isHideExchangeRules.set(true);
            }else {
                viewModel.isHideExchangeRules.set(false);
            }
        }
    }

    private void setTimerForCallinfo() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(isFinishing() || isDestroyed()){
                        return;
                    }
                    viewModel.getCallingStatus(viewModel.roomId);
                }catch (Exception ignored){

                }
            }
        }, 1000,10000);
    }

    private void startVideoSendHeadAnimotion(GiftBagBean.giftEntity giftEntity) {
        ImageView giftImageTrans = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(50), dip2px(50));
        layoutParams.bottomMargin = dip2px(43);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.gravity = Gravity.BOTTOM;
        giftImageTrans.setLayoutParams(layoutParams);
        giftImageTrans.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_call_video_send_tip);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postRemoveView(binding.mainView,giftImageTrans);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Glide.with(mContext)
                .asBitmap()
                .load(StringUtil.getFullImageUrl(giftEntity.getImg()))
                .error(R.drawable.radio_program_list_content)
                .placeholder(R.drawable.radio_program_list_content)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(giftImageTrans);
        binding.mainView.addView(giftImageTrans);
        giftImageTrans.startAnimation(animation);
    }

    private void startVideoSendBannersAnimotion(int account, GiftBagBean.giftEntity giftEntity) {
        if (account > 1) {
            View streamerView = View.inflate(mContext, R.layout.call_user_streamer_item, null);
            //????????????
            CircleImageView userImg = streamerView.findViewById(R.id.user_img);
            //????????????
            ImageView giftImg = streamerView.findViewById(R.id.gift_img);
            //??????????????????
            ImageView giftNumImg = streamerView.findViewById(R.id.gift_num_img);
            //??????
            TextView tipText = streamerView.findViewById(R.id.tip_text);
            String sexText = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt17);
            String messageText = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt1);
            String itemTextMessage = sexText + messageText + "\n" + viewModel.callingVideoInviteInfoField.get().getNickname();
            SpannableString stringBuilder = new SpannableString(itemTextMessage);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), 3, itemTextMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tipText.setText(stringBuilder);
            Glide.with(mContext)
                    .asBitmap()
                    .load(StringUtil.getFullImageUrl(viewModel.readUserData().getAvatar()))
                    .error(R.drawable.default_avatar)
                    .placeholder(R.drawable.default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(userImg);
            Glide.with(mContext)
                    .asBitmap()
                    .load(StringUtil.getFullImageUrl(giftEntity.getImg()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(giftImg);
            if (account == 1) {
                giftNumImg.setImageResource(R.drawable.img_gift_num1);
            } else if (account == 10) {
                giftNumImg.setImageResource(R.drawable.img_gift_num10);
            } else if (account == 38) {
                giftNumImg.setImageResource(R.drawable.img_gift_num38);
            } else if (account == 66) {
                giftNumImg.setImageResource(R.drawable.img_gift_num66);
            } else if (account == 188) {
                giftNumImg.setImageResource(R.drawable.img_gift_num188);
            } else if (account == 520) {
                giftNumImg.setImageResource(R.drawable.img_gift_num520);
            } else if (account == 1314) {
                giftNumImg.setImageResource(R.drawable.img_gift_num1314);
            } else if (account == 3344) {
                giftNumImg.setImageResource(R.drawable.img_gift_num3344);
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_call_audio_right_in_streamer);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    postRemoveView(binding.mainView,streamerView);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = dip2px(14);
            layoutParams.topMargin = dip2px(330);
            streamerView.setLayoutParams(layoutParams);
            binding.mainView.addView(streamerView);
            streamerView.startAnimation(animation);
        }
    }

    private void startVideoSendSvgAnimotion(GiftBagBean.giftEntity giftEntity) {
        SVGAParser svgaParser = SVGAParser.Companion.shareParser();
        try {
            svgaParser.decodeFromURL(new URL(StringUtil.getFullAudioUrl(giftEntity.getLink())), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    giftEffects.setVisibility(View.VISIBLE);
                    giftEffects.setVideoItem(videoItem);
                    giftEffects.setLoops(1);
                    giftEffects.setCallback(new SVGACallback() {
                        @Override
                        public void onPause() {

                        }

                        @Override
                        public void onFinished() {
                            giftEffects.setVisibility(View.GONE);
                        }

                        @Override
                        public void onRepeat() {
                        }

                        @Override
                        public void onStep(int i, double v) {
                        }
                    });
                    giftEffects.startAnimation();
                }

                @Override
                public void onError() {
                }
            }, null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void startVideoAcceptHeadAnimotion(GiftEntity giftEntity) {
        ImageView giftImageTrans = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(32), dip2px(32));
        layoutParams.gravity = Gravity.START;
        layoutParams.leftMargin = dip2px(17);
        layoutParams.topMargin = dip2px(44);
        giftImageTrans.setLayoutParams(layoutParams);
        giftImageTrans.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_call_video_receive_tip);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postRemoveView(binding.mainView,giftImageTrans);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Glide.with(mContext)
                .asBitmap()
                .load(StringUtil.getFullImageUrl(giftEntity.getImgPath()))
                .error(R.drawable.radio_program_list_content)
                .placeholder(R.drawable.radio_program_list_content)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(giftImageTrans);
        giftImageTrans.setElevation(22);
        binding.mainView.addView(giftImageTrans);
        giftImageTrans.startAnimation(animation);
    }

    //????????????
    private void startVideoAcceptBannersAnimotion(GiftEntity giftEntity, int account) {
        if (account > 1) {
            View streamerView = View.inflate(mContext, R.layout.call_user_streamer_item, null);
            //????????????
            CircleImageView userImg = streamerView.findViewById(R.id.user_img);
            //????????????
            ImageView giftImg = streamerView.findViewById(R.id.gift_img);
            //??????????????????
            ImageView giftNumImg = streamerView.findViewById(R.id.gift_num_img);
            //??????
            TextView tipText = streamerView.findViewById(R.id.tip_text);
            String sexText = viewModel.callingVideoInviteInfoField.get().getNickname();
            String messageText = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt1);
            String lastText = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt17);
            String itemTextMessage = sexText + "\n" + messageText + lastText;
            SpannableString stringBuilder = new SpannableString(itemTextMessage);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), 0, sexText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), sexText.length(), sexText.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), sexText.length() + 2, itemTextMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tipText.setText(stringBuilder);
            Glide.with(mContext)
                    .asBitmap()
                    .load(StringUtil.getFullImageUrl(viewModel.callingVideoInviteInfoField.get().getAvatar()))
                    .error(R.drawable.default_avatar)
                    .placeholder(R.drawable.default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(userImg);
            Glide.with(mContext)
                    .asBitmap()
                    .load(StringUtil.getFullImageUrl(giftEntity.getImgPath()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(giftImg);
            if (account == 1) {
                giftNumImg.setImageResource(R.drawable.img_gift_num1);
            } else if (account == 10) {
                giftNumImg.setImageResource(R.drawable.img_gift_num10);
            } else if (account == 38) {
                giftNumImg.setImageResource(R.drawable.img_gift_num38);
            } else if (account == 66) {
                giftNumImg.setImageResource(R.drawable.img_gift_num66);
            } else if (account == 188) {
                giftNumImg.setImageResource(R.drawable.img_gift_num188);
            } else if (account == 520) {
                giftNumImg.setImageResource(R.drawable.img_gift_num520);
            } else if (account == 1314) {
                giftNumImg.setImageResource(R.drawable.img_gift_num1314);
            } else if (account == 3344) {
                giftNumImg.setImageResource(R.drawable.img_gift_num3344);
            }
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_call_audio_right_in_streamer);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    postRemoveView(binding.mainView,streamerView);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = dip2px(14);
            layoutParams.topMargin = dip2px(330);
            streamerView.setElevation(15);
            streamerView.setLayoutParams(layoutParams);
            binding.mainView.addView(streamerView);
            streamerView.startAnimation(animation);
        }
    }

    private void startVideoAcceptSVGAnimotion(GiftEntity giftEntity) {
        if (!StringUtils.isEmpty(giftEntity.getSvgaPath())) {
            SVGAParser svgaParser = SVGAParser.Companion.shareParser();
            try {
                svgaParser.decodeFromURL(new URL(StringUtil.getFullAudioUrl(giftEntity.getSvgaPath())), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                        Log.d("##", "## FromNetworkActivity load onComplete");
                        giftEffects.setVisibility(View.VISIBLE);
                        giftEffects.setVideoItem(videoItem);
                        giftEffects.setLoops(1);
                        giftEffects.setCallback(new SVGACallback() {
                            @Override
                            public void onPause() {
                            }

                            @Override
                            public void onFinished() {
                                //????????????
                                giftEffects.setVisibility(View.GONE);
                            }

                            @Override
                            public void onRepeat() {
                            }

                            @Override
                            public void onStep(int i, double v) {
                            }
                        });
                        giftEffects.startAnimation();
                    }

                    @Override
                    public void onError() {
                    }
                }, null);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    //????????????view
    private void postRemoveView(ViewGroup viewGroup, View IiageTrans) {
        viewGroup.post(new Runnable() {
            public void run () {
                // it works without the runOnUiThread, but all UI updates must
                // be done on the UI thread
                CCLineVideoActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        viewGroup.removeView(IiageTrans);
                    }
                });
            }
        });
    }

    //????????????
    @Override
    public void onBackPressed() {
        onBackViewCLick();
    }

    //????????????????????????
    public void onBackViewCLick() {
        if (viewModel.collected == null) {
            viewModel.hangup();
            return;
        }
        if (viewModel.isMale) {
            if (viewModel.collected == 1) {//?????????
                String title = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt9);
                MessageDetailDialog.callAudioHint2(mContext, title, null, new MessageDetailDialog.AudioCallHintOnClickListener() {
                    @Override
                    public void check1OnClick() {
                        CCApplication.instance().logEvent(AppsFlyerEvent.videocall_close_hangup_M);
                        viewModel.hangup();
                    }

                    @Override
                    public void check2OnClick() {
                        CCApplication.instance().logEvent(AppsFlyerEvent.videocall_close_goon_M);
                    }
                }).show();
            } else {//????????????
                MessageDetailDialog.callAudioHint(mContext, new MessageDetailDialog.AudioCallHintOnClickListener() {
                    @Override
                    public void check1OnClick() {
                        CCApplication.instance().logEvent(AppsFlyerEvent.videocall_close_follow_M);
                        viewModel.addLike(true);
                    }

                    @Override
                    public void check2OnClick() {
                        CCApplication.instance().logEvent(AppsFlyerEvent.videocall_close_hangup_M);
                        viewModel.hangup();
                    }
                }).show();
            }
        } else {
            String title = mContext.getString(R.string.playcc_call_message_deatail_girl_txt13);
            String content = mContext.getString(R.string.playcc_call_message_deatail_girl_txt12);
            MessageDetailDialog.callAudioHint2(mContext, title, content, new MessageDetailDialog.AudioCallHintOnClickListener() {
                @Override
                public void check1OnClick() {
                    CCApplication.instance().logEvent(AppsFlyerEvent.videocall_close_hangup_F);
                    viewModel.hangup();
                }

                @Override
                public void check2OnClick() {
                    CCApplication.instance().logEvent(AppsFlyerEvent.videocall_close_goon_F);
                }
            }).show();
        }
    }

    /**
     * ?????????????????????????????????
     */
    public void TimeCallMessage() {
        if (timerRunnable != null) {
            return;
        }
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                mTimeCount++;
                viewModel.TimeCount++;
                viewModel.timeTextField.set(mContext.getString(R.string.playcc_call_message_deatail_time_msg, mTimeCount/3600, mTimeCount / 60, mTimeCount % 60));
                if (mTimeCount>=5){viewModel.tipSwitch.set(false);}
                if (!viewModel.sayHiEntityHidden.get() && mTimeCount % 10 == 0) {
                    //???10???????????????????????????
                    viewModel.getSayHiList();
                }
                if (mTimeCount % 30 == 0){
                    viewModel.getRoomStatus(viewModel.roomId);
                }
                if (viewModel.callInfoLoaded && viewModel.isShowTipMoney.get()){
                    //????????????????????????
                    if (!viewModel.isPayee) {
                        if (viewModel.totalMinutesRemaining <= viewModel.balanceNotEnoughTipsMinutes * 60) {
                            viewModel.totalMinutesRemaining--;
                            if (viewModel.totalMinutesRemaining < 0) {
                                viewModel.hangup();
                                return;
                            }
                            String minute = StringUtils.getString(R.string.playcc_minute);
                            String textHint = (viewModel.totalMinutesRemaining / 60) + minute + (viewModel.totalMinutesRemaining % 60);
                            String txt = String.format(StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt14), textHint);
                            viewModel.maleTextMoneyField.set(txt);
                            if (!viewModel.flagMoneyNotWorth) {
                                moneyNoWorthSwich(true);
                            }

                        }else{
                            if (viewModel.flagMoneyNotWorth) {
                                moneyNoWorthSwich(false);
                            }
                        }
                    }else {
                        setProfitTips();
                    }
                }

                mHandler.postDelayed(timerRunnable, 1000);
            }
        };
        mHandler.postDelayed(timerRunnable, 1000);
    }

    /**
     * ???????????????????????????
     */
    private void setProfitTips() {
        if (!viewModel.isShowCountdown.get() && viewModel.payeeProfits > 0) {//??????????????????????????????
            if (!viewModel.girlEarningsField.get()) {
                viewModel.girlEarningsField.set(true);
            }
            String profit = viewModel.payeeProfits + "";
            String girlEarningsTex = String.format(StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt), profit);
            SpannableString stringBuilder = new SpannableString(girlEarningsTex);
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint1));
            int index = girlEarningsTex.indexOf(profit);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), 0, girlEarningsTex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(blueSpan, index, index + profit.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewModel.girlEarningsText.set(stringBuilder);
        }
    }

    /**
     * ???????????????????????????
     * @param isShow
     */
    private void moneyNoWorthSwich(boolean isShow) {
        viewModel.flagMoneyNotWorth = isShow;
        viewModel.maleTextLayoutSHow.set(isShow);
        //????????????????????????????????????
        if (isShow){
            viewModel.getTips(viewModel.callingVideoInviteInfoField.get().getId(),2,"1");
        }else {
            viewModel.getTips(viewModel.callingVideoInviteInfoField.get().getId(),2,"0");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CCApplication.isCalling = false;
        //????????????
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //????????????
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mHandler != null) {
            mHandler.removeCallbacks(timerRunnable);
            mHandler = null;
        }
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    /**
     * ??????????????????????????? dp ????????? ????????? px(??????)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}