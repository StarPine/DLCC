package com.fine.friendlycc.calling.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aliyun.svideo.common.utils.FastClickUtil;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.ActivityCallAudioChatingBinding;
import com.fine.friendlycc.bean.AudioCallingBarrageBean;
import com.fine.friendlycc.bean.CrystalDetailsConfigBean;
import com.fine.friendlycc.bean.GiftBagBean;
import com.fine.friendlycc.bean.ShowFloatWindowBean;
import com.fine.friendlycc.event.CallChatingHangupEvent;
import com.fine.friendlycc.calling.viewmodel.AudioCallChatingItemViewModel;
import com.fine.friendlycc.calling.viewmodel.AudioCallChatingViewModel;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocaleManager;
import com.fine.friendlycc.ui.dialog.GiftBagDialog;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.MiuiUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.dialog.MessageDetailDialog;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.fine.friendlycc.widget.image.CircleImageView;
import com.google.gson.reflect.TypeToken;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGASoundManager;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.custom.GiftEntity;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.ui.floatwindow.FloatWindowService;
import com.tencent.qcloud.tuicore.Status;
import com.tencent.qcloud.tuicore.util.ConfigManagerUtil;
import com.tencent.qcloud.tuikit.tuichat.ui.view.MyImageSpan;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BR;

public class AudioCallChatingActivity extends BaseActivity<ActivityCallAudioChatingBinding, AudioCallChatingViewModel> implements Ifinish {

    private String barrageInfo;//弹幕数据
    private String inviterImId;//邀請人id
    private String receiverImId;//接收人id
    private TUICalling.Role mRole;
    private Integer roomId;
    private Context mContext;
    //音频悬浮框
    private AudioFloatCallView   mFloatView;

    private int mTimeCount;
    private boolean isRestart;
    //每个10秒+1
    private int mTimeTen;
    private int minuteTime;

    private SVGAImageView giftEffects;

