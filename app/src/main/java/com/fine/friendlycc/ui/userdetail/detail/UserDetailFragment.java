package com.fine.friendlycc.ui.userdetail.detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.databinding.FragmentUserDetailBinding;
import com.fine.friendlycc.bean.ApiConfigManagerBean;
import com.fine.friendlycc.bean.EvaluateBean;
import com.fine.friendlycc.bean.EvaluateItemBean;
import com.fine.friendlycc.bean.EvaluateObjBean;
import com.fine.friendlycc.bean.UserDetailBean;
import com.fine.friendlycc.helper.DialogHelper;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.fine.friendlycc.ui.dialog.CommitEvaluateDialog;
import com.fine.friendlycc.ui.dialog.MyEvaluateDialog;
import com.fine.friendlycc.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.ui.userdetail.playnum.CoinPaySheetUserMain;
import com.fine.friendlycc.ui.userdetail.report.ReportUserFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.ListUtils;
import com.fine.friendlycc.utils.PictureSelectorUtil;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.AppBarStateChangeListener;
import com.fine.friendlycc.widget.bottomsheet.BottomSheet;
import com.fine.friendlycc.widget.coinpaysheet.CoinPaySheet;
import com.fine.friendlycc.widget.custom.FlowAdapter;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.fine.friendlycc.widget.dialog.WebViewDialog;
import com.fine.friendlycc.widget.emptyview.EmptyState;
import com.google.android.material.appbar.AppBarLayout;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.qcloud.tuicore.Status;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei ????????????fragment
 */
@SuppressLint("StringFormatMatches")
public class UserDetailFragment extends BaseToolbarFragment<FragmentUserDetailBinding, UserDetailViewModel> implements View.OnClickListener {

    public static final String ARG_USER_DETAIL_USER_ID = "arg_user_detail_user_id";
    public static final String ARG_USER_DETAIL_MORENUMBER = "arg_user_detail_morenumber";
    public static final String ARG_USER_DETAIL_POSITION = "arg_user_detail_position";

    private int userId;
    private int position;

    private boolean flagShow = false;

    private boolean toolbarUp = false;

