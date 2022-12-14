package com.fine.friendlycc.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.FragmentHomeMainBinding;
import com.fine.friendlycc.bean.CoinPusherDataInfoBean;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.event.CityChangeEvent;
import com.fine.friendlycc.event.LocationChangeEvent;
import com.fine.friendlycc.calling.view.VideoPresetActivity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocationManager;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.ui.coinpusher.CoinPusherGameActivity;
import com.fine.friendlycc.ui.coinpusher.dialog.CoinPusherDialogAdapter;
import com.fine.friendlycc.ui.coinpusher.dialog.CoinPusherRoomListDialog;
import com.fine.friendlycc.ui.dialog.CityChooseDialog;
import com.fine.friendlycc.ui.home.accost.HomeAccostDialog;
import com.fine.friendlycc.ui.home.active.HomeFristTabFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.widget.AppBarStateChangeListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import me.goldze.mvvmhabit.bus.RxBus;

/**
 * @author wulei
 */
public class HomeMainFragment extends BaseFragment<FragmentHomeMainBinding, HomeMainViewModel> {

    private List<ConfigItemBean> citys;

    private CityChooseDialog cityChooseDialog;
    private final BaseFragment[] mFragments = new BaseFragment[2];
    private int currIndex = -1;

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
        initViewPager2();
        Glide.with(getContext())
                .asGif()
                .load(R.drawable.nearby_accost_tip_img)
                .error(R.drawable.nearby_accost_tip_img)
                .placeholder(R.drawable.nearby_accost_tip_img)
                .into(binding.ivAccost);
        CCApplication.instance().logEvent(AppsFlyerEvent.Nearby);
        citys = ConfigManager.getInstance().getAppRepository().readCityConfig();
        ConfigItemBean nearItemEntity = new ConfigItemBean();
        nearItemEntity.setId(-1);
        nearItemEntity.setName(getStringByResId(R.string.playcc_tab_female_1));
        citys.add(0, nearItemEntity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        binding.tvLocationWarn.setOnClickListener(view -> {
            Intent intent = IntentUtils.getLaunchAppDetailsSettingsIntent(mActivity.getPackageName());
            startActivity(intent);
        });

        binding.appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                if (state == State.COLLAPSED) {
                    //????????????
                    binding.imgBarBackground.setVisibility(View.GONE);
                } else if (state == State.EXPANDED) {
                    //????????????
                    binding.imgBarBackground.setVisibility(View.VISIBLE);
                }
            }
        });

        //?????????????????????
        viewModel.getAdListBannber();
        setLocal();

    }

    @SuppressLint("CheckResult")
    private void setLocal() {
        try {

            new RxPermissions(this)
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            viewModel.locationService.set(true);
                            startLocation();
                        } else {
                            viewModel.locationService.set(false);
                            RxBus.getDefault().post(new LocationChangeEvent(LocationChangeEvent.LOCATION_STATUS_FAILED));
                        }
                    });
        } catch (Exception ignored) {

        }
    }

    private void initViewPager2(){
        for (int i = 0; i < mFragments.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            mFragments[i] = new HomeFristTabFragment();
            mFragments[i].setArguments(bundle);
        }

        HomePagerAdapter fragmentAdapter = new HomePagerAdapter(this);
        fragmentAdapter.setFragmentList(mFragments);

        binding.viewPager.setOffscreenPageLimit(1);
        binding.viewPager.setAdapter(fragmentAdapter);
        binding.viewPager.setSaveEnabled(false);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabTitle, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                tab.setText(tabs[position]);

            }
        });
        mediator.attach();

        binding.viewPager.setCurrentItem(0, false);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currIndex = position;
            }
        });

    }

    //????????????
    private void payCoinRechargeDialog(){
        Intent intent = new Intent(mActivity, DialogDiamondRechargeActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.pop_enter_anim, 0);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //???????????????
        viewModel.uc.coinPusherRoomEvent.observe(this,unused->{
            //???????????????????????????
            CoinPusherRoomListDialog coinersDialog = new CoinPusherRoomListDialog(mActivity);
            coinersDialog.setDialogEventListener(new CoinPusherRoomListDialog.DialogEventListener() {
                @Override
                public void startViewing(CoinPusherDataInfoBean itemEntity) {
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

        //????????????
        viewModel.uc.clickRegion.observe(this, unused -> {
            if(cityChooseDialog==null){
                cityChooseDialog = new CityChooseDialog(getContext(),citys,viewModel.cityId.get());
            }
            cityChooseDialog.show();
            cityChooseDialog.setCityChooseDialogListener((dialog1, itemEntity) -> {
                if(itemEntity!=null){

                    viewModel.regionTitle.set(itemEntity.getName());
                }else{
                    viewModel.cityId.set(null);
                    viewModel.regionTitle.set(StringUtils.getString(R.string.playcc_tab_female_1));
                }
                RxBus.getDefault().post(new CityChangeEvent(itemEntity , currIndex));
//                binding.refreshLayout.autoRefresh();
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

        //tab ????????????
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
                        CCApplication.instance().logEvent(AppsFlyerEvent.accost_close);
                        dialog.dismiss();
                    }
                });
                homeAccostDialog.show();
            }
        });
        //????????????
        viewModel.uc.sendAccostFirstError.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                CCApplication.instance().logEvent(AppsFlyerEvent.Top_up);
                toRecharge();
            }
        });
        //??????????????????
        viewModel.uc.loadLoteAnime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer position) {
//                LinearLayoutManager layoutManager = (LinearLayoutManager) binding.rcvLayout.getLayoutManager();
//                final View child = layoutManager.findViewByPosition(position);
//                if (child != null) {
//                    LottieAnimationView itemLottie = child.findViewById(R.id.item_lottie);
//                    if (itemLottie != null) {
//                        itemLottie.setImageAssetsFolder("images/");
//                        itemLottie.addAnimatorListener(new AnimatorListenerAdapter() {
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                itemLottie.removeAnimatorListener(this);
//                                itemLottie.setVisibility(View.GONE);
//                            }
//                        });
//                        if (!itemLottie.isAnimating()) {
//                            itemLottie.setVisibility(View.VISIBLE);
//                            itemLottie.setAnimation(R.raw.accost_animation);
//                            itemLottie.playAnimation();
//                        }
//                    }
//                }
            }
        });
    }

    /**
     * ?????????
     */
    private void toRecharge() {
        Intent intent = new Intent(mActivity, DialogDiamondRechargeActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.pop_enter_anim, 0);
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
                //????????????????????????????????????????????? RxBus.getDefault().post(new LocationChangeEvent());
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
        CCApplication.isHomePage = true;
        CCApplication.isShowNotPaid = true;
        //30?????????????????????
        if(AppConfig.CoinPusherGameNotPushed){
            AppConfig.CoinPusherGameNotPushed = false;
            //???????????????????????????
            CoinPusherRoomListDialog coinersDialog = new CoinPusherRoomListDialog(mActivity);
            coinersDialog.setDialogEventListener(new CoinPusherRoomListDialog.DialogEventListener() {
                @Override
                public void startViewing(CoinPusherDataInfoBean itemEntity) {
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
        CCApplication.isHomePage = false;
        CCApplication.isShowNotPaid = false;
    }

    @Override
    protected boolean isUmengReportPage() {
        return false;
    }
}