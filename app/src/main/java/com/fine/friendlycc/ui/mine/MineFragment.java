package com.fine.friendlycc.ui.mine;

import static com.fine.friendlycc.ui.dialog.MyEvaluateDialog.TYPE_MYSELF;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.databinding.FragmentMineBinding;
import com.fine.friendlycc.entity.BrowseNumberEntity;
import com.fine.friendlycc.entity.EvaluateEntity;
import com.fine.friendlycc.entity.EvaluateItemEntity;
import com.fine.friendlycc.entity.EvaluateObjEntity;
import com.fine.friendlycc.entity.UserInfoEntity;
import com.fine.friendlycc.ui.base.BaseRefreshFragment;
import com.fine.friendlycc.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.fine.friendlycc.ui.dialog.MyEvaluateDialog;
import com.fine.friendlycc.ui.mine.setredpackagephoto.SetRedPackagePhotoFragment;
import com.fine.friendlycc.ui.mine.setredpackagevideo.SetRedPackageVideoFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.PictureSelectorUtil;
import com.fine.friendlycc.utils.SoftKeyBoardListener;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.AppBarStateChangeListener;
import com.fine.friendlycc.widget.animate.JumpingSpan;
import com.fine.friendlycc.widget.bottomsheet.BottomSheet;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tencent.qcloud.tuicore.Status;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class MineFragment extends BaseRefreshFragment<FragmentMineBinding, MineViewModel> {

    protected InputMethodManager inputMethodManager;
    private boolean SoftKeyboardShow = false;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
       // ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_mine;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(MineViewModel.class);
    }

    /**
     * 如果软键盘显示就隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        if (SoftKeyboardShow) {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
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
        if (!hidden) {
//            JumpingBeans.with(binding.taskRightTitle)
//                    .makeTextJump(0, binding.taskRightTitle.getText().length())
//                    .setIsWave(true)
//                    .setLoopDuration(1300)
//                    .build();

            viewModel.newsBrowseNumber();
        } else {
            if (AudioPlayer.getInstance().isPlaying()) {
                AudioPlayer.getInstance().stopPlay();
                binding.audioWaves.setImageResource(R.drawable.audio_waves_stop);
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        binding.refreshLayout.setEnableLoadMore(false);
        //允许语音开关
        binding.shAudio.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.setAllowPrivacy(viewModel.ALLOW_TYPE_AUDIO, isChecked));
        //允许视讯开关
        binding.shVideo.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.setAllowPrivacy(viewModel.ALLOW_TYPE_VIDEO, isChecked));

        binding.appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    binding.layoutTitle.setVisibility(View.GONE);
                    ImmersionBarUtils.setupStatusBar(MineFragment.this, false, true);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    binding.layoutTitle.setVisibility(View.VISIBLE);
                    ImmersionBarUtils.setupStatusBar(MineFragment.this, true, true);
                } else {
                    //中间状态
//                    Toast.makeText(getActivity(),"中间状态",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.audioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status.mIsShowFloatWindow){
                    ToastUtils.showShort(R.string.audio_in_call);
                    return;
                }
                UserInfoEntity userInfoEntity = viewModel.userInfoEntity.get();
                if(userInfoEntity!=null && !StringUtils.isEmpty(userInfoEntity.getSound()) ){
                    binding.audioStop.setImageResource(R.drawable.mine_audio_stop_img);
                    if (AudioPlayer.getInstance().isPlaying()) {
                        AudioPlayer.getInstance().stopPlay();
                        Glide.with(MineFragment.this.getContext()).asGif().load(R.drawable.audio_waves_stop)
                                .error(R.drawable.audio_waves_stop)
                                .placeholder(R.drawable.audio_waves_stop)
                                .into(binding.audioWaves);
                        return;
                    }
                    Glide.with(MineFragment.this.getContext()).asGif().load(R.drawable.audio_waves)
                            .error(R.drawable.audio_waves_stop)
                            .placeholder(R.drawable.audio_waves_stop)
                            .into(binding.audioWaves);
                    AudioPlayer.getInstance().startPlay(StringUtil.getFullAudioUrl(userInfoEntity.getSound()), new AudioPlayer.Callback() {
                        @Override
                        public void onCompletion(Boolean success, Boolean isOutTime) {
                            binding.audioStop.setImageResource(R.drawable.mine_play_audio);
                            binding.audioWaves.setImageResource(R.drawable.audio_waves_stop);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        AppContext.instance().logEvent(AppsFlyerEvent.Me);
        inputMethodManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        viewModel.uc.allowAudio.observe(this,aBoolean -> {
            binding.shAudio.setChecked(aBoolean);
        });
        viewModel.uc.allowVideo.observe(this,aBoolean -> {
            binding.shVideo.setChecked(aBoolean);
        });
        viewModel.uc.removeAudioAlert.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                MMAlertDialog.AlertAudioRemove(getContext(), false, new MMAlertDialog.DilodAlertInterface() {
                    @Override
                    public void confirm(DialogInterface dialog, int which, int sel_Index) {
                        dialog.dismiss();
                        viewModel.removeSound();
                    }

                    @Override
                    public void cancel(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        viewModel.uc.loadBrowseNumber.observe(this, new Observer<BrowseNumberEntity>() {
            @Override
            public void onChanged(BrowseNumberEntity browseNumberEntity) {
                if (!ObjectUtils.isEmpty(viewModel.userInfoEntity.get()) && !ObjectUtils.isEmpty(browseNumberEntity)) {
                    if (viewModel.userInfoEntity.get().getSex() == 0) {
                        if (ObjectUtils.isEmpty(browseNumberEntity.getFansNumber()) || browseNumberEntity.getFansNumber().intValue() < 1) {
                            binding.traceNum.setVisibility(View.GONE);
                        } else {
                            int fensi = browseNumberEntity.getFansNumber().intValue();
                            String total = fensi > 99 ? "99+" : String.valueOf(fensi);
                            binding.traceNum.setText(total);
                            binding.traceNum.setVisibility(View.VISIBLE);
                            binding.traceNum.setAlpha(0.1f);
                            binding.traceNum.animate()
                                    .alpha(1.0f)
                                    .setDuration(2000)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                        }
                                    })
                                    .start();
                        }
                    } else if (viewModel.userInfoEntity.get().getSex() == 1) {
                        if (ObjectUtils.isEmpty(browseNumberEntity.getBrowseNumber()) || browseNumberEntity.getBrowseNumber().intValue() < 1) {
                            binding.traceNum.setVisibility(View.GONE);
                        } else {
                            int fensi = browseNumberEntity.getBrowseNumber().intValue();
                            String total = fensi > 99 ? "99+" : String.valueOf(fensi);
                            binding.traceNum.setText(total);
                            binding.traceNum.setVisibility(View.VISIBLE);
                            binding.traceNum.setAlpha(0.1f);
                            binding.traceNum.animate()
                                    .alpha(1.0f)
                                    .setDuration(2000)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                        }
                                    })
                                    .start();
                        }
                    }

                } else {
                    binding.traceNum.setVisibility(View.GONE);
                }

            }
        });
        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                SoftKeyboardShow = true;
            }

            @Override
            public void keyBoardHide(int height) {
                SoftKeyboardShow = false;
            }
        });
        viewModel.uc.clickAvatar.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                chooseAvatar();
            }
        });
        viewModel.uc.clickMyEvaluate.observe(this, evaluateEntities -> {
            List<EvaluateObjEntity> list = null;
            if (Injection.provideDemoRepository().readUserData().getSex() == 1) {
                list = Injection.provideDemoRepository().readMaleEvaluateConfig();
            } else {
                list = Injection.provideDemoRepository().readFemaleEvaluateConfig();
            }
            if (list == null) {
                list = new ArrayList<>();
            }
            List<EvaluateItemEntity> items = new ArrayList<>();
            for (EvaluateObjEntity configEntity : list) {
                EvaluateItemEntity evaluateItemEntity = new EvaluateItemEntity(configEntity.getId(), configEntity.getName());
                items.add(evaluateItemEntity);
                for (EvaluateEntity evaluateEntity : evaluateEntities) {
                    if (configEntity.getId() == evaluateEntity.getTagId()) {
                        evaluateItemEntity.setNumber(evaluateEntity.getNumber());
                    }
                }
            }
            MyEvaluateDialog dialog = new MyEvaluateDialog(TYPE_MYSELF, items);
            dialog.show(getChildFragmentManager(), MyEvaluateDialog.class.getCanonicalName());
        });
        viewModel.uc.clickPrivacy.observe(this, aVoid -> {
            String[] items = new String[]{getString(R.string.playcc_public_recommended), getString(R.string.playcc_pay_to_unlock), getString(R.string.playcc_need_verify_look)};
            int selectIndex = viewModel.userInfoEntity.get().getAlbumType() - 1;
            if (selectIndex < 0) {
                selectIndex = 0;
            }
            new BottomSheet.Builder(mActivity)
                    .setType(BottomSheet.BOTTOM_SHEET_TYPE_SINGLE_CHOOSE)
                    .setTitle(getString(R.string.playcc_mine_album_privacy))
                    .setDatas(items)
                    .setSelectIndex(selectIndex)
                    .setOnItemSelectedListener((bottomSheet, position) -> {
                        bottomSheet.dismiss();
                        if (position == 0) {
                            AppContext.instance().logEvent(AppsFlyerEvent.All_Recommended);
                            viewModel.setAlbumPrivacy(1, null);
                        } else if (position == 1) {
                            if (viewModel.userInfoEntity.get().getSex() == 0 && viewModel.userInfoEntity.get().getIsVip() != 1) {
                                MVDialog.getInstance(MineFragment.this.getContext())
                                        .setTitele(getStringByResId(R.string.playcc_goddess_unlock_payalbum))
                                        .setConfirmText(getStringByResId(R.string.playcc_goddess_certification))
                                        .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                            @Override
                                            public void confirm(MVDialog dialog) {
                                                dialog.dismiss();
                                                startFragment(CertificationFemaleFragment.class.getCanonicalName());
                                            }
                                        })
                                        .chooseType(MVDialog.TypeEnum.CENTER)
                                        .show();
                                return;
                            }
                            //"非认证女士需要付费解锁才能查看你的相册，费用由你定；认证女士可以消耗一次查看机会解锁你的相册。";
                            String msg = String.format(getString(R.string.playcc_mine_contrnt_one),
                                    viewModel.userInfoEntity.get().getSex() == 1 ? getString(R.string.playcc_non_certified_lady) : getString(R.string.playcc_non_certified_man),
                                    viewModel.userInfoEntity.get().getSex() == 1 ? getString(R.string.playcc_certified_lady) : getString(R.string.playcc_certified_man));
                            MVDialog.getInstance(MineFragment.this.getContext())
                                    .setContent(msg)
                                    .setConfirmText(getString(R.string.playcc_carry_on))
                                    .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                        @Override
                                        public void confirm(MVDialog dialog) {
                                            dialog.dismiss();
                                            MVDialog.getInstance(MineFragment.this.getContext())
                                                    .setTitele(getString(R.string.playcc_set_view_amount))
                                                    .setConfirmMoneyOnclick(new MVDialog.ConfirmMoneyOnclick() {
                                                        @Override
                                                        public void confirm(MVDialog dialog, String money) {
                                                            if (!StringUtils.isEmpty(money)) {
                                                                dialog.dismiss();
                                                                AppContext.instance().logEvent(AppsFlyerEvent.Paid_me);
                                                                viewModel.setAlbumPrivacy(2, Integer.parseInt(money));
                                                            } else {
                                                                ToastUtils.showShort(R.string.playcc_please_enter_amount);
                                                            }
                                                        }
                                                    }).chooseType(MVDialog.TypeEnum.SET_MONEY)
                                                    .setOnDismiss(new MVDialog.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(Dialog dialog) {
                                                            hideSoftKeyboard();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    })
                                    .chooseType(MVDialog.TypeEnum.CENTER)
                                    .show();
                        } else if (position == 2) {
                            String msg = String.format(getString(R.string.playcc_mine_contrnt_two), viewModel.userInfoEntity.get().getSex() == 1 ? getString(R.string.playcc_lady) : getString(R.string.playcc_man));
                            MVDialog.getInstance(MineFragment.this.getContext())
                                    .setContent(msg)
                                    .chooseType(MVDialog.TypeEnum.CENTER)
                                    .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                        @Override
                                        public void confirm(MVDialog dialog) {
                                            dialog.dismiss();
                                            AppContext.instance().logEvent(AppsFlyerEvent.Ask_me);
                                            viewModel.setAlbumPrivacy(3, null);
                                        }
                                    })
                                    .chooseType(MVDialog.TypeEnum.CENTER)
                                    .show();
                        }
                    }).setCancelButton(getString(R.string.playcc_cancel), new BottomSheet.CancelClickListener() {
                @Override
                public void onCancelClick(BottomSheet bottomSheet) {
                    bottomSheet.dismiss();
                    AppContext.instance().logEvent(AppsFlyerEvent.cancel_me);
                    hideSoftInput();
                }
            }).build().show();
        });
        viewModel.uc.clickSetRedPackagePhoto.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                String[] items = new String[]{getString(R.string.playcc_mine_set_redpackage_photo), getString(R.string.playcc_setting_red_video)};
                new BottomSheet.Builder(mActivity)
                        .setType(BottomSheet.BOTTOM_SHEET_TYPE_NORMAL)
                        .setDatas(items)
                        .setOnItemSelectedListener(new BottomSheet.ItemSelectedListener() {
                            @Override
                            public void onItemSelected(BottomSheet bottomSheet, int position) {
                                bottomSheet.dismiss();
                                if (viewModel.userInfoEntity.get().getIsVip() != 1) {
                                    MVDialog.getInstance(MineFragment.this.getContext())
                                            .setTitele(getStringByResId(R.string.playcc_goddess_unlock_redpackage_photo))
                                            .setConfirmText(getStringByResId(R.string.playcc_goddess_certification))
                                            .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                                @Override
                                                public void confirm(MVDialog dialog) {
                                                    dialog.dismiss();
                                                    startFragment(CertificationFemaleFragment.class.getCanonicalName());
                                                }
                                            })
                                            .chooseType(MVDialog.TypeEnum.CENTER)
                                            .show();
                                    return;
                                }
                                if (position == 0) {
                                    viewModel.start(SetRedPackagePhotoFragment.class.getCanonicalName());
                                } else if (position == 1) {
                                    viewModel.start(SetRedPackageVideoFragment.class.getCanonicalName());
                                }
                            }
                        }).setCancelButton(getString(R.string.playcc_cancel), new BottomSheet.CancelClickListener() {
                    @Override
                    public void onCancelClick(BottomSheet bottomSheet) {
                        bottomSheet.dismiss();
                    }
                }).build().show();
            }
        });
        viewModel.uc.clickRecoverBurn.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                MVDialog.getInstance(MineFragment.this.getContext())
                        .setContent(getString(R.string.playcc_dialog_recover_burn_content))
                        .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                            @Override
                            public void confirm(MVDialog dialog) {
                                dialog.dismiss();
                                viewModel.burnReset();
                            }
                        })
                        .chooseType(MVDialog.TypeEnum.CENTER)
                        .show();
            }
        });
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        binding.refreshLayout.setEnableLoadMore(false);
        binding.recyclerView.setNestedScrollingEnabled(false);
    }

    private void chooseAvatar() {
        PictureSelectorUtil.selectImageAndCrop(mActivity, true, 1, 1, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                viewModel.userInfoEntity.get().setAvatar(result.get(0).getCutPath());
                viewModel.saveAvatar(result.get(0).getCutPath());
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private JumpingSpan[] buildWavingSpans(SpannableStringBuilder sbb, TextView tv) {
        JumpingSpan[] spans;
        int loopDuration = 1300;
        int startPos = 0;//textview字体的开始位置
        int endPos = tv.getText().length();//结束位置
        int waveCharDelay = loopDuration / (3 * (endPos - startPos));//每个字体延迟的时间
        spans = new JumpingSpan[endPos - startPos];
        for (int pos = startPos; pos < endPos; pos++) {//设置每个字体的jumpingspan
            JumpingSpan jumpingBean =
                    new JumpingSpan(tv, loopDuration, pos - startPos, waveCharDelay, 0.65f);
            sbb.setSpan(jumpingBean, pos, pos + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spans[pos - startPos] = jumpingBean;
        }
        return spans;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (AudioPlayer.getInstance().isPlaying()) {
            AudioPlayer.getInstance().stopPlay();
        }
    }
}