    public static Bundle getStartBundle(int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_DETAIL_USER_ID, userId);
        return bundle;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_user_detail;
    }

    @Override
    public void initParam() {
        super.initParam();
        userId = getArguments().getInt(ARG_USER_DETAIL_USER_ID, 0);
        position = getArguments().getInt(ARG_USER_DETAIL_POSITION, -1);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (AudioPlayer.getInstance().isPlaying()) {
            AudioPlayer.getInstance().stopPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CCApplication.isShowNotPaid = false;
    }

    @Override
    public UserDetailViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        UserDetailViewModel userDetailViewModel = ViewModelProviders.of(this, factory).get(UserDetailViewModel.class);
        userDetailViewModel.userId.set(userId);
        userDetailViewModel.stateModel.setEmptyState(EmptyState.NORMAL);
        return userDetailViewModel;
    }

    @Override
    public void initViewObservable() {
        //????????????
        viewModel.uc.sendAccostFirstError.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                CCApplication.instance().logEvent(AppsFlyerEvent.Top_up);
                toRecharge();
            }
        });
        viewModel.uc.sendDialogViewEvent.observe(this, event -> {
            paySelectionboxChoose();
        });
        //????????????
        viewModel.uc.otherBusy.observe(this, o -> {
            TraceDialog.getInstance(UserDetailFragment.this.getContext())
                    .chooseType(TraceDialog.TypeEnum.CENTER)
                    .setTitle(StringUtils.getString(R.string.playcc_other_busy_title))
                    .setContent(StringUtils.getString(R.string.playcc_other_busy_text))
                    .setConfirmText(StringUtils.getString(R.string.playcc_mine_trace_delike_confirm))
                    .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                        @Override
                        public void confirm(Dialog dialog) {

                            dialog.dismiss();
                        }
                    }).TraceVipDialog().show();
        });
        //????????????????????????
        viewModel.uc.finishRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //????????????
                binding.refreshLayout.finishRefresh(100);
            }
        });
        viewModel.uc.evaluateItemEntityList.observe(this, new Observer<List<EvaluateItemBean>>() {
            @Override
            public void onChanged(List<EvaluateItemBean> evaluateItemEntities) {
                if (!ListUtils.isEmpty(evaluateItemEntities)) {
                    // ?????? Adapter
                    FlowAdapter adapter = new FlowAdapter(UserDetailFragment.this.getContext(), evaluateItemEntities);
                    binding.flowLayout.setAdapter(adapter);
                    // ???????????????????????????
                    binding.flowLayout.setMaxLines(4);
                    // ??????????????? item ???
                    binding.flowLayout.post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }
        });
        viewModel.uc.clickMore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if (viewModel.detailEntity.get() == null) {
                    return;
                }
//                String[] items = new String[]{getString(R.string.pull_black_shield_both_sides), getString(R.string.remark_user_title), getString(R.string.report_user_title)};
                String[] items = new String[]{getString(R.string.playcc_pull_black_shield_both_sides), getString(R.string.playcc_report_user_title)};
                if (viewModel.detailEntity.get().getCollect()) {
                    items = new String[]{getString(R.string.playcc_pull_black_shield_both_sides), getString(R.string.playcc_report_user_title), getString(R.string.playcc_cancel_zuizong)};
                }
                if (viewModel.detailEntity.get().getIsBlacklist() == 1) {
                    items[0] = getString(R.string.playcc_remove_black_shield_both_sides);
                }
                new BottomSheet.Builder(mActivity).setDatas(items).setOnItemSelectedListener((bottomSheet, position) -> {
                    bottomSheet.dismiss();
                    if (position == 0) {
                        if (viewModel.detailEntity.get().getIsBlacklist() == 1) {
                            viewModel.delBlackList();
                        } else {
                            MVDialog.getInstance(mActivity)
                                    .setContent(getString(R.string.playcc_dialog_add_blacklist_content))
                                    .setConfirmText(getString(R.string.playcc_dialog_add_blacklist_content2))
                                    .setConfirmOnlick(dialog -> {
                                        viewModel.addBlackList();
                                    })
                                    .chooseType(MVDialog.TypeEnum.CENTERWARNED)
                                    .show();
                        }
//                    } else if (position == 1) {
//                        Bundle bundle = RemarkUserFragment.getStartBundle(userId);
//                        RemarkUserFragment remarkUserFragment = new RemarkUserFragment();
//                        remarkUserFragment.setArguments(bundle);
//                        start(remarkUserFragment);
                    } else if (position == 1) {
                        Bundle bundle = ReportUserFragment.getStartBundle("home", userId);
                        ReportUserFragment reportUserFragment = new ReportUserFragment();
                        reportUserFragment.setArguments(bundle);
                        start(reportUserFragment);
                    } else if (position == 2) {
                        if (viewModel.detailEntity.get().getCollect()) {//????????????
                            viewModel.collectOnClickCommand.execute();
                        }
                    }
                }).setCancelButton(StringUtils.getString(R.string.playcc_cancel), new BottomSheet.CancelClickListener() {
                    @Override
                    public void onCancelClick(BottomSheet bottomSheet) {
                        bottomSheet.dismiss();
                    }
                }).build().show();
            }
        });
        viewModel.uc.clickEvaluate.observe(this, evaluateEntities -> {
            if (viewModel.detailEntity.get() == null) {
                return;
            }
            List<EvaluateObjBean> list = null;
            if (viewModel.detailEntity.get().getSex() == 1) {
                list = Injection.provideDemoRepository().readMaleEvaluateConfig();
            } else {
                list = Injection.provideDemoRepository().readFemaleEvaluateConfig();
            }
            List<EvaluateItemBean> items = new ArrayList<>();
            for (EvaluateObjBean configEntity : list) {
                EvaluateItemBean evaluateItemEntity = new EvaluateItemBean(configEntity.getId(), configEntity.getName(), configEntity.getType() == 1);
                items.add(evaluateItemEntity);
                for (EvaluateBean evaluateEntity : evaluateEntities) {
                    if (configEntity.getId() == evaluateEntity.getTagId()) {
                        evaluateItemEntity.setNumber(evaluateEntity.getNumber());
                    }
                }
            }
            MyEvaluateDialog dialog = new MyEvaluateDialog(viewModel.detailEntity.get().getSex() == 1 ? MyEvaluateDialog.TYPE_USER_MALE : MyEvaluateDialog.TYPE_USER_FEMALE, items);
            dialog.setEvaluateDialogListener(new MyEvaluateDialog.EvaluateDialogListener() {
                @Override
                public void onEvaluateClick(MyEvaluateDialog dialog) {
                    if (viewModel.canEvaluate.get() == false) {
                        ToastUtils.showShort(viewModel.detailEntity.get().isMale() ? R.string.playcc_no_information_cant_comment_him : R.string.playcc_no_information_cant_comment);
                        return;
                    }
                    dialog.dismiss();
                    CommitEvaluateDialog commitEvaluateDialog = new CommitEvaluateDialog(items);
                    commitEvaluateDialog.show(getChildFragmentManager(), CommitEvaluateDialog.class.getCanonicalName());
                    commitEvaluateDialog.setCommitEvaluateDialogListener((dialog12, entity) -> {
                        dialog12.dismiss();
                        if (entity != null) {
                            if (entity.isNegativeEvaluate()) {
                                MVDialog.getInstance(mActivity)
                                        .setContent(getString(R.string.playcc_provide_screenshot))
                                        .setConfirmText(getString(R.string.playcc_choose_photo))
                                        .setConfirmOnlick(dialog1 -> {
                                            dialog1.dismiss();
                                            PictureSelectorUtil.selectImage(mActivity, false, 1, new OnResultCallbackListener<LocalMedia>() {
                                                @Override
                                                public void onResult(List<LocalMedia> result) {
                                                    if (!result.isEmpty()) {
                                                        viewModel.commitNegativeEvaluate(entity.getTagId(), result.get(0).getCompressPath());
                                                    }
                                                }

                                                @Override
                                                public void onCancel() {
                                                }
                                            });
                                        })
                                        .chooseType(MVDialog.TypeEnum.CENTERWARNED)
                                        .show();
                            } else {
                                viewModel.commitUserEvaluate(entity.getTagId(), null);
                            }
                        } else {
                            ToastUtils.showShort(R.string.playcc_user_detail_evaluation_hint2);
                        }
                    });
                }

                @Override
                public void onAnonymousReportClick(MyEvaluateDialog dialog) {
                    dialog.dismiss();
                    Bundle bundle = ReportUserFragment.getStartBundle("home", userId);
                    ReportUserFragment fragment = new ReportUserFragment();
                    fragment.setArguments(bundle);
                    start(fragment);
                }
            });
            dialog.show(getChildFragmentManager(), MyEvaluateDialog.class.getCanonicalName());
        });
        viewModel.uc.clickApplyCheckDetail.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String nickName) {
                MVDialog.getInstance(mActivity)
                        .setTitele(String.format(getString(R.string.playcc_setting_limit), nickName))
                        .setContent(getString(R.string.playcc_setting_limit_content))
                        .setConfirmText(getString(R.string.playcc_choose_photo))
                        .setConfirmOnlick(dialog -> {
                            dialog.dismiss();
                            choosePhoto();
                        })
                        .chooseType(MVDialog.TypeEnum.CENTER)
                        .show();
            }
        });
        viewModel.uc.clickPayAlbum.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (viewModel.detailEntity.get() == null) {
                    return;
                }
                //?????????????????????
                payLockAlbum(userId, viewModel.detailEntity.get().getNickname(), viewModel.detailEntity.get().getAlbumPayMoney());
            }
        });
        viewModel.uc.clickVipCheckAlbum.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                //??????????????????
                if (viewModel.detailEntity.get() == null) {
                    return;
                }
                vipLockAlbum(viewModel.detailEntity.get().getNickname(), count, viewModel.detailEntity.get().getAlbumPayMoney());
            }
        });
        viewModel.uc.clickVipChat.observe(this, integer -> vipCheckChat(integer, ConfigManager.getInstance().getImMoney()));
        viewModel.uc.clickConnMic.observe(this, aVoid -> System.out.println("??????"));
        viewModel.uc.todayCheckNumber.observe(this, integer -> DialogHelper.showCheckUserNumberDialog(UserDetailFragment.this, integer));

        viewModel.uc.isAlertVipMonetyunlock.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                getUserdetailUnlock();
            }
        });
        viewModel.uc.VipSuccessFlag.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                flagShow = true;
            }
        });
        viewModel.uc.startVideo.observe(this,o -> {
            startVideoCalling();
        });
        viewModel.uc.startAudio.observe(this,o -> {
            startAudioCalling();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        CCApplication.isShowNotPaid = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            // if(!ConfigManager.getInstance().isVip()){
//                if(flagShow){
//                    return;
//                }
            if(viewModel.detailEntity.get()!=null){
                Integer moreNumber = viewModel.detailEntity.get().getMoreNumber();
                if ((moreNumber != null && moreNumber <= 0) || !viewModel.detailEntity.get().isBrowse()) {
                    getUserdetailUnlock();
                }
            }


            // }
        } else {
            if (AudioPlayer.getInstance().isPlaying()) {
                AudioPlayer.getInstance().stopPlay();
                binding.audioStart.setImageResource(R.drawable.radio_item_play_audio);
            }
        }
    }

