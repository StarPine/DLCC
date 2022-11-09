package com.fine.friendlycc.ui.task;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.TaskCenterFragmentBinding;
import com.fine.friendlycc.entity.BonusGoodsEntity;
import com.fine.friendlycc.entity.EjectEntity;
import com.fine.friendlycc.entity.EjectSignInEntity;
import com.fine.friendlycc.entity.GoodsEntity;
import com.fine.friendlycc.entity.SystemConfigTaskEntity;
import com.fine.friendlycc.entity.TaskConfigEntity;
import com.fine.friendlycc.event.TaskMainTabEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.coinrechargesheet.CoinExchargeItegralDialog;
import com.fine.friendlycc.widget.dialog.TraceDialog;

import java.util.List;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/8/7 10:52
 * Description: 任务中心
 */
public class TaskCenterFragment extends BaseToolbarFragment<TaskCenterFragmentBinding, TaskCenterViewModel> {

    private static final String TAG = "任务中心签到领取会员";

    private int RcvHeight = -1;
    private String getHeadImg = null;

    private int toolbarHeight = -1;
    private boolean toolbarUp = false;

    //是否首次签到
    private final boolean isFirstSign = false;
    private TaskConfigEntity.MaleConfigEntity maleConfig7;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.task_center_fragment;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public TaskCenterViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(TaskCenterViewModel.class);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (ApiUitl.taskRefresh) {
            ApiUitl.taskRefresh = false;
            //viewModel.getTaskConfig();
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
            if (ApiUitl.taskBottom) {
                ApiUitl.taskBottom = false;
            }
            if (ApiUitl.taskTop) {
                ApiUitl.taskTop = false;
                // 让页面返回顶部
                binding.nestedScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.nestedScroll.post(new Runnable() {
                            public void run() {
                                // 滚动至顶部
                                try {
                                    binding.nestedScroll.smoothScrollTo(0, 0);
                                } catch (Exception e) {

                                }
                                // 滚动到底部
                                //sc.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                });
            }
            if (ApiUitl.taskDisplay) {
                ApiUitl.taskDisplay = false;
                if (!StringUtils.isEmpty(AppConfig.FukubukuroWebUrl)) {
//                Bundle bundle = new Bundle();
//                bundle.putString("link", $goodBagUrl);
//                start(FukubukuroFragment.class.getCanonicalName(), bundle);
                    RxBus.getDefault().post(new TaskMainTabEvent(true));
                }
            }
            viewModel.getTaskListConfig();
        }
    }

    @Override
    public void initData() {
        super.initData();
        binding.refreshLayout.setEnableLoadMore(false);
        toolbarHeight = binding.taskTitle1.getHeight();
        AppContext.instance().logEvent(AppsFlyerEvent.task_center);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        binding.dailyTaskRx.setLayoutManager(layoutManager);
        binding.nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    //判断版本 大于安卓7
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!toolbarUp) {
                            if (scrollX > toolbarHeight || oldScrollY > toolbarHeight) {
                                toolbarUp = true;
                                binding.taskTitle1.setImageResource(R.color.white);
                            }
                        }
                    }

                    if (!viewModel.loadTaskAdFlag) {
                        if (viewModel.adItemEntityObservableField.get().size() == 0 && scrollY >= binding.taskNewLayout.getTop()) {
                            viewModel.loadTaskAdFlag = true;
                        }
                    }
                }

                if (scrollY == 0) {
                    //Log.e("=====", "滑倒顶部");
                    //判断版本 大于安卓7
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (toolbarUp) {
                            toolbarUp = false;
                            if (getHeadImg != null) {
                                Glide.with(TaskCenterFragment.this.getContext()).load(StringUtil.getFullImageUrl(getHeadImg))
                                        .error(R.drawable.chat_top_bar_bg)
                                        .placeholder(R.drawable.photo_mark_tran_bottom)
                                        .dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(binding.taskTitle1);
                            } else {
                                binding.taskTitle1.setImageResource(R.drawable.chat_top_bar_bg);
                            }
                        }
                    }
                }
