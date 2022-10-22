package com.dl.playfun.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.aliyun.svideo.common.utils.ScreenUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.app.Injection;
import com.dl.playfun.app.config.TbarCenterImgConfig;
import com.dl.playfun.databinding.FragmentMainBinding;
import com.dl.playfun.entity.MqBroadcastGiftEntity;
import com.dl.playfun.entity.MqBroadcastGiftUserEntity;
import com.dl.playfun.entity.VersionEntity;
import com.dl.playfun.event.DailyAccostEvent;
import com.dl.playfun.event.MainTabEvent;
import com.dl.playfun.event.TaskListEvent;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.base.BaseFragment;
import com.dl.playfun.ui.dialog.LockDialog;
import com.dl.playfun.ui.home.HomeMainFragment;
import com.dl.playfun.ui.message.MessageMainFragment;
import com.dl.playfun.ui.mine.MineFragment;
import com.dl.playfun.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.dl.playfun.ui.radio.radiohome.RadioFragment;
import com.dl.playfun.ui.task.TaskCenterFragment;
import com.dl.playfun.ui.userdetail.detail.UserDetailFragment;
import com.dl.playfun.ui.vest.first.VestFirstFragment;
import com.dl.playfun.ui.vest.second.VestSecondFragment;
import com.dl.playfun.utils.ImmersionBarUtils;
import com.dl.playfun.utils.StringUtil;
import com.dl.playfun.widget.coinrechargesheet.CoinRechargeSheetView;
import com.dl.playfun.widget.dialog.MVDialog;
import com.dl.playfun.widget.dialog.TraceDialog;
import com.dl.playfun.widget.dialog.WebViewDialog;
import com.dl.playfun.widget.dialog.version.view.UpdateDialogView;
import com.dl.playfun.widget.pageview.FragmentAdapter;
import com.tencent.qcloud.tuicore.custom.entity.VideoEvaluationEntity;
import com.tencent.qcloud.tuicore.custom.entity.VideoPushEntity;
import com.tencent.qcloud.tuicore.util.BackgroundTasks;
import com.tencent.qcloud.tuikit.tuiconversation.ui.view.ConversationCommonHolder;

import me.goldze.mvvmhabit.bus.RxBus;

/**
 * @author wulei
 */
