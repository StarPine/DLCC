package com.dl.playfun.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.databinding.FragmentHomeMainBinding;
import com.dl.playfun.entity.CoinPusherDataInfoEntity;
import com.dl.playfun.entity.ConfigItemEntity;
import com.dl.playfun.entity.GoodsEntity;
import com.dl.playfun.event.LocationChangeEvent;
import com.dl.playfun.kl.view.VideoPresetActivity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.manager.LocationManager;
import com.dl.playfun.ui.base.BaseRefreshFragment;
import com.dl.playfun.ui.coinpusher.CoinPusherGameActivity;
import com.dl.playfun.ui.coinpusher.dialog.CoinPusherDialogAdapter;
import com.dl.playfun.ui.coinpusher.dialog.CoinPusherRoomListDialog;
import com.dl.playfun.ui.dialog.CityChooseDialog;
import com.dl.playfun.ui.home.accost.HomeAccostDialog;
import com.dl.playfun.utils.AutoSizeUtils;
import com.dl.playfun.utils.ImmersionBarUtils;
import com.dl.playfun.viewadapter.CustomRefreshHeader;
import com.dl.playfun.widget.coinrechargesheet.CoinRechargeSheetView;

import java.util.List;

import me.goldze.mvvmhabit.bus.RxBus;

/**
 * @author wulei
 */
public class HomeMainFragment extends BaseRefreshFragment<FragmentHomeMainBinding, HomeMainViewModel> {

    private List<ConfigItemEntity> citys;

    private CityChooseDialog cityChooseDialog;