//
//                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    Log.e("=====", "滑倒底部");
//                }
            }
        });
    }


    @Override
    public void initViewObservable() {
        //加载全部-收起
        viewModel.uc.UnfoldEvent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    ViewGroup.LayoutParams layoutParams = binding.dailyTaskRx.getLayoutParams();
                    if (RcvHeight == -1) {
                        RcvHeight = layoutParams.height;
                    }
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    binding.dailyTaskRx.setLayoutParams(layoutParams);
                } else {
                    ViewGroup.LayoutParams layoutParams = binding.dailyTaskRx.getLayoutParams();
                    layoutParams.height = RcvHeight;
                    binding.dailyTaskRx.setLayoutParams(layoutParams);
                    binding.dailyTaskRx.setNestedScrollingEnabled(false);
                }

            }
        });
        //兑换商品成功
        viewModel.uc.exchangeSuccess.observe(this, new Observer<BonusGoodsEntity>() {
            @Override
            public void onChanged(BonusGoodsEntity bonusGoodsEntity) {
                int totalMoney = viewModel.goldMoney.get().intValue();
                int money = bonusGoodsEntity.getMoney().intValue();
                viewModel.goldMoney.set(totalMoney - money);
                TraceDialog.getInstance(TaskCenterFragment.this.getContext())
                        .setTitle(getString(R.string.task_fragment_bonus_dialog_8))
                        .setContent(getString(R.string.task_fragment_bonus_dialog_9))
                        .setConfirmText(getString(R.string.task_fragment_bonus_dialog_1))
                        .setConfirmTwoText(getString(R.string.task_fragment_bonus_dialog_2))
                        .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                            @Override
                            public void confirm(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).setConfirmTwoOnlick(new TraceDialog.ConfirmTwoOnclick() {
                    @Override
                    public void confirmTwo(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).AlertTaskBonus().show();
            }
        });
        //选择兑换商品
        viewModel.uc.AlertBonuClick.observe(this, new Observer<BonusGoodsEntity>() {
            @Override
            public void onChanged(BonusGoodsEntity bonusGoodsEntity) {
                if (ObjectUtils.isEmpty(viewModel.goldMoney.get()) || viewModel.goldMoney.get().intValue() == 0) {
                    TraceDialog.getInstance(TaskCenterFragment.this.getContext())
                            .setTitle(getString(R.string.task_fragment_bonus_dialog_5))
                            .setContent(getString(R.string.task_fragment_bonus_dialog_6))
                            .setConfirmText(getString(R.string.playfun_mine_trace_delike_confirm))
                            .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                                @Override
                                public void confirm(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            }).AlertTaskBonus().show();
                    return;
                }
                int totalMoney = viewModel.goldMoney.get().intValue();
                int money = bonusGoodsEntity.getMoney().intValue();
                if (totalMoney >= money) {
                    boolean male = ConfigManager.getInstance().isMale();
                    boolean isVip = ConfigManager.getInstance().isVip();
                    if (bonusGoodsEntity.getType().intValue() == 3) {
                        if (isVip) {
                            if (male) {
                                ToastUtils.showShort(R.string.task_fragment_bonus_dialog_11);
                                return;
                            } else {
                                ToastUtils.showShort(R.string.task_fragment_bonus_dialog_12);
                                return;
                            }
                        } else {
                            if (!male) {
                                ToastUtils.showShort(R.string.task_fragment_bonus_dialog_12);
                                return;
                            }
                        }
                    }
                    String title = String.format(getString(R.string.task_fragment_bonus_dialog_7), bonusGoodsEntity.getTitle());
                    TraceDialog.getInstance(TaskCenterFragment.this.getContext())
                            .setTitle(title)
                            .setCannelText(getString(R.string.cancel))
                            .setConfirmText(getString(R.string.playfun_mine_trace_delike_confirm))
                            .setCannelOnclick(new TraceDialog.CannelOnclick() {
                                @Override
                                public void cannel(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                                @Override
                                public void confirm(Dialog dialog) {
                                    dialog.dismiss();
                                    viewModel.exchange(String.valueOf(bonusGoodsEntity.getId()), bonusGoodsEntity);
                                }
                            }).AlertTaskBonuSubHint(bonusGoodsEntity.getType().intValue() == 3).show();

                } else {
                    TraceDialog.getInstance(TaskCenterFragment.this.getContext())
                            .setTitle(getString(R.string.task_fragment_bonus_dialog_5))
                            .setContent(getString(R.string.task_fragment_bonus_dialog_6))
                            .setConfirmText(getString(R.string.playfun_mine_trace_delike_confirm))
                            .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                                @Override
                                public void confirm(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            }).AlertTaskBonus().show();
                }
            }
        });
        //加载每日签到积分
        viewModel.uc.EjectDay.observe(this, new Observer<EjectEntity>() {
            @Override
            public void onChanged(EjectEntity ejectEntity) {
                //isFirstSign = ejectEntity.getFirstSign() == 1;
                initSignDay(ejectEntity);
            }
        });
        //签到成功
        viewModel.uc.reporSignInSuccess.observe(this, new Observer<EjectSignInEntity>() {
            @Override
            public void onChanged(EjectSignInEntity ejectSignInEntity) {
                if (isFirstSign) {
                    ToastUtils.showShort(R.string.radio_daily_atendance_day_text);
                    return;
                }
                AppContext.instance().logEvent(AppsFlyerEvent.sign_day + ejectSignInEntity.getDayNumber());
                Integer isCard = ejectSignInEntity.getIsCard();
                String text = ejectSignInEntity.getText();
                String message = ejectSignInEntity.getMsg();
                //签到成功页面刷新
                checkDay(ejectSignInEntity.getDayNumber(), ejectSignInEntity.getIsCard());
                TraceDialog.getInstance(getContext())
                        .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                            @Override
                            public void confirm(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).setCannelOnclick(new TraceDialog.CannelOnclick() {
                    @Override
                    public void cannel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).AlertTaskSignDay(isCard, text, message).show();

            }
        });

        viewModel.uc.startRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.autoRefresh();
            }
        });

        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.finishRefresh(100);
            }
        });

        //监听上拉加载完成
        viewModel.uc.finishLoadmore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.finishLoadMore(100);
            }
        });
    }

    //初始化签到天数奖励配置
    public void initSignDay(EjectEntity ejectEntity) {
        Typeface typeface = Typeface.createFromAsset(TaskCenterFragment.this.getContext().getAssets(), "DIN-Bold.TTF");
        binding.day1Fen.setTypeface(typeface);
        binding.day2Fen.setTypeface(typeface);
        binding.day3Fen.setTypeface(typeface);
        binding.day4Fen.setTypeface(typeface);
        binding.day5Fen.setTypeface(typeface);
        binding.day6Fen.setTypeface(typeface);
        binding.day7Fen.setTypeface(typeface);

        binding.day1Fen.setVisibility(View.VISIBLE);
        binding.day2Fen.setVisibility(View.VISIBLE);
        binding.day3Fen.setVisibility(View.VISIBLE);
        binding.day4Fen.setVisibility(View.VISIBLE);
        binding.day5Fen.setVisibility(View.VISIBLE);
        binding.day6Fen.setVisibility(View.VISIBLE);
        binding.day7Fen.setVisibility(View.VISIBLE);

        binding.day1Fen.setTextColor(ColorUtils.getColor(R.color.task_sign_day_text));
        binding.day2Fen.setTextColor(ColorUtils.getColor(R.color.task_sign_day_text));
        binding.day3Fen.setTextColor(ColorUtils.getColor(R.color.task_sign_day_text));
        binding.day4Fen.setTextColor(ColorUtils.getColor(R.color.task_sign_day_text));
        binding.day5Fen.setTextColor(ColorUtils.getColor(R.color.task_sign_day_text));
        binding.day6Fen.setTextColor(ColorUtils.getColor(R.color.task_sign_day_text));
        binding.day7Fen.setTextColor(ColorUtils.getColor(R.color.task_sign_day_text));
        binding.day1Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day1_no));
        binding.day2Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day2_no));
        binding.day3Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day3_no));
        binding.day4Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day4_no));
        binding.day5Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day5_no));
        binding.day6Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day6_no));
        boolean sexMale = ConfigManager.getInstance().isMale();
        int day_ed = ejectEntity.getDayNumber().intValue();
        int size = 28;
        int sizeCard = 12;
        int defaultSize = 17;
        if (sexMale) {//男生
            binding.day7Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_male_day7_no));
            List<TaskConfigEntity.MaleConfigEntity> maleSignConfig = ejectEntity.getMaleConfig();
            TaskConfigEntity.MaleConfigEntity maleConfig1 = maleSignConfig.get(0);
            TaskConfigEntity.MaleConfigEntity maleConfig2 = maleSignConfig.get(1);
            TaskConfigEntity.MaleConfigEntity maleConfig3 = maleSignConfig.get(2);
            TaskConfigEntity.MaleConfigEntity maleConfig4 = maleSignConfig.get(3);
            TaskConfigEntity.MaleConfigEntity maleConfig5 = maleSignConfig.get(4);
            TaskConfigEntity.MaleConfigEntity maleConfig6 = maleSignConfig.get(5);
            maleConfig7 = maleSignConfig.get(6);
            //类型 1钻石 2卡
            if (maleConfig1.getType() == 1) {
                binding.day1Img.setImageResource(R.drawable.dialog_sign_gold_coins);
                binding.day1Img.setVisibility(View.VISIBLE);
                binding.day1Fen.setTextSize(defaultSize);
                binding.day1Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), maleConfig1.getValue()));
            } else {
                binding.day1Fen.setTextSize(sizeCard);
                binding.day1Fen.setTypeface(Typeface.DEFAULT);
                binding.day1Fen.setText(StringUtils.getString(R.string.task_sign_day3));
                binding.day1Img.setImageResource(R.drawable.task_sign_chest_img);
                binding.day1Img.setVisibility(View.VISIBLE);
            }
            if (day_ed >= 1) {
                binding.day1Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day1_ed));
                if (maleConfig1.getType() == 1) {
                    binding.day1Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                    binding.day1Img.setVisibility(View.GONE);
                    binding.day1Fen.setTextSize(size);
                } else {
                    binding.day1Fen.setVisibility(View.GONE);
                    binding.day1Img.setVisibility(View.VISIBLE);
                    binding.day1Img.setImageResource(R.drawable.task_sign_card_img);
                }
            }

            if (maleConfig2.getType() == 1) {
                binding.day2Img.setVisibility(View.VISIBLE);
                binding.day2Img.setImageResource(R.drawable.dialog_sign_gold_coins);
                binding.day2Fen.setTextSize(defaultSize);
                binding.day2Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), maleConfig2.getValue()));
            } else {
                binding.day2Fen.setTextSize(sizeCard);
                binding.day2Fen.setTypeface(Typeface.DEFAULT);
                binding.day2Fen.setText(StringUtils.getString(R.string.task_sign_day3));
                binding.day2Img.setImageResource(R.drawable.task_sign_chest_img);
                binding.day2Img.setVisibility(View.VISIBLE);
            }

            if (day_ed >= 2) {
                binding.day2Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day2_ed));
                if (maleConfig2.getType() == 1) {
                    binding.day2Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                    binding.day2Img.setVisibility(View.GONE);
                    binding.day2Fen.setTextSize(size);
                } else {
                    binding.day2Fen.setVisibility(View.GONE);
                    binding.day2Img.setVisibility(View.VISIBLE);
                    binding.day2Img.setImageResource(R.drawable.task_sign_card_img);
                }
            }

            if (maleConfig3.getType() == 1) {
                binding.day3Img.setVisibility(View.VISIBLE);
                binding.day3Img.setImageResource(R.drawable.dialog_sign_gold_coins);
                binding.day3Fen.setTextSize(defaultSize);
                binding.day3Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), maleConfig3.getValue()));
            } else {
                binding.day3Fen.setTypeface(Typeface.DEFAULT);
                binding.day3Fen.setTextSize(sizeCard);
                binding.day3Img.setImageResource(R.drawable.task_sign_chest_img);
                binding.day3Fen.setText(StringUtils.getString(R.string.task_sign_day3));
                binding.day3Img.setVisibility(View.VISIBLE);
            }

            if (day_ed >= 3) {
                binding.day3Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day3_ed));
                if (maleConfig3.getType() == 1) {
                    binding.day3Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                    binding.day3Img.setVisibility(View.GONE);
                    binding.day3Fen.setTextSize(size);
                } else {
                    binding.day3Fen.setVisibility(View.GONE);
                    binding.day3Img.setVisibility(View.VISIBLE);
                    binding.day3Img.setImageResource(R.drawable.task_sign_card_img);
                }
            }

            if (maleConfig4.getType() == 1) {
                binding.day4Img.setVisibility(View.VISIBLE);
                binding.day4Img.setImageResource(R.drawable.dialog_sign_gold_coins);
                binding.day4Fen.setTextSize(defaultSize);
                binding.day4Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), maleConfig4.getValue()));
            } else {
                binding.day4Fen.setTypeface(Typeface.DEFAULT);
                binding.day4Fen.setTextSize(sizeCard);
                binding.day4Img.setImageResource(R.drawable.task_sign_chest_img);
                binding.day4Fen.setText(StringUtils.getString(R.string.task_sign_day3));
                binding.day4Img.setVisibility(View.VISIBLE);
            }

            if (day_ed >= 4) {
                binding.day4Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day4_ed));
                if (maleConfig4.getType() == 1) {
                    binding.day4Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                    binding.day4Img.setVisibility(View.GONE);
                    binding.day4Fen.setTextSize(size);
                } else {
                    binding.day4Fen.setVisibility(View.GONE);
                    binding.day4Img.setVisibility(View.VISIBLE);
                    binding.day4Img.setImageResource(R.drawable.task_sign_card_img);
                }
            }

            if (maleConfig5.getType() == 1) {
                binding.day5Img.setVisibility(View.VISIBLE);
                binding.day5Img.setImageResource(R.drawable.dialog_sign_gold_coins);
                binding.day5Fen.setTextSize(defaultSize);
                binding.day5Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), maleConfig5.getValue()));
            } else {
                binding.day5Fen.setTypeface(Typeface.DEFAULT);
                binding.day5Fen.setTextSize(sizeCard);
                binding.day5Img.setImageResource(R.drawable.task_sign_chest_img);
                binding.day5Fen.setText(StringUtils.getString(R.string.task_sign_day3));
                binding.day5Img.setVisibility(View.VISIBLE);
            }
            if (day_ed >= 5) {
                binding.day5Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day5_ed));
                if (maleConfig5.getType() == 1) {
                    binding.day5Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                    binding.day5Img.setVisibility(View.GONE);
                    binding.day5Fen.setTextSize(size);
                } else {
                    binding.day5Fen.setVisibility(View.GONE);
                    binding.day5Img.setVisibility(View.VISIBLE);
                    binding.day5Img.setImageResource(R.drawable.task_sign_card_img);
                }
            }

            if (maleConfig6.getType() == 1) {
                binding.day6Img.setVisibility(View.VISIBLE);
                binding.day6Img.setImageResource(R.drawable.dialog_sign_gold_coins);
                binding.day6Fen.setTextSize(defaultSize);
                binding.day6Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), maleConfig6.getValue()));
            } else {
                binding.day6Fen.setTypeface(Typeface.DEFAULT);
                binding.day6Fen.setTop(3);
                binding.day6Fen.setTextSize(sizeCard);
                binding.day6Img.setImageResource(R.drawable.task_sign_chest_img);
                binding.day6Fen.setText(StringUtils.getString(R.string.task_sign_day3));
                binding.day6Img.setVisibility(View.VISIBLE);
            }

            if (day_ed >= 6) {
                binding.day6Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day6_ed));
                if (maleConfig6.getType() == 1) {
                    binding.day6Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                    binding.day6Img.setVisibility(View.GONE);
                    binding.day6Fen.setTextSize(size);
                } else {
                    binding.day6Fen.setVisibility(View.GONE);
                    binding.day6Img.setVisibility(View.VISIBLE);
                    binding.day6Img.setImageResource(R.drawable.task_sign_card_img);
                }
            }
            if (maleConfig7 != null && maleConfig7.getType() != null) {
                binding.day7Fen.setTypeface(Typeface.DEFAULT);
            }
            binding.day7Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_male_day7_no));

            if (day_ed >= 7) {
                binding.day7Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_male_day7_ed));
                binding.day7Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day7Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), maleConfig7.getValue()));
                binding.day7Fen2.setVisibility(View.GONE);
                binding.day7Fen.setTextSize(size);
            } else {
                binding.day7Fen.setText(StringUtils.getString(R.string.task_sign_day7));
                binding.day7Fen.setTextSize(sizeCard);
                binding.day7Fen2.setVisibility(View.VISIBLE);
            }
        } else {//女生
            binding.day7Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_girl_day7_no));
            binding.day1Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), ejectEntity.getFemaleConfig().get(0)));
            binding.day2Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), ejectEntity.getFemaleConfig().get(1)));
            binding.day3Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), ejectEntity.getFemaleConfig().get(2)));
            binding.day4Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), ejectEntity.getFemaleConfig().get(3)));
            binding.day5Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), ejectEntity.getFemaleConfig().get(4)));
            binding.day6Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), ejectEntity.getFemaleConfig().get(5)));
            binding.day7Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), ejectEntity.getFemaleConfig().get(6)));
            if (day_ed == 0) {
                binding.day1Img.setVisibility(View.VISIBLE);
                binding.day2Img.setVisibility(View.VISIBLE);
                binding.day3Img.setVisibility(View.VISIBLE);
                binding.day4Img.setVisibility(View.VISIBLE);
                binding.day5Img.setVisibility(View.VISIBLE);
                binding.day6Img.setVisibility(View.VISIBLE);
            }
            if (day_ed >= 1) {
                binding.day1Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day1_ed));
                binding.day1Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day1Img.setVisibility(View.GONE);
                binding.day1Fen.setTextSize(size);
            }
            if (day_ed >= 2) {
                binding.day2Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day2_ed));
                binding.day2Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day2Img.setVisibility(View.GONE);
                binding.day2Fen.setTextSize(size);
            }
            if (day_ed >= 3) {
                binding.day3Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day3_ed));
                binding.day3Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day3Img.setVisibility(View.GONE);
                binding.day3Fen.setTextSize(size);
            }
            if (day_ed >= 4) {
                binding.day4Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day4_ed));
                binding.day4Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day4Img.setVisibility(View.GONE);
                binding.day4Fen.setTextSize(size);
            }
            if (day_ed >= 5) {
                binding.day5Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day5_ed));
                binding.day5Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day5Img.setVisibility(View.GONE);
                binding.day5Fen.setTextSize(size);
            }

            if (day_ed >= 6) {
                binding.day6Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day6_ed));
                binding.day6Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day6Img.setVisibility(View.GONE);
                binding.day6Fen.setTextSize(size);
            }
            if (day_ed >= 7) {
                binding.day7Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_girl_day7_ed));
                binding.day7Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day7Fen.setTextSize(size);
                binding.day7Fen2.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @return void
     * @Desc TODO(签到天数)
     * @author 彭石林
     * @parame [ejectEntity]
     * @Date 2021/8/9
     */
    public void checkDay(int day_ed, int isCard) {
        int size = 28;
        if (day_ed == 1) {
            binding.day1Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day1_ed));
            if (isCard == 0) {
                binding.day1Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day1Img.setVisibility(View.GONE);
                binding.day1Fen.setTextSize(size);
            } else {
                binding.day1Fen.setVisibility(View.GONE);
                binding.day1Img.setVisibility(View.VISIBLE);
                binding.day1Img.setImageResource(R.drawable.task_sign_card_img);
            }
        }

        if (day_ed == 2) {
            binding.day2Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day2_ed));
            if (isCard == 0) {
                binding.day2Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day2Img.setVisibility(View.GONE);
                binding.day2Fen.setTextSize(size);
            } else {
                binding.day2Fen.setVisibility(View.GONE);
                binding.day2Img.setVisibility(View.VISIBLE);
                binding.day2Img.setImageResource(R.drawable.task_sign_card_img);
            }
        }

        if (day_ed == 3) {
            binding.day3Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day3_ed));
            if (isCard == 0) {
                binding.day3Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day3Img.setVisibility(View.GONE);
                binding.day3Fen.setTextSize(size);
            } else {
                binding.day3Fen.setVisibility(View.GONE);
                binding.day3Img.setVisibility(View.VISIBLE);
                binding.day3Img.setImageResource(R.drawable.task_sign_card_img);
            }
        }

        if (day_ed == 4) {
            binding.day4Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day4_ed));
            if (isCard == 0) {
                binding.day4Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day4Img.setVisibility(View.GONE);
                binding.day4Fen.setTextSize(size);
            } else {
                binding.day4Fen.setVisibility(View.GONE);
                binding.day4Img.setVisibility(View.VISIBLE);
                binding.day4Img.setImageResource(R.drawable.task_sign_card_img);
            }
        }
        if (day_ed == 5) {
            binding.day5Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day5_ed));
            if (isCard == 0) {
                binding.day5Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day5Img.setVisibility(View.GONE);
                binding.day5Fen.setTextSize(size);
            } else {
                binding.day5Fen.setVisibility(View.GONE);
                binding.day5Img.setVisibility(View.VISIBLE);
                binding.day5Img.setImageResource(R.drawable.task_sign_card_img);
            }
        }

        if (day_ed == 6) {
            binding.day6Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_day6_ed));
            if (isCard == 0) {
                binding.day6Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
                binding.day6Img.setVisibility(View.GONE);
                binding.day6Fen.setTextSize(size);
            } else {
                binding.day6Fen.setVisibility(View.GONE);
                binding.day6Img.setVisibility(View.VISIBLE);
                binding.day6Img.setImageResource(R.drawable.task_sign_card_img);
            }
        }
        if (day_ed >= 7) {
            binding.day7Fen.setTextColor(TaskCenterFragment.this.getResources().getColor(R.color.colorPrimaryDark));
            binding.day7Fen2.setVisibility(View.GONE);
            binding.day7Fen.setTextSize(size);
            if (isCard == 0) {
                binding.day7Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_girl_day7_ed));
            }else {
                binding.day7Back.setBackground(TaskCenterFragment.this.getResources().getDrawable(R.drawable.task_sign_male_day7_ed));
                binding.day7Fen.setText(String.format(StringUtils.getString(R.string.playfun_coin_earnings_money_add), maleConfig7.getValue()));

            }

        }
    }

    public void DialogCoinExchangeIntegralShow(Dialog dialog) {
        CoinExchargeItegralDialog coinExchargeItegralSheetView = new CoinExchargeItegralDialog(TaskCenterFragment.this.getContext(), mActivity);
        coinExchargeItegralSheetView.setCoinRechargeSheetViewListener(new CoinExchargeItegralDialog.CoinExchargeIntegralAdapterListener() {
            @Override
            public void onPaySuccess(CoinExchargeItegralDialog sheetView, GoodsEntity sel_goodsEntity) {
                coinExchargeItegralSheetView.dismiss();
                dialog.dismiss();
                //ToastUtils.showShort(R.string.dialog_exchange_integral_success);
                //viewModel.showHUD("儲值中…");
                //viewModel.BonusExchange(sel_goodsEntity.getActualValue());
            }

            @Override
            public void onPayFailed(CoinExchargeItegralDialog sheetView, String msg) {
                coinExchargeItegralSheetView.dismiss();
                ToastUtils.showShort(msg);
                AppContext.instance().logEvent(AppsFlyerEvent.Failed_to_top_up);
            }
        });
        coinExchargeItegralSheetView.show();
    }
}