public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    public static final int FIFTH = 4;

    private final BaseFragment[] mFragments = new BaseFragment[5];
    private TextView tvBadgeNum;

    private ImageView selTabImgLayout;
    private ViewPager2 mainViewPager;

    private static final long WAIT_TIME = 3000L;

    private long TOUCH_TIME = 0;

    private Vibrator mVibrator;
    private boolean isShowing;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImmersionBarUtils.setupStatusBar(this, true, true);
        return R.layout.fragment_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MainViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //异步移除view
    private void postRemoveView(ViewGroup viewGroup, View IiageTrans) {
        viewGroup.post(new Runnable() {
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        viewGroup.removeView(IiageTrans);
                    }
                });
            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.versionOnClickCommand();
        viewModel.uc.videoEvaluation.observe(this,evaluationEntity->{
            setVideoEvaluationDialog(evaluationEntity);
        });
        viewModel.uc.videoPush.observe(this,videoPushEntity->{
            setVideoPushDialog(videoPushEntity);
        });

        AppContext.instance().logEvent(AppsFlyerEvent.main_open);
        //未付费弹窗
        viewModel.uc.notPaidDialog.observe(this,s -> {
            String url = Injection.provideDemoRepository().readApiConfigManagerEntity().getPlayFunWebUrl();
            if (s.equals("2")) {
                url = url +"/recharge_vip/recharge_vip.html";
            } else {
                url = url +"/recharge_zuan/recharge_zuan.html";
            }
            new WebViewDialog(getContext(), mActivity, url, new WebViewDialog.ConfirmOnclick() {
                @Override
                public void webToVipRechargeVC(Dialog dialog) {
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                    viewModel.start(VipSubscribeFragment.class.getCanonicalName());
                }

                @Override
                public void vipRechargeDiamondSuccess(Dialog dialog, Integer coinValue) {
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                }

                @Override
                public void moreRechargeDiamond(Dialog dialog) {
                    dialog.dismiss();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showRecharge();
                        }
                    });
                }

                @Override
                public void cancel() {
                }
            }).noticeDialog().show();
        });
        //每日奖励
        viewModel.uc.showDayRewardDialog.observe(this,s -> {
            showRewardDialog();
        });
        //注册奖励
        viewModel.uc.showRegisterRewardDialog.observe(this,s -> {
            showRegisterRewardDialog();
        });
        //主页公屏礼物
        viewModel.uc.giftBanner.observe(this,mqttMessageEntity -> {
            MqBroadcastGiftUserEntity leftUser = mqttMessageEntity.getFromUser();
            MqBroadcastGiftUserEntity rightUser = mqttMessageEntity.getToUser();
            View streamerView = View.inflate(MainFragment.this.getContext(), R.layout.fragment_main_gift_banner, null);
            setGiftViewBanner(mqttMessageEntity, leftUser, rightUser, streamerView);
            Animation animation = AnimationUtils
                    .loadAnimation(MainFragment.this.getContext(), R.anim.anim_main_gift_banner_right_to_left);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    postRemoveView(binding.container, streamerView);
                    if (viewModel.publicScreenBannerGiftEntity.size() > 0)
                        viewModel.publicScreenBannerGiftEntity.remove(0);
                    viewModel.playing = false;
                    viewModel.playBannerGift();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            int deviceStatusHeight = ScreenUtils.getDeviceStatusHeight(mActivity);
            layoutParams.setMargins(0,deviceStatusHeight+5,0,0);
            streamerView.setLayoutParams(layoutParams);
            binding.container.addView(streamerView);
            animation.setInterpolator(new AccelerateInterpolator());
            streamerView.startAnimation(animation);
            viewModel.playing = true;
        });

        //气泡提示
        viewModel.uc.bubbleTopShow.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean tipShow) {
                if (!ConfigManager.getInstance().getTipMoneyShowFlag()) {
                    binding.bubbleTip.setVisibility(View.GONE);
                    return;
                }
                if (tipShow && !ConfigManager.getInstance().isMale()) {
                    binding.bubbleTip.setVisibility(View.VISIBLE);
                } else {
                    binding.bubbleTip.setVisibility(View.GONE);
                }
            }
        });

        viewModel.uc.restartActivity.observe(this, intent -> {
            startActivity(intent);
            mActivity.overridePendingTransition(R.anim.anim_zoom_in, R.anim.anim_stay);
        });
        viewModel.uc.mainTab.observe(this, new Observer<MainTabEvent>() {
            @Override
            public void onChanged(MainTabEvent mainTabEvent) {
                if (mainTabEvent != null) {
                    switch (mainTabEvent.getTabName()) {
                        case "home":
                            setSelectedItemId(binding.navigationHomeImg);//tbar切换到首頁
                            break;
                        case "plaza":
                            setSelectedItemId(binding.navigationRadioImg);//tbar切换到廣場
                            break;
                        case "message":
                            setSelectedItemId(binding.navigationMessageImg);//tbar切换到訊息
                            break;
                    }
                }
            }
        });
        //版本更新提示
        viewModel.uc.versionEntitySingl.observe(this, new Observer<VersionEntity>() {
            @Override
            public void onChanged(VersionEntity versionEntity) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (versionEntity.getVersion_code().intValue() <= AppConfig.VERSION_CODE.intValue()) {
                            dialogCallback();

                        } else {
                            boolean isUpdate = versionEntity.getIs_update().intValue() == 1;
                            UpdateDialogView.getInstance(mActivity)
                                    .getUpdateDialogView(versionEntity.getVersion_name(),
                                            versionEntity.getContent(),
                                            versionEntity.getUrl(), isUpdate, "playchat", versionEntity.getLinkUrl())
                                    .setConfirmOnlick(new UpdateDialogView.CancelOnclick() {
                                        @Override
                                        public void cancel() {
                                            dialogCallback();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
            }
        });
        viewModel.uc.showFaceRecognitionDialog.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                showFaceRecognitionDialog();
            }
        });
        viewModel.uc.lockDialog.observe(this, aVoid -> {
            LockDialog dialog = new LockDialog();
            dialog.setPassword(viewModel.lockPassword.get());
            dialog.setLockDialogListener(new LockDialog.LockDialogListener() {
                @Override
                public void onLogoutClick(LockDialog lockDialog) {
                    MVDialog.getInstance(MainFragment.this.getContext())
                            .setContent(getString(R.string.playfun_conflirm_log_out))
                            .setConfirmOnlick(dialog -> {
                                lockDialog.dismiss();
                                dialog.dismiss();
                                viewModel.logout();
                            })
                            .chooseType(MVDialog.TypeEnum.CENTERWARNED)
                            .show();
                }

                @Override
                public void onVerifySuccess(LockDialog dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show(getChildFragmentManager(), LockDialog.class.getCanonicalName());
        });
        viewModel.uc.startFace.observe(this, verifyToken -> {

        });

        viewModel.uc.mainTab.observe(this, new Observer<MainTabEvent>() {
            @Override
            public void onChanged(MainTabEvent mainTabEvent) {
                if (mainTabEvent != null) {
                    switch (mainTabEvent.getTabName()) {
                        case "home":
                            setSelectedItemId(binding.navigationHomeImg);//tbar切换到首頁
                            break;
                        case "redio":
                            setSelectedItemId(binding.navigationRadioImg);//tbar切换到廣場
                            break;
                        case "message":
                            setSelectedItemId(binding.navigationMessageImg);//tbar切换到訊息
                            break;
                        case "mine":
                            setSelectedItemId(binding.navigationMineImg);//tbar切换到訊息
                            break;
                    }
                }
            }
        });
        viewModel.uc.taskCenterclickTab.observe(this, taskMainTabEvent -> {
            if (taskMainTabEvent != null) {
                if (taskMainTabEvent.isTbarClicked()) {//tbar切换到活动中心
                    setSelectedItemId(binding.navigationRankImg);
                }
            }
        });
        viewModel.uc.allMessageCountChange.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                try {
                    if (count == 0) {
                        tvBadgeNum.setVisibility(View.GONE);
                    } else {
                        tvBadgeNum.setVisibility(View.VISIBLE);
                        if (count > 98) {
                            tvBadgeNum.setText("99+");
                        } else {
                            tvBadgeNum.setText(String.valueOf(count));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //视讯评价
    private void setVideoEvaluationDialog(VideoEvaluationEntity evaluationEntity) {
        if (evaluationEntity == null)return;
        TraceDialog.getInstance(mActivity)
                .setConfirmOnlick(dialog -> {
                    viewModel.videoFeedback(evaluationEntity.getVideoCallPushLogId(),2);
                })
                .setConfirmTwoOnlick(dialog -> {
                    viewModel.videoFeedback(evaluationEntity.getVideoCallPushLogId(),1);
                })
                .getVideoEvaluationDialog(evaluationEntity.getAvatar())
                .show();
    }

    //视讯推送
    private void setVideoPushDialog(VideoPushEntity videoPushEntity) {
        if (videoPushEntity == null || videoPushEntity.getUserProfile() == null || isShowing)return;
        isShowing = true;
        if (videoPushEntity.getIsShake() == 1){
            if (mVibrator == null)
                mVibrator = (Vibrator) mActivity.getApplication().getSystemService(Service.VIBRATOR_SERVICE);

            mVibrator.vibrate(new long[]{500, 300, 500, 300}, 0);
        }

        TraceDialog.getInstance(mActivity)
                .setConfirmOnlick(dialog -> {
                    isShowing = false;
                    if (mVibrator != null)
                        mVibrator.cancel();
                })
                .setConfirmTwoOnlick(dialog -> {
                    viewModel.getCallingInvitedInfo(ConfigManager.getInstance().getUserImID(),
                            videoPushEntity.getUserProfile().getImId(),
                            videoPushEntity.getVideoCallPushLogId());
                })
                .getVideoPushDialog(videoPushEntity.getUserProfile().getAvatar(),
                        videoPushEntity.getUserProfile().getSex() == 0,
                        videoPushEntity.getUserProfile().getIsVip() == 1,
                        videoPushEntity.getUserProfile().getAge(),
                        videoPushEntity.getSeconds(),
                        videoPushEntity.getUserProfile().getNickname())
                .show();
    }

    private void showRecharge() {
        CoinRechargeSheetView coinRechargeSheetView = new CoinRechargeSheetView(mActivity);
        coinRechargeSheetView.show();
    }

    private void dialogCallback() {
        //注册奖励
        if (AppConfig.isRegister) {
            AppConfig.isRegisterAccost = true;
            viewModel.getRegisterReward();
        } else {
            if (!viewModel.isShowedReward) {
                viewModel.getDayReward();
            } else {
                RxBus.getDefault().post(new DailyAccostEvent());
            }
        }
    }


    /**
     * 显示奖励dialog
     */
    private void showRewardDialog() {
        if (viewModel.giveCoin <= 0 && viewModel.videoCard <= 0){
            RxBus.getDefault().post(new DailyAccostEvent());
            return;
        }
        TraceDialog.getInstance(mActivity)
                .setConfirmOnlick(dialog -> {
                    RxBus.getDefault().post(new DailyAccostEvent());
                    dialog.dismiss();
                })
                .dayRewardDialog(true,
                        viewModel.nextGiveCoin,
                        viewModel.nextVideoCard,
                        viewModel.giveCoin,
                        viewModel.videoCard)
                .show();
    }

    private void showRegisterRewardDialog() {
        if (viewModel.chatCardNum <= 0 && viewModel.assostCardNum <= 0){
            RxBus.getDefault().post(new DailyAccostEvent());
            return;
        }
        TraceDialog.getInstance(mActivity)
                .setTitle(getString(R.string.playfun_reward_title))
                .setFirstRewardId(R.drawable.icon_say_hi_card)
                .setSecondRewardId(R.drawable.icon_chat_card)
                .setConfirmOnlick(dialog -> {
                    RxBus.getDefault().post(new DailyAccostEvent());
                    AppConfig.isRegister = false;
                    dialog.dismiss();
                })
                .registerRewardDialog(true, viewModel.assostCardNum, viewModel.chatCardNum)
                .show();
    }

    private void setGiftViewBanner(MqBroadcastGiftEntity mqttMessageEntity, MqBroadcastGiftUserEntity leftUser, MqBroadcastGiftUserEntity rightUser, View streamerView) {
        ImageView leftUserImg = streamerView.findViewById(R.id.left_user_img);
        TextView leftUserName = streamerView.findViewById(R.id.left_user_name);
        leftUserName.setText(leftUser.getNickname());
        ImageView leftUserIcon = streamerView.findViewById(R.id.left_user_icon);
        ImageView ivGift = streamerView.findViewById(R.id.iv_gift);
        broadcastGiftImg(leftUser,leftUserIcon);
        Glide.with(getContext())
                .asBitmap()
                .load(StringUtil.getFullImageUrl(mqttMessageEntity.getImagePath()))
                .error(R.drawable.radio_program_list_content)
                .placeholder(R.drawable.radio_program_list_content)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivGift);
        Glide.with(getContext())
                .asBitmap()
                .load(StringUtil.getFullImageUrl(leftUser.getAvatar()))
                .error(R.drawable.radio_program_list_content)
                .placeholder(R.drawable.radio_program_list_content)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(leftUserImg);
        leftUserImg.setOnClickListener(v -> {
            Bundle bundle = UserDetailFragment.getStartBundle(leftUser.getId());
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        });
        ImageView rightUserImg = streamerView.findViewById(R.id.right_user_img);
        TextView rightUserName = streamerView.findViewById(R.id.right_user_name);
        ImageView rightUserIcon = streamerView.findViewById(R.id.right_user_icon);
        rightUserName.setText(rightUser.getNickname());
        Glide.with(getContext())
                .asBitmap()
                .load(StringUtil.getFullImageUrl(rightUser.getAvatar()))
                .error(R.drawable.radio_program_list_content)
                .placeholder(R.drawable.radio_program_list_content)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(rightUserImg);
        rightUserImg.setOnClickListener(v -> {
            if (rightUser.getImId().equals(ConfigManager.getInstance().getUserImID())){
                return;
            }
            Bundle bundle = UserDetailFragment.getStartBundle(rightUser.getId());
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        });
        broadcastGiftImg(rightUser,rightUserIcon);

        TextView giftTitle = streamerView.findViewById(R.id.gift_title);

        String tips = mActivity.getString(R.string.playfun_send_tips);
        String detail = tips + "[" + mqttMessageEntity.getGiftName() + "]";
        SpannableString stringBuilder = new SpannableString(detail);
        ForegroundColorSpan blueSpanWhite = new ForegroundColorSpan(ColorUtils.getColor(R.color.white));
        ForegroundColorSpan blueSpanYellow = new ForegroundColorSpan(ColorUtils.getColor(R.color.yellow_617));
        stringBuilder.setSpan(blueSpanWhite, 0, detail.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(blueSpanYellow, tips.length(), detail.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        giftTitle.setText(stringBuilder);
        ImageView giftNumImg = streamerView.findViewById(R.id.gift_count);
        int account = mqttMessageEntity.getAmount();
        setGiftNumImg(giftNumImg, account);
    }

    private void setGiftNumImg(ImageView giftNumImg, int account) {
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
    }

    private void broadcastGiftImg(MqBroadcastGiftUserEntity giftUserEntity,ImageView userImg){
        if(giftUserEntity.getSex()!=null && giftUserEntity.getSex()==1){
            if(giftUserEntity.getCertification()!=null && giftUserEntity.getCertification()==1){
                userImg.setImageResource(R.drawable.ic_real_man);
                userImg.setVisibility(View.VISIBLE);
            }
            if(giftUserEntity.getIsVip()!=null && giftUserEntity.getIsVip()==1){
                userImg.setImageResource(R.drawable.ic_vip);
                userImg.setVisibility(View.VISIBLE);
            }
        }else{
            if(giftUserEntity.getCertification()!=null && giftUserEntity.getCertification()==1){
                userImg.setImageResource(R.drawable.ic_real_man);
                userImg.setVisibility(View.VISIBLE);
            }
            if(giftUserEntity.getIsVip()!=null && giftUserEntity.getIsVip()==1){
                userImg.setImageResource(R.drawable.ic_goddess);
                userImg.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        int ImgSrcPath = TbarCenterImgConfig.getInstance().getImgSrcPath();
        if(ImgSrcPath!=-1){
            binding.navigationRankImg.setImageResource(ImgSrcPath);
        }
        initView();
        boolean sexFlag = ConfigManager.getInstance().isMale();
        ConversationCommonHolder.sexMale = sexFlag;
        binding.navigationMineImgLottie.setAnimation(sexFlag ? R.raw.lottie_navigation_mine_male : R.raw.lottie_navigation_mine_fmale);
        binding.navigationMineImg.setImageResource(sexFlag ? R.drawable.tab_mine_male_image : R.drawable.tab_mine_female_normal);


    }

    /**
     * @return void
     * @Desc TODO(页面再次进入)
     * @author 彭石林
     * @parame [hidden]
     * @Date 2021/8/4
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppConfig.isMainPage = !hidden;
        if (!hidden) {
            if (System.currentTimeMillis() - TOUCH_TIME > WAIT_TIME) {
                //刷新任何列表数据
                RxBus.getDefault().post(new TaskListEvent());
                TOUCH_TIME = System.currentTimeMillis();
            }
            if (!StringUtils.isEmpty(AppConfig.homePageName)) {
                switch (AppConfig.homePageName) {
                    case "home":
                        AppConfig.homePageName = null;
                        setSelectedItemId(binding.navigationHomeImg);
                        break;
                    case "broadcast":
                        AppConfig.homePageName = null;
                        setSelectedItemId(binding.navigationRadioImg);
                        break;
                    case "navigation_rank":
                        AppConfig.homePageName = null;
                        setSelectedItemId(binding.navigationRankImg);
                        break;
                    case "message":
                        AppConfig.homePageName = null;
                        setSelectedItemId(binding.navigationMessageImg);
                        break;
                    case "user":
                        AppConfig.homePageName = null;
                        setSelectedItemId(binding.navigationMineImg);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void showFaceRecognitionDialog() {
        Animation animation = AnimationUtils.loadAnimation(MainFragment.this.getContext(), R.anim.pop_enter_anim);
        animation.setFillAfter(true);
    }

    private void initView() {
//        mFragments[FIRST] = new HomeMainFragment();
        mFragments[FIRST] = new VestFirstFragment();
//        mFragments[SECOND] = new RadioFragment();
        mFragments[SECOND] = new VestSecondFragment();
        mFragments[THIRD] = new TaskCenterFragment();
        mFragments[FOURTH] = new MessageMainFragment();
        mFragments[FIFTH] = new MineFragment();
        tvBadgeNum = binding.tvMsgCount;
        //添加到Tab上

        //首页点击
        binding.navigationHome.setOnClickListener(v -> setSelectedItemId(binding.navigationHomeImg));
        binding.navigationRadio.setOnClickListener(v -> setSelectedItemId(binding.navigationRadioImg));
        binding.navigationRank.setOnClickListener(v -> setSelectedItemId(binding.navigationRankImg));
        binding.navigationMessage.setOnClickListener(v -> setSelectedItemId(binding.navigationMessageImg));
        binding.navigationMine.setOnClickListener(v -> setSelectedItemId(binding.navigationMineImg));
        binding.bubbleTip.setOnClickListener(v -> setSelectedItemId(binding.navigationMessageImg));
        mainViewPager = binding.viewPager;
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this);
        fragmentAdapter.setFragmentList(mFragments);
        // 关闭左右滑动切换页面
        mainViewPager.setUserInputEnabled(false);
        // 设置缓存数量 避免销毁重建
        mainViewPager.setOffscreenPageLimit(5);
        //取消保存页面--未知BUG
        mainViewPager.setSaveEnabled(false);
        mainViewPager.setAdapter(fragmentAdapter);
        mainViewPager.setCurrentItem(0, false);

        if (selTabImgLayout == null) {
            selTabImgLayout = binding.navigationHomeImg;
        } else {
            // 初始化时，强制切换tab到上一次的位置
            ImageView tmp = selTabImgLayout;
            setSelectedItemId(tmp);
            selTabImgLayout = tmp;
        }
        lottieAddAnimatorListener();
    }

    private void setSelectedItemId(ImageView view) {
        if(selTabImgLayout==null){
            selTabImgLayout = view;
        }
        if (selTabImgLayout != null && selTabImgLayout.getId() != view.getId()) {
            resetMenuState(selTabImgLayout.getId());
            int id = view.getId();
            if (id == R.id.navigation_home_img) {
                mainViewPager.setCurrentItem(0, false);
                //binding.navigationHomeImg.setVisibility(View.VISIBLE);
                binding.navigationHomeImg.setVisibility(View.INVISIBLE);
                binding.navigationHomeImgLottie.setVisibility(View.VISIBLE);
                binding.navigationHomeImgLottie.playAnimation();
                binding.navigationHomeImg.setImageResource(R.drawable.tab_home_checked);
            } else if (id == R.id.navigation_radio_img) {
                mainViewPager.setCurrentItem(1, false);
                binding.navigationRadioImg.setVisibility(View.INVISIBLE);
                binding.navigationRadioImgLottie.setVisibility(View.VISIBLE);
                binding.navigationRadioImgLottie.playAnimation();
                binding.navigationRadioImg.setImageResource(R.drawable.tab_radio_checked);
            } else if (id == R.id.navigation_rank_img) {
                mainViewPager.setCurrentItem(2, false);
            } else if (id == R.id.navigation_message_img) {
                mainViewPager.setCurrentItem(3, false);
                binding.navigationMessageImg.setVisibility(View.INVISIBLE);
                binding.navigationMessageImgLottie.setVisibility(View.VISIBLE);
                binding.navigationMessageImgLottie.playAnimation();
                binding.navigationMessageImg.setImageResource(R.drawable.tab_message_checked);
            } else if (id == R.id.navigation_mine_img) {
                mainViewPager.setCurrentItem(4, false);
                binding.navigationMineImg.setVisibility(View.INVISIBLE);
                binding.navigationMineImgLottie.setVisibility(View.VISIBLE);
                binding.navigationMineImgLottie.playAnimation();
                if (ConfigManager.getInstance().isMale()) {
                    binding.navigationMineImg.setImageResource(R.drawable.tab_mine_male_checked);
                } else {
                    binding.navigationMineImg.setImageResource(R.drawable.tab_mine_female_checked);
                }
            }
            selTabImgLayout = view;
        }
    }

    //初始化按钮状态
    private void resetMenuState(int id) {
        if (id == R.id.navigation_home_img) { //首页
            binding.navigationHomeImg.setImageResource(R.drawable.tab_home_normal);
        } else if (id == R.id.navigation_radio_img) { //广场
            binding.navigationRadioImg.setImageResource(R.drawable.tab_radio_normal);
        } else if (id == R.id.navigation_rank_img) { //任务中心
            int ImgSrcPath = TbarCenterImgConfig.getInstance().getImgSrcPath();
            if(ImgSrcPath!=-1){
                binding.navigationRankImg.setImageResource(ImgSrcPath);
            }else{
                binding.navigationRankImg.setImageResource(R.drawable.toolbar_icon_task_checked);
            }
            binding.navigationRankText.setTextColor(getResources().getColor(R.color.navigation_checkno));
        } else if (id == R.id.navigation_message_img) { //讯息页面
            binding.navigationMessageImg.setImageResource(R.drawable.tab_message_normal);
        } else if (id == R.id.navigation_mine_img) { //我的页面
            if (ConfigManager.getInstance().isMale()) {
                binding.navigationMineImg.setImageResource(R.drawable.tab_mine_male_image);
            } else {
                binding.navigationMineImg.setImageResource(R.drawable.tab_mine_female_normal);
            }
        }
    }
    //添加lottie监听
    private void lottieAddAnimatorListener(){
        binding.navigationHomeImgLottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.navigationHomeImgLottie.setVisibility(View.GONE);
                binding.navigationHomeImg.setVisibility(View.VISIBLE);
            }
        });
        binding.navigationRadioImgLottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.navigationRadioImgLottie.setVisibility(View.GONE);
                binding.navigationRadioImg.setVisibility(View.VISIBLE);
            }
        });
        binding.navigationMessageImgLottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.navigationMessageImgLottie.setVisibility(View.GONE);
                binding.navigationMessageImg.setVisibility(View.VISIBLE);
            }
        });
        binding.navigationMineImgLottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.navigationMineImgLottie.setVisibility(View.GONE);
                binding.navigationMineImg.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected boolean isUmengReportPage() {
        return false;
    }
}