    private Runnable timerRunnable = null;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });
    private ObjectAnimator rotation;
    private Timer timer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    /**
     * 就算你在Manifest.xml设置横竖屏切换不重走生命周期。横竖屏切换还是会走这里

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
        FloatWindowService.stopService(this);
        CCApplication.isCalling = true;
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.activity_call_audio_chating;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AudioCallChatingViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(AudioCallChatingViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        //防窥屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Status.mCallStatus = Status.CALL_STATUS.ACCEPT;
        SVGASoundManager.INSTANCE.init();
        SVGAParser.Companion.shareParser().init(this);
        mContext = this;
        Intent intent = getIntent();
        inviterImId = intent.getStringExtra("fromUserId");
        receiverImId = intent.getStringExtra("toUserId");
        mRole = (TUICalling.Role)intent.getExtras().get("mRole");
        roomId = intent.getIntExtra("roomId", 0);
        mTimeCount = intent.getIntExtra("timeCount", 0);
        isRestart = intent.getBooleanExtra("isRestart", false);
        barrageInfo = intent.getStringExtra("audioCallingBarrage");

    }

    @Override
    public void initData() {
        super.initData();
        hideExchangeRules();
        giftEffects = binding.giftEffects;
        if (isRestart){
            viewModel.tipSwitch.set(false);
            viewModel.TimeCount = mTimeCount;
            TimeCallMessage();
            setTimerForCallinfo();
            loadBarrageInfo();
        }
        viewModel.init(this);
        viewModel.roomId = roomId;
        viewModel.fromUserId = inviterImId;
        viewModel.toUserId = receiverImId;
        viewModel.getCallingInfo(roomId, inviterImId, receiverImId);
    }

    /**
     * 解析弹幕数据
     * @param json
     * @return
     */
    public List<AudioCallingBarrageBean> getAudioCallingBarrageInfo(String json) {
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<AudioCallingBarrageBean> list = GsonUtils.fromJson(json, new TypeToken<List<AudioCallingBarrageBean>>() {
        }.getType());
        return list;
    }

    /**
     * 加载弹幕数据
     */
    private void loadBarrageInfo() {
        ForegroundColorSpan blueSpanWhite = new ForegroundColorSpan(ColorUtils.getColor(R.color.white));
        List<AudioCallingBarrageBean> audioCallingBarrageInfo = getAudioCallingBarrageInfo(barrageInfo);
        for (AudioCallingBarrageBean barrageEntity : audioCallingBarrageInfo) {
            SpannableString itemtext = new SpannableString(barrageEntity.getItemText());
            itemtext.setSpan(blueSpanWhite, 0, barrageEntity.getItemText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (barrageEntity.getItemText().startsWith("C+")){
                itemtext.setSpan(new MyImageSpan(getApplication(),R.drawable.icon_crystal),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            AudioCallChatingItemViewModel audioCallChatingItemViewModel = new AudioCallChatingItemViewModel(viewModel,itemtext , barrageEntity.getImgPath(), barrageEntity.isSendGiftBag());
            viewModel.observableList.add(audioCallChatingItemViewModel);
        }
    }

    //开启悬浮窗
    private boolean startFloatService() {
        if (isFinishing() || isDestroyed()) {
            return true;
        }
        if (Status.mIsShowFloatWindow) {
            return true;
        }
        if (MiuiUtils.checkFloatWindowPermission(this)) {
            mFloatView = createFloatView();
            if (mFloatView == null){
                return true;
            }
            FloatWindowService.startFloatService(this, mFloatView);
            RxBus.getDefault().post(new ShowFloatWindowBean(true));
        }
        return false;
    }

    //创建悬浮窗视图
    private AudioFloatCallView createFloatView() {
        String[] userIds = new String[]{receiverImId};
        if (viewModel.leftUserInfoField.get() == null){
            return null;
        }
        ArrayList<AudioCallingBarrageBean> audioCallChatingItemViewModelList = new ArrayList<>();
        for (AudioCallChatingItemViewModel audioCallChatingItemViewModel : viewModel.observableList) {
            SpannableString itemText = audioCallChatingItemViewModel.itemText.get();
            String imgPath = audioCallChatingItemViewModel.imgPath.get();
            boolean sendGiftBag = audioCallChatingItemViewModel.sendGiftBag.get();
            AudioCallingBarrageBean audioCallingBarrageEntity = new AudioCallingBarrageBean(itemText.toString(),imgPath,sendGiftBag);
            audioCallChatingItemViewModelList.add(audioCallingBarrageEntity);
        }
        return new AudioFloatCallView(this, mRole, TUICalling.Type.AUDIO, userIds, inviterImId,
                null, false,viewModel.leftUserInfoField.get(),mTimeCount,roomId, audioCallChatingItemViewModelList);
    }

    private void requestSettingCanDrawOverlays() {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {//8.0以上
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-8.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {//4.4-6.0以下
            //无需处理了
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.ivMinimize.setOnClickListener(v -> {
            if (!MiuiUtils.checkFloatWindowPermission(this)) {
                requestSettingCanDrawOverlays();
                ToastUtils.showLong(getString(R.string.playcc_float_permission));
                return;
            }
            boolean isError = startFloatService();
            if (!isError){
                finish();
                overridePendingTransition(0, R.anim.anim_zoom_out);
            }
        });
        //公屏消息滚动到底部
        viewModel.uc.scrollToEnd.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.rcvLayout.scrollToPosition(viewModel.adapter.getItemCount() - 1);
            }
        });
        //水晶兑换规则
        viewModel.uc.clickCrystalExchange.observe(this, data -> {
            TraceDialog.getInstance(AudioCallChatingActivity.this)
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

        //破冰文案刷新動畫
        viewModel.uc.startUpSayHiAnimotor.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                if (rotation == null) {
                    rotation = ObjectAnimator.ofFloat(binding.ivUpSayHi, "rotation", 0.0F, 360.0F);
                }
                if (!rotation.isRunning()) {
//                    rotation.setRepeatMode(ValueAnimator.RESTART);
//                    rotation.setRepeatCount(-1);
                    rotation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            viewModel.getSayHiList();
                        }
                    });
                    rotation.setDuration(800);//设置旋转时间
                    rotation.start();//开始执行动画（顺时针旋转动画
                }

            }
        });

        //关闭按钮点击事件回调
        viewModel.uc.closeViewHint.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                onBackViewCLick();
            }
        });
        //接收礼物效果展示
        viewModel.uc.acceptUserGift.observe(this, new Observer<GiftEntity>() {
            @Override
            public void onChanged(GiftEntity giftEntity) {
                viewModel.getCallingStatus(roomId);
                int account = giftEntity.getAmount();
                //启动SVG动画
                startAcceptSVGAnimotion(giftEntity);
                //启动横幅动画
                startAcceptBannersAnimotion(giftEntity, account);
                //启动头像动画
                startAcceptHeadAnimotion(giftEntity);
            }
        });
        //发送礼物效果展示
        viewModel.uc.sendUserGiftAnim.observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> stringObjectMap) {
                int account = (int) stringObjectMap.get("account");
                GiftBagBean.giftEntity giftEntity = (GiftBagBean.giftEntity) stringObjectMap.get("giftEntity");
                //启动SVG动画
                startSendSvgAnimotion(giftEntity);
                //启动横幅动画
                startSendBannersAnimotion(giftEntity, account);
                //启动头像动画
                startSendHeadAnimotion(giftEntity);
            }
        });
        //接听成功
        viewModel.uc.callAudioStart.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                //开始记时
                if (!isRestart){
                    TimeCallMessage();
                    setTimerForCallinfo();
                }
            }
        });
        //钻石不足充值弹窗
        viewModel.uc.sendUserGiftError.observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean isGiftSend) {
                toRecharge();
//                GameCoinExchargeSheetView coinRechargeSheetView = new GameCoinExchargeSheetView(AudioCallChatingActivity.this);
//                coinRechargeSheetView.setCallMedia(true);
//                coinRechargeSheetView.setMaleBalance(viewModel.maleBalanceMoney);
//                coinRechargeSheetView.show();
//                coinRechargeSheetView.setCoinRechargeSheetViewListener(new GameCoinExchargeSheetView.CoinRechargeSheetViewListener() {
//                    @Override
//                    public void onPaySuccess(GameCoinExchargeSheetView sheetView, CoinExchangePriceInfo sel_goodsEntity) {
//                        sheetView.dismiss();
//                        viewModel.getCallingStatus(roomId);
//                    }
//
//                    @Override
//                    public void onPayFailed(GameCoinExchargeSheetView sheetView, String msg) {
//                        sheetView.dismiss();
//                    }
//                });
            }
        });
        //发送礼物弹窗
        viewModel.uc.callGiftBagAlert.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                if (viewModel.unitPriceList == null || viewModel.maleBalanceMoney == 0){
                    return;
                }
                GiftBagDialog giftBagDialog = new GiftBagDialog(mContext, true, viewModel.maleBalanceMoney, viewModel.unitPriceList.size() > 1 ? 3 : 0);
                giftBagDialog.setGiftOnClickListener(new GiftBagDialog.GiftOnClickListener() {
                    @Override
                    public void sendGiftClick(Dialog dialog, int number, GiftBagBean.giftEntity giftEntity) {
                        dialog.dismiss();
                        CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_send_gift);
                        viewModel.sendUserGift(dialog, giftEntity, viewModel.leftUserInfoField.get().getId(), number);
                    }

                    @Override
                    public void rechargeStored(Dialog dialog) {
                        dialog.dismiss();
                        CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_gift_topup);
                        viewModel.uc.sendUserGiftError.postValue(true);
                    }
                });
                giftBagDialog.show();
            }
        });
    }

    /**
     * 去充值
     */
    private void toRecharge() {
        Intent intent = new Intent(this, DialogDiamondRechargeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pop_enter_anim, 0);
    }

    /**
     * 隐藏水晶兑换规则弹框
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
                    viewModel.getCallingStatus(roomId);
                }catch (Exception ignored){

                }
            }
        }, 1000,10000);
    }

    private void startAcceptBannersAnimotion(GiftEntity giftEntity, int account) {
        if (account > 1) {
            View streamerView = View.inflate(mContext, R.layout.call_user_streamer_item, null);
            //用户头像
            CircleImageView userImg = streamerView.findViewById(R.id.user_img);
            //礼物图片
            ImageView giftImg = streamerView.findViewById(R.id.gift_img);
            //礼物图片数量
            ImageView giftNumImg = streamerView.findViewById(R.id.gift_num_img);
            //文案
            TextView tipText = streamerView.findViewById(R.id.tip_text);
            String sexText = viewModel.leftUserInfoField.get().getNickname();
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
                    .load(StringUtil.getFullImageUrl(viewModel.rightUserInfoField.get().getAvatar()))
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
                    postRemoveView(binding.mainView, streamerView);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = dip2px(14);
            layoutParams.topMargin = dip2px(249);
            streamerView.setLayoutParams(layoutParams);
            binding.mainView.addView(streamerView);
            streamerView.startAnimation(animation);
        }
    }



    private void startAcceptHeadAnimotion(GiftEntity giftEntity) {
        ImageView giftImageTrans = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(90), dip2px(90));
        layoutParams.gravity = Gravity.START;
        layoutParams.leftMargin = dip2px(46);
        layoutParams.topMargin = dip2px(135);
        giftImageTrans.setLayoutParams(layoutParams);
        giftImageTrans.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_call_audio_left_to_right_tip);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postRemoveView(binding.mainView, giftImageTrans);
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
        binding.mainView.addView(giftImageTrans);
        giftImageTrans.startAnimation(animation);
    }

    private void startAcceptSVGAnimotion(GiftEntity giftEntity) {
        if (!StringUtils.isEmpty(giftEntity.getSvgaPath())) {
            SVGAParser svgaParser = SVGAParser.Companion.shareParser();
            try {
                svgaParser.decodeFromURL(new URL(StringUtil.getFullAudioUrl(giftEntity.getSvgaPath())), new SVGAParser.ParseCompletion() {
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
                                //播放完成
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

    private void startSendBannersAnimotion(GiftBagBean.giftEntity giftEntity, int account) {
        if (account > 1) {
            View streamerView = View.inflate(mContext, R.layout.call_user_streamer_item, null);
            //用户头像
            CircleImageView userImg = streamerView.findViewById(R.id.user_img);
            //礼物图片
            ImageView giftImg = streamerView.findViewById(R.id.gift_img);
            //礼物图片数量
            ImageView giftNumImg = streamerView.findViewById(R.id.gift_num_img);
            //文案
            TextView tipText = streamerView.findViewById(R.id.tip_text);
            String sexText = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt17);
            String messageText = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt1);
            String itemTextMessage = sexText + messageText + "\n" + viewModel.leftUserInfoField.get().getNickname();
            SpannableString stringBuilder = new SpannableString(itemTextMessage);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), 3, itemTextMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tipText.setText(stringBuilder);
            Glide.with(mContext)
                    .asBitmap()
                    .load(StringUtil.getFullImageUrl(viewModel.rightUserInfoField.get().getAvatar()))
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
                    postRemoveView(binding.mainView, streamerView);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = dip2px(14);
            layoutParams.topMargin = dip2px(249);
            streamerView.setLayoutParams(layoutParams);
            binding.mainView.addView(streamerView);
            streamerView.startAnimation(animation);
        }
    }

    private void startSendHeadAnimotion(GiftBagBean.giftEntity giftEntity) {
        ImageView giftImageTrans = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(90), dip2px(90));
        layoutParams.gravity = Gravity.END;
        layoutParams.rightMargin = dip2px(42);
        layoutParams.topMargin = dip2px(135);
        giftImageTrans.setLayoutParams(layoutParams);
        giftImageTrans.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_call_audio_right_to_left_tip);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postRemoveView(binding.mainView, giftImageTrans);

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

    private void startSendSvgAnimotion(GiftBagBean.giftEntity giftEntity) {
        if (!StringUtils.isEmpty(giftEntity.getLink())) {
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
    }

    //异步移除view
    private void postRemoveView(ViewGroup viewGroup, View IiageTrans) {
        viewGroup.post(new Runnable() {
            public void run() {
                // it works without the runOnUiThread, but all UI updates must
                // be done on the UI thread
                AudioCallChatingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        viewGroup.removeView(IiageTrans);
                    }
                });
            }
        });
    }

    /**
     * 两分钟发送一次在线信息
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
                if (mTimeCount % 10 == 0){
                    viewModel.getRoomStatus(roomId);
                }
                if (!viewModel.sayHiEntityHidden.get() && mTimeCount % 10 == 0) {
                    //没10秒更新一次破冰文案
                    viewModel.getSayHiList();
                }
                if (viewModel.callInfoLoaded && viewModel.isShowTipMoney){
                    //判断是否为付费方
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
     * 展示右下角收益提示
     */
    private void setProfitTips() {
        if (!viewModel.isShowCountdown.get() && viewModel.payeeProfits > 0) {//对方余额不足没有展示
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
     * 余额不足推送与显示
     * @param isShow
     */
    private void moneyNoWorthSwich(boolean isShow) {
        viewModel.flagMoneyNotWorth = isShow;
        viewModel.maleTextLayoutSHow.set(isShow);
        //通知女生男生这边余额不足
            if (isShow){
                viewModel.getTips(viewModel.leftUserInfoField.get().getId(),2,"1");
            }else {
                viewModel.getTips(viewModel.leftUserInfoField.get().getId(),2,"0");
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CCApplication.isCalling = false;
        //取消窥屏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //取消常亮
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

    //返回按键
    @Override
    public void onBackPressed() {
        onBackViewCLick();
    }

    //返回按钮调用代码
    public void onBackViewCLick() {
        if (viewModel.collected == null){
            viewModel.hangup();
            return;
        }
        if (viewModel.isMale) {
            if (viewModel.collected == 1) {//已追踪
                String title = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt9);
                MessageDetailDialog.callAudioHint2(mContext, title, null, new MessageDetailDialog.AudioCallHintOnClickListener() {
                    @Override
                    public void check1OnClick() {
                        CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_close_hangup_M);
                        viewModel.hangup();
                    }

                    @Override
                    public void check2OnClick() {
                        CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_close_goon_M);
                    }
                }).show();
            } else {//没有追踪
                MessageDetailDialog.callAudioHint(mContext, new MessageDetailDialog.AudioCallHintOnClickListener() {
                    @Override
                    public void check1OnClick() {
                        CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_close_follow_M);
                        viewModel.addLike(true);
                    }

                    @Override
                    public void check2OnClick() {
                        CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_close_hangup_M);
                        viewModel.hangup();
                    }
                }).show();
            }
        } else {
            if (!ConfigManager.getInstance().getTipMoneyShowFlag()) {
                viewModel.hangup();
                return;
            }
            String title = mContext.getString(R.string.playcc_call_message_deatail_girl_txt13);
            String content = mContext.getString(R.string.playcc_call_message_deatail_girl_txt12);
            MessageDetailDialog.callAudioHint2(mContext, title, content, new MessageDetailDialog.AudioCallHintOnClickListener() {
                @Override
                public void check1OnClick() {
                    CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_close_hangup_F);
                    viewModel.hangup();
                }

                @Override
                public void check2OnClick() {
                    CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_close_goon_F);
                }
            }).show();
        }
    }

    @Override
    public void finishView() {
        //2秒最多发一次
        if (!FastClickUtil.isFastCallFun("AudioCallChatingActivity")) {
            RxBus.getDefault().post(new CallChatingHangupEvent());
        }
//        Utils.runOnUiThread(this::finish);
        runOnUiThread(() -> {
            finish();
            overridePendingTransition(0, R.anim.anim_nomal);
        });
        RxBus.getDefault().post(new ShowFloatWindowBean(false));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}