    //充值弹窗
    private CoinRechargeSheetView coinRechargeSheetView;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_home_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HomeMainViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(HomeMainViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.refreshLayout.setRefreshHeader(new CustomRefreshHeader(getContext()));
        Glide.with(getContext()).asGif().load(R.drawable.nearby_accost_tip_img)
                .error(R.drawable.nearby_accost_tip_img)
                .placeholder(R.drawable.nearby_accost_tip_img)
                .into(binding.ivAccost);
        AppContext.instance().logEvent(AppsFlyerEvent.Nearby);
        citys = ConfigManager.getInstance().getAppRepository().readCityConfig();
        ConfigItemEntity nearItemEntity = new ConfigItemEntity();
        nearItemEntity.setId(-1);
        nearItemEntity.setName(getStringByResId(R.string.playfun_tab_female_1));
        citys.add(0, nearItemEntity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        binding.rcvTable.setLayoutManager(layoutManager);

        binding.tvLocationWarn.setOnClickListener(view -> {
            Intent intent = IntentUtils.getLaunchAppDetailsSettingsIntent(mActivity.getPackageName());
            startActivity(intent);
        });

        //展示首页广告位
        viewModel.getAdListBannber();
    }

    //充值弹窗
    private void payCoinRechargeDialog(){
        if (coinRechargeSheetView == null){
            coinRechargeSheetView = new CoinRechargeSheetView(mActivity);
            coinRechargeSheetView.setClickListener(new CoinRechargeSheetView.ClickListener() {
                @Override
                public void paySuccess(GoodsEntity goodsEntity) {
                }
            });
        }
        if (!coinRechargeSheetView.isShowing()){
            coinRechargeSheetView.show();
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //弹出推币机
        viewModel.uc.coinPusherRoomEvent.observe(this,unused->{
            //弹出推币机选择弹窗
            CoinPusherRoomListDialog coinersDialog = new CoinPusherRoomListDialog(mActivity);
            coinersDialog.setDialogEventListener(new CoinPusherRoomListDialog.DialogEventListener() {
                @Override
                public void startViewing(CoinPusherDataInfoEntity itemEntity) {
                        coinersDialog.dismiss();
                        Intent intent = new Intent(mActivity, CoinPusherGameActivity.class);
                        intent.putExtra("CoinPusherInfo",itemEntity);
                        startActivity(intent);
                }

                @Override
                public void buyErrorPayView() {
                    payCoinRechargeDialog();
                }
            });
            coinersDialog.show();
        });
        //选择城市
        viewModel.uc.clickRegion.observe(this, unused -> {
            if(cityChooseDialog==null){
                cityChooseDialog = new CityChooseDialog(getContext(),citys,viewModel.cityId.get());
            }
            cityChooseDialog.show();
            cityChooseDialog.setCityChooseDialogListener((dialog1, itemEntity) -> {
                if(itemEntity!=null){
                    if(itemEntity.getId()!=null && itemEntity.getId()==-1){
                        viewModel.cityId.set(null);
                    }else{
                        viewModel.cityId.set(itemEntity.getId());
                    }
                    viewModel.regionTitle.set(itemEntity.getName());
                }else{
                    viewModel.cityId.set(null);
                    viewModel.regionTitle.set(StringUtils.getString(R.string.playfun_tab_female_1));
                }
                binding.refreshLayout.autoRefresh();
                dialog1.dismiss();

            } );
        });
        viewModel.uc.starActivity.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aBoolean) {
                Intent intent = new Intent(mActivity, VideoPresetActivity.class);
                mActivity.startActivity(intent);
            }
        });
        viewModel.uc.isLoad.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showHud();
                } else {
                    dismissHud();
                }
            }
        });

        //tab 搭讪弹窗
        viewModel.uc.clickAccountDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String isShow) {
                HomeAccostDialog homeAccostDialog = new HomeAccostDialog(getContext());
                homeAccostDialog.setDialogAccostClicksListener(new HomeAccostDialog.DialogAccostClicksListener() {
                    @Override
                    public void onSubmitClick(HomeAccostDialog dialog, List<Integer> listData) {
                        dialog.dismiss();
                        viewModel.putAccostList(listData);
                    }

                    @Override
                    public void onCancelClick(HomeAccostDialog dialog) {
                        AppContext.instance().logEvent(AppsFlyerEvent.accost_close);
                        dialog.dismiss();
                    }
                });
                homeAccostDialog.show();
            }
        });
        //搭讪相关
        viewModel.uc.sendAccostFirstError.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                AppContext.instance().logEvent(AppsFlyerEvent.Top_up);
                toRecharge();
            }
        });
        //播放搭讪动画
        viewModel.uc.loadLoteAnime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer position) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) binding.rcvLayout.getLayoutManager();
                final View child = layoutManager.findViewByPosition(position);
                if (child != null) {
                    LottieAnimationView itemLottie = child.findViewById(R.id.item_lottie);
                    if (itemLottie != null) {
                        itemLottie.setImageAssetsFolder("images/");
                        itemLottie.addAnimatorListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                itemLottie.removeAnimatorListener(this);
                                itemLottie.setVisibility(View.GONE);
                            }
                        });
                        if (!itemLottie.isAnimating()) {
                            itemLottie.setVisibility(View.VISIBLE);
                            itemLottie.setAnimation(R.raw.accost_animation);
                            itemLottie.playAnimation();
                        }
                    }
                }
            }
        });
    }

    /**
     * 去充值
     */
    private void toRecharge() {
        CoinRechargeSheetView coinRechargeFragmentView = new CoinRechargeSheetView(mActivity);
        coinRechargeFragmentView.show();
    }

    @SuppressLint("MissingPermission")
    private void startLocation() {
        LocationManager.getInstance().getLastLocation(new LocationManager.LocationListener() {
            @Override
            public void onLocationSuccess(double lat, double lng) {
                viewModel.lat.set(lat);
                viewModel.lng.set(lng);
                RxBus.getDefault().post(new LocationChangeEvent());
            }

            @Override
            public void onLocationFailed() {
                //附近页面定位失败。通知一直下发 RxBus.getDefault().post(new LocationChangeEvent());
                RxBus.getDefault().post(new LocationChangeEvent());
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBarUtils.setupStatusBar(this, true, true);
        viewModel.locationService.get();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppContext.isHomePage = true;
        AppContext.isShowNotPaid = true;
        //30秒没有投币提示
        if(AppConfig.CoinPusherGameNotPushed){
            AppConfig.CoinPusherGameNotPushed = false;
            //弹出推币机选择弹窗
            CoinPusherRoomListDialog coinersDialog = new CoinPusherRoomListDialog(mActivity);
            coinersDialog.setDialogEventListener(new CoinPusherRoomListDialog.DialogEventListener() {
                @Override
                public void startViewing(CoinPusherDataInfoEntity itemEntity) {
                    coinersDialog.dismiss();
                    Intent intent = new Intent(mActivity, CoinPusherGameActivity.class);
                    intent.putExtra("CoinPusherInfo",itemEntity);
                    startActivity(intent);
                }
                @Override
                public void buyErrorPayView() {
                    payCoinRechargeDialog();
                }
            });
            coinersDialog.show();
            CoinPusherDialogAdapter.getDialogCoinPusherHint(getContext());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AppContext.isHomePage = false;
        AppContext.isShowNotPaid = false;
    }

    @Override
    protected boolean isUmengReportPage() {
        return false;
    }
}