//    @Override
//    public boolean onBackPressedSupport() {
//        Log.e("userDetail??????????????????","===================");
//       return true;
//        // return mDelegate.onBackPressedSupport();
//    }

    private void startVideoCalling() {
        new RxPermissions(mActivity)
                .request(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        CCApplication.instance().logEvent(AppsFlyerEvent.im_video_call);
                        viewModel.getCallingInvitedInfo(2, viewModel.detailEntity.get().getImUserId(), viewModel.detailEntity.get().getImToUserId());
                    } else {
                        TraceDialog.getInstance(mActivity)
                                .setCannelOnclick(new TraceDialog.CannelOnclick() {
                                    @Override
                                    public void cannel(Dialog dialog) {

                                    }
                                })
                                .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                                    @Override
                                    public void confirm(Dialog dialog) {
                                        new RxPermissions(mActivity)
                                                .request(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
                                                .subscribe(granted -> {
                                                    if (granted) {
                                                        CCApplication.instance().logEvent(AppsFlyerEvent.im_video_call);
                                                        viewModel.getCallingInvitedInfo(2, viewModel.detailEntity.get().getImUserId(), viewModel.detailEntity.get().getImToUserId());
                                                    }
                                                });
                                    }
                                })
                                .AlertCallAudioPermissions().show();
                    }
                });
    }

    private void startAudioCalling() {
        new RxPermissions(mActivity)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe(granted -> {
                    if (granted) {
                        CCApplication.instance().logEvent(AppsFlyerEvent.im_voice_call);
                        viewModel.getCallingInvitedInfo(1, viewModel.detailEntity.get().getImUserId(), viewModel.detailEntity.get().getImToUserId());
                    } else {
                        TraceDialog.getInstance(mActivity)
                                .setCannelOnclick(new TraceDialog.CannelOnclick() {
                                    @Override
                                    public void cannel(Dialog dialog) {

                                    }
                                })
                                .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                                    @Override
                                    public void confirm(Dialog dialog) {
                                        new RxPermissions(mActivity)
                                                .request(Manifest.permission.RECORD_AUDIO)
                                                .subscribe(granted -> {
                                                    if (granted) {
                                                        CCApplication.instance().logEvent(AppsFlyerEvent.im_voice_call);
                                                        viewModel.getCallingInvitedInfo(1, viewModel.detailEntity.get().getImUserId(), viewModel.detailEntity.get().getImToUserId());
                                                    }
                                                });
                                    }
                                }).AlertCallAudioPermissions().show();
                    }
                });
    }

    public void getUserdetailUnlock() {
        if (flagShow) {//????????????????????????
            return;
        }
        //?????????????????????
        if (!ConfigManager.getInstance().isMale()) {
            return;
        }
        UserDetailBean userDetailEntity = viewModel.detailEntity.get();
        if (!ObjectUtils.isEmpty(userDetailEntity.getIsView()) && userDetailEntity.getIsView().intValue() == 1 && userDetailEntity.isBrowse()) {
            return;
        }
        Integer MoreNumber = ConfigManager.getInstance().getGetUserHomeMoney();
        Integer getMoreNumber = viewModel.detailEntity.get().getMoreNumber();
        if (getMoreNumber == null) {
            return;
        }
        String title1 = null;
        String title2 = null;
        if (getMoreNumber.intValue() <= 2) {
            if (getMoreNumber.intValue() <= 0) {
                title1 = getString(R.string.playcc_userdetail_unlock_title1);
            } else {
                title1 = String.format(getString(R.string.playcc_userdetail_unlock_title), viewModel.detailEntity.get().getMoreNumber());
            }
            title2 = String.format(getString(R.string.playcc_pay_unlock), ConfigManager.getInstance().getGetUserHomeMoney());
        } else {
            title1 = String.format(getString(R.string.playcc_userdetail_unlock_title), viewModel.detailEntity.get().getMoreNumber());
        }
        String vipTitle = null;
        if (!ConfigManager.getInstance().isVip()) {
            vipTitle = getString(R.string.playcc_userdetail_unlock_btn_vip);
        }
        MMAlertDialog.isAlertVipMonetyunlock(UserDetailFragment.this.getContext(), true, title1, getString(R.string.playcc_userdetail_unlock_context), vipTitle, title2, new MMAlertDialog.AlertVipMonetyunlockInterface() {

            @Override
            public void confirm(DialogInterface dialog, int which) {
                dialog.dismiss();
                CCApplication.instance().logEvent(AppsFlyerEvent.Become_A_VIP);
                viewModel.start(VipSubscribeFragment.class.getCanonicalName());
            }

            @Override
            public void confirmTwo(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    if (UserDetailFragment.this.getActivity() == null) {
                        return;
                    }
                    new CoinPaySheetUserMain.Builder(mActivity).setPayParams(12, userId, MoreNumber, false, new CoinPaySheetUserMain.UserdetailUnlockListener() {
                        @Override
                        public void onPaySuccess(CoinPaySheetUserMain sheet, String orderNo, Integer payPrice) {
                            CCApplication.instance().logEvent(AppsFlyerEvent.Unlock_Now);
                            flagShow = true;
                            sheet.dismiss2(1);
                            dialog.dismiss();
                            viewModel.loadData();
                            ToastUtils.showShort(R.string.playcc_pay_success);
                        }

                        @Override
                        public void onPauError(int type) {
                            switch (type) {
                                case -1:
                                    getUserdetailUnlock();
                                    break;
                                case 1:
                                    break;
                                default:
                                    getUserdetailUnlock();
                                    break;
                            }
                        }
                    }).build().show();
                } catch (Exception e) {

                }
            }

            @Override
            public void cannel(DialogInterface dialog) {
                dialog.dismiss();
                if (UserDetailFragment.this.getActivity() == null) {
                    return;
                }
                if (!viewModel.detailEntity.get().isBrowse()) {
                    pop();
                }
            }
        }).show();
        //flagShow = true;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.position = position;
        binding.refreshLayout.setEnableLoadMore(false);
        binding.meAvatar.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.blackBack.setOnClickListener(this);
        binding.appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //????????????
                    binding.layoutTitle.setVisibility(View.GONE);
                    ImmersionBarUtils.setupStatusBar(UserDetailFragment.this, false, true);
                } else if (state == State.COLLAPSED) {
                    //????????????
                    binding.layoutTitle.setVisibility(View.VISIBLE);
                    ImmersionBarUtils.setupStatusBar(UserDetailFragment.this, true, true);
                } else {
                    //????????????
//                    Toast.makeText(getActivity(),"????????????",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    //???????????? ????????????7
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!toolbarUp) {
                            if (scrollX > binding.appbarLayout.getHeight() || oldScrollY > binding.appbarLayout.getHeight()) {
                                toolbarUp = true;
                                binding.layoutTitle.setVisibility(View.VISIBLE);
                                ImmersionBarUtils.setupStatusBar(UserDetailFragment.this, true, true);
                            }
                        }
                    }

                }

                if (scrollY == 0) {
                    //Log.e("=====", "????????????");
                    if (toolbarUp) {
                        toolbarUp = false;
                        //????????????
                        binding.layoutTitle.setVisibility(View.GONE);
                        ImmersionBarUtils.setupStatusBar(UserDetailFragment.this, false, true);
                    }
                }
            }
        });
        binding.audioStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status.mIsShowFloatWindow){
                    ToastUtils.showShort(R.string.audio_in_call);
                    return ;
                }
                binding.audioStart.setImageResource(R.drawable.mine_audio_stop_img);
                if (AudioPlayer.getInstance().isPlaying()) {
                    AudioPlayer.getInstance().stopPlay();
                    binding.audioStart.setImageResource(R.drawable.radio_item_play_audio);
                    return;
                }
                Glide.with(UserDetailFragment.this.getContext()).asGif().load(R.drawable.audio_waves)
                        .error(R.drawable.audio_waves_stop)
                        .placeholder(R.drawable.audio_waves_stop)
                        .into(binding.audioStart);
                AudioPlayer.getInstance().startPlay(StringUtil.getFullAudioUrl(viewModel.detailEntity.get().getSound()), new AudioPlayer.Callback() {
                    @Override
                    public void onCompletion(Boolean success, Boolean isOutTime) {
                        binding.audioStart.setImageResource(R.drawable.radio_item_play_audio);
                    }
                });
            }
        });
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            onBackClick(null);
        } else if (view.getId() == R.id.black_back) {
            onBackClick(null);
        }
    }

    private void choosePhoto() {
        PictureSelectorUtil.selectImage(mActivity, false, 1, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                viewModel.uploadImage(result.get(0).getCompressPath());
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void payLockAlbum(Integer userId, String nickName, Integer coinPrice) {
        String btn1 = "";
        if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == 1) {
            btn1 = getString(R.string.playcc_to_be_member_free_albums);
        } else {
            if (ConfigManager.getInstance().getAppRepository().readUserData().getCertification() == 1) {
                btn1 = getString(R.string.playcc_to_be_goddess_free_albums);
            } else {
                btn1 = getString(R.string.playcc_warn_no_certification);
            }
        }
        MVDialog.getInstance(UserDetailFragment.this.getContext())
                .setContent(String.format(getString(R.string.playcc_unlock_paid_album), nickName))
                .setConfirmText(btn1)
                .setConfirmTwoText(String.format(getString(R.string.playcc_pay_ro_unlock_diamond), coinPrice))
                .setConfirmOnlick(dialog -> {
//                    if(dialog!=null){
//                        dialog.dismiss();
//                    }
                    if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == 1) {
                        viewModel.start(VipSubscribeFragment.class.getCanonicalName());
                    } else {
                        viewModel.start(CertificationFemaleFragment.class.getCanonicalName());
                    }
                })
                .setConfirmTwoOnclick(dialog -> {
//                    if(dialog!=null){
//                        dialog.dismiss();
//                    }
                    new CoinPaySheet.Builder(mActivity).setPayParams(3, userId, getString(R.string.playcc_unlock_album), false, new CoinPaySheet.CoinPayDialogListener() {
                        @Override
                        public void onPaySuccess(CoinPaySheet sheet, String orderNo, Integer payPrice) {
                            sheet.dismiss();
                            ToastUtils.showShort(R.string.playcc_pay_success);
                            viewModel.payLockAlbumSuccess(userId);
                        }
                        @Override
                        public void toGooglePlayView() {
                            toRecharge();
                        }

                    }).build().show();
                })
                .getTop2BottomDialog()
                .show();
    }

    private void vipLockAlbum(String nickName, Integer number, Integer coinPrice) {
        if (number == 0) {
            MVDialog.getInstance(UserDetailFragment.this.getContext())
                    .setContent(String.format(getString(R.string.playcc_pay_dimond_content), coinPrice, nickName))
                    .setConfirmText(String.format(getString(R.string.playcc_pay_diamond), coinPrice))
                    .setConfirmOnlick(dialog -> {
                        dialog.dismiss();
                        new CoinPaySheet.Builder(mActivity).setPayParams(3, userId, getString(R.string.playcc_unlock_album), false, new CoinPaySheet.CoinPayDialogListener(){

                            @Override
                            public void onPaySuccess(CoinPaySheet sheet, String orderNo, Integer payPrice) {
                                ToastUtils.showShort(R.string.playcc_pay_success);
                                viewModel.payLockAlbumSuccess(userId);
                            }

                            @Override
                            public void toGooglePlayView() {
                                toRecharge();
                            }
                        }).build().show();
                    })
                    .chooseType(MVDialog.TypeEnum.CENTER)
                    .show();
        } else {
            MVDialog.getInstance(UserDetailFragment.this.getContext())
                    .setContent(String.format(getString(R.string.playcc_use_one_chance_content), nickName, number))
                    .setConfirmText(getString(R.string.playcc_use_one_chance))
                    .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                        @Override
                        public void confirm(MVDialog dialog) {
                            dialog.dismiss();
                            viewModel.useVipChat(userId, 2, 2);
                        }
                    })
                    .chooseType(MVDialog.TypeEnum.CENTER)
                    .show();
        }
    }

    /**
     * ?????????
     */
    private void toRecharge() {
        Intent intent = new Intent(mActivity, DialogDiamondRechargeActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.pop_enter_anim, 0);
    }

    private boolean isVip() {
        return ConfigManager.getInstance().getAppRepository().readUserData().getIsVip() == 1;
    }

    private void vipCheckChat(Integer number, Integer coinPrice) {
        if (number <= 0) {
            // ????????????????????????????????????
            MVDialog.getInstance(UserDetailFragment.this.getContext())
                    .setContent(String.format(getString(R.string.playcc_pay_diamond_content), coinPrice))
                    .setConfirmText(String.format(getString(R.string.playcc_pay_diamond), coinPrice))
                    .setConfirmOnlick(dialog -> {
                        dialog.dismiss();
                        new CoinPaySheet.Builder(mActivity).setPayParams(6, userId, getString(R.string.playcc_check_detail), false, (sheet, orderNo, payPrice) -> {
                            sheet.dismiss();
                            ToastUtils.showShort(R.string.playcc_pay_success);
                            viewModel.chatPaySuccess();
                        }).build().show();
                    })
                    .chooseType(MVDialog.TypeEnum.CENTER)
                    .show();
        } else {
            MVDialog.getInstance(UserDetailFragment.this.getContext())
                    .setContent(String.format(getString(R.string.playcc_use_one_chance_chat), number))
                    .setConfirmText(getString(R.string.playcc_use_one_chance))
                    .setConfirmOnlick(dialog -> {
                        dialog.dismiss();
                        viewModel.useVipChat(userId, 1, 1);
                    })
                    .chooseType(MVDialog.TypeEnum.CENTER)
                    .show();
        }
    }

    //?????????????????????
    private void paySelectionboxChoose() {
        if (ConfigManager.getInstance().isMale()) {
            if (ConfigManager.getInstance().isVip()) {
                googleCoinValueBox();
            } else {
                dialogRechargeShow();
            }
        } else {
            googleCoinValueBox();
        }
    }

    private void googleCoinValueBox() {
        toRecharge();
    }

    //??????????????????
    private void dialogRechargeShow() {
        ApiConfigManagerBean apiConfigManagerEntity = ConfigManager.getInstance().getAppRepository().readApiConfigManagerEntity();
        if(apiConfigManagerEntity!=null && apiConfigManagerEntity.getPlayFunWebUrl()!=null){
            String url = apiConfigManagerEntity.getPlayFunWebUrl() + AppConfig.PAY_RECHARGE_URL;
            new WebViewDialog(getContext(), mActivity, url, new WebViewDialog.ConfirmOnclick() {
                @Override
                public void webToVipRechargeVC(Dialog dialog) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    viewModel.start(VipSubscribeFragment.class.getCanonicalName());
                }

                @Override
                public void vipRechargeDiamondSuccess(Dialog dialog, Integer coinValue) {
                    if (dialog != null) {
                        this.cancel();
                        dialog.dismiss();
                    }
                }

                @Override
                public void moreRechargeDiamond(Dialog dialog) {
                    dialog.dismiss();
                    if(mActivity!=null && !mActivity.isFinishing()){
                        mActivity.runOnUiThread(() -> googleCoinValueBox());
                    }
                }

                @Override
                public void cancel() {
                }
            }).noticeDialog().show();
        }else{
            googleCoinValueBox();
        }
    }

}