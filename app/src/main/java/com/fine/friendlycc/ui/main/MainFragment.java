package com.fine.friendlycc.ui.main;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
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
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.app.config.TbarCenterImgConfig;
import com.fine.friendlycc.databinding.FragmentMainBinding;
import com.fine.friendlycc.bean.MqBroadcastGiftBean;
import com.fine.friendlycc.bean.MqBroadcastGiftUserBean;
import com.fine.friendlycc.bean.VersionBean;
import com.fine.friendlycc.event.DailyAccostEvent;
import com.fine.friendlycc.event.GenderToggleEvent;
import com.fine.friendlycc.event.MainTabEvent;
import com.fine.friendlycc.event.TaskListEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.ui.dialog.LockDialog;
import com.fine.friendlycc.ui.home.HomeMainFragment;
import com.fine.friendlycc.ui.message.MessageMainFragment;
import com.fine.friendlycc.ui.mine.MineFragment;
import com.fine.friendlycc.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.ui.radio.radiohome.RadioFragment;
import com.fine.friendlycc.ui.task.TaskCenterFragment;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.fine.friendlycc.widget.dialog.WebViewDialog;
import com.fine.friendlycc.widget.dialog.version.view.UpdateDialogView;
import com.fine.friendlycc.widget.pageview.FragmentAdapter;
import com.tencent.qcloud.tuicore.custom.entity.VideoEvaluationEntity;
import com.tencent.qcloud.tuicore.custom.entity.VideoPushEntity;
import com.tencent.qcloud.tuicore.util.BackgroundTasks;

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

    //????????????view
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

        CCApplication.instance().logEvent(AppsFlyerEvent.main_open);
        //???????????????
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
        //????????????
        viewModel.uc.showDayRewardDialog.observe(this,s -> {
            showRewardDialog();
        });
        //????????????
        viewModel.uc.showRegisterRewardDialog.observe(this,s -> {
            showRegisterRewardDialog();
        });
        //??????????????????
        viewModel.uc.giftBanner.observe(this,mqttMessageEntity -> {
            MqBroadcastGiftUserBean leftUser = mqttMessageEntity.getFromUser();
            MqBroadcastGiftUserBean rightUser = mqttMessageEntity.getToUser();
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

        //????????????
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
                            setSelectedItemId(binding.navigationHomeImg);//tbar???????????????
                            break;
                        case "plaza":
                            setSelectedItemId(binding.navigationRadioImg);//tbar???????????????
                            break;
                        case "message":
                            setSelectedItemId(binding.navigationMessageImg);//tbar???????????????
                            break;
                    }
                }
            }
        });
        //??????????????????
        viewModel.uc.versionEntitySingl.observe(this, new Observer<VersionBean>() {
            @Override
            public void onChanged(VersionBean versionEntity) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (versionEntity == null || versionEntity.getVersion_code().intValue() <= AppConfig.VERSION_CODE.intValue()) {
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
                            .setContent(getString(R.string.playcc_conflirm_log_out))
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
                            setSelectedItemId(binding.navigationHomeImg);//tbar???????????????
                            break;
                        case "redio":
                            setSelectedItemId(binding.navigationRadioImg);//tbar???????????????
                            break;
                        case "message":
                            setSelectedItemId(binding.navigationMessageImg);//tbar???????????????
                            break;
                        case "mine":
                            setSelectedItemId(binding.navigationMineImg);//tbar???????????????
                            break;
                    }
                }
            }
        });
        viewModel.uc.taskCenterclickTab.observe(this, taskMainTabEvent -> {
            if (taskMainTabEvent != null) {
                if (taskMainTabEvent.isTbarClicked()) {//tbar?????????????????????
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

    //????????????
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

    //????????????
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
        Intent intent = new Intent(mActivity, DialogDiamondRechargeActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.pop_enter_anim, 0);
    }

    private void dialogCallback() {
        //????????????
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
     * ????????????dialog
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
                .setTitle(getString(R.string.playcc_reward_title))
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

    private void setGiftViewBanner(MqBroadcastGiftBean mqttMessageEntity, MqBroadcastGiftUserBean leftUser, MqBroadcastGiftUserBean rightUser, View streamerView) {
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

        String tips = mActivity.getString(R.string.playcc_send_tips);
        String detail = tips + "[" + mqttMessageEntity.getGiftName() + "]";
        SpannableString stringBuilder = new SpannableString(detail);
        ForegroundColorSpan blueSpanWhite = new ForegroundColorSpan(ColorUtils.getColor(R.color.white));
        ForegroundColorSpan blueSpanYellow = new ForegroundColorSpan(ColorUtils.getColor(R.color.yellow_617));
        stringBuilder.setSpan(blueSpanWhite, 0, detail.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(blueSpanYellow, tips.length(), detail.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        giftTitle.setText(stringBuilder);
        ImageView giftNumImg = streamerView.findViewById(R.id.gift_count);
        RelativeLayout rl_banner_gift = streamerView.findViewById(R.id.rl_banner_gift);
        rl_banner_gift.setOnClickListener(v -> {});
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

    private void broadcastGiftImg(MqBroadcastGiftUserBean giftUserEntity,ImageView userImg){
        if (giftUserEntity.getSex()!=null
                && giftUserEntity.getIsVip()!=null
                && giftUserEntity.getCertification()!= null){

            if(giftUserEntity.getIsVip() == 1){
                if (giftUserEntity.getSex()==1){
                    userImg.setImageResource(R.drawable.ic_vip);
                    userImg.setVisibility(View.VISIBLE);
                    return;
                }else {
                    userImg.setImageResource(R.drawable.ic_good_goddess);
                    userImg.setVisibility(View.VISIBLE);
                    return;
                }
            }
            if (giftUserEntity.getCertification()== 1){
                userImg.setImageResource(R.drawable.ic_real_people);
                userImg.setVisibility(View.VISIBLE);
                return;
            }
        }
        userImg.setVisibility(View.GONE);

    }

    @Override
    public void initData() {
        super.initData();
        int ImgSrcPath = TbarCenterImgConfig.getInstance().getImgSrcPath();
        if(ImgSrcPath!=-1){
            binding.navigationRankImg.setImageResource(ImgSrcPath);
        }
        initView();
    }

    /**
     * @return void
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [hidden]
     * @Date 2021/8/4
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppConfig.isMainPage = !hidden;
        if (!hidden) {
            if (System.currentTimeMillis() - TOUCH_TIME > WAIT_TIME) {
                //????????????????????????
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
        mFragments[FIRST] = new HomeMainFragment();
        mFragments[SECOND] = new RadioFragment();
        mFragments[THIRD] = new TaskCenterFragment();
        mFragments[FOURTH] = new MessageMainFragment();
        mFragments[FIFTH] = new MineFragment();
        tvBadgeNum = binding.tvMsgCount;
        //?????????Tab???

        //????????????
        binding.navigationHome.setOnClickListener(v -> setSelectedItemId(binding.navigationHomeImg));
        binding.navigationRadio.setOnClickListener(v -> setSelectedItemId(binding.navigationRadioImg));
        binding.navigationRank.setOnClickListener(v -> setSelectedItemId(binding.navigationRankImg));
        binding.navigationMessage.setOnClickListener(v -> setSelectedItemId(binding.navigationMessageImg));
        binding.navigationMine.setOnClickListener(v -> setSelectedItemId(binding.navigationMineImg));
        binding.bubbleTip.setOnClickListener(v -> setSelectedItemId(binding.navigationMessageImg));
        mainViewPager = binding.viewPager;
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this);
        fragmentAdapter.setFragmentList(mFragments);
        // ??????????????????????????????
        mainViewPager.setUserInputEnabled(false);
        // ?????????????????? ??????????????????
        mainViewPager.setOffscreenPageLimit(5);
        //??????????????????--??????BUG
        mainViewPager.setSaveEnabled(false);
        mainViewPager.setAdapter(fragmentAdapter);
        mainViewPager.setCurrentItem(0, false);

        if (selTabImgLayout == null) {
            selTabImgLayout = binding.navigationHomeImg;
        } else {
            // ???????????????????????????tab?????????????????????
            ImageView tmp = selTabImgLayout;
            setSelectedItemId(tmp);
            selTabImgLayout = tmp;
        }
    }

    private void setSelectedItemId(ImageView view) {
        if(selTabImgLayout==null){
            selTabImgLayout = view;
        }
        if (selTabImgLayout.getId() == view.getId()) {
            int id = view.getId();
            if (id == R.id.navigation_home_img) {
                RxBus.getDefault().post(new GenderToggleEvent());
            }
        }
        if (selTabImgLayout != null && selTabImgLayout.getId() != view.getId()) {
            resetMenuState(selTabImgLayout.getId());
            int id = view.getId();
            if (id == R.id.navigation_home_img) {
                mainViewPager.setCurrentItem(0, false);
                binding.navigationHomeImg.setVisibility(View.VISIBLE);
                binding.navigationHomeImg.setImageResource(R.drawable.tab_home_checked);
            } else if (id == R.id.navigation_radio_img) {
                mainViewPager.setCurrentItem(1, false);
                binding.navigationRadioImg.setVisibility(View.VISIBLE);
                binding.navigationRadioImg.setImageResource(R.drawable.tab_radio_checked);
            } else if (id == R.id.navigation_rank_img) {
                mainViewPager.setCurrentItem(2, false);
                binding.navigationRankImg.setVisibility(View.VISIBLE);
                binding.navigationRankImg.setImageResource(R.drawable.toolbar_icon_center_checked);
            } else if (id == R.id.navigation_message_img) {
                mainViewPager.setCurrentItem(3, false);
                binding.navigationMessageImg.setVisibility(View.VISIBLE);
                binding.navigationMessageImg.setImageResource(R.drawable.tab_message_checked);
            } else if (id == R.id.navigation_mine_img) {
                mainViewPager.setCurrentItem(4, false);
                binding.navigationMineImg.setVisibility(View.VISIBLE);
                binding.navigationMineImg.setImageResource(R.drawable.tab_mine_checked);
            }
            selTabImgLayout = view;
        }
    }

    //?????????????????????
    private void resetMenuState(int id) {
        if (id == R.id.navigation_home_img) { //??????
            binding.navigationHomeImg.setImageResource(R.drawable.tab_home_normal);
        } else if (id == R.id.navigation_radio_img) { //??????
            binding.navigationRadioImg.setImageResource(R.drawable.tab_radio_normal);
        } else if (id == R.id.navigation_rank_img) { //????????????
            binding.navigationRankImg.setImageResource(R.drawable.toolbar_icon_center_normal);
            binding.navigationRankText.setTextColor(getResources().getColor(R.color.navigation_checkno));
        } else if (id == R.id.navigation_message_img) { //????????????
            binding.navigationMessageImg.setImageResource(R.drawable.tab_message_normal);
        } else if (id == R.id.navigation_mine_img) { //????????????
            binding.navigationMineImg.setImageResource(R.drawable.tab_mine_female_normal);
        }
    }
    @Override
    protected boolean isUmengReportPage() {
        return false;
    }
}