package com.dl.playfun.ui.viewmodel;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.entity.AdItemEntity;
import com.dl.playfun.entity.ParkItemEntity;
import com.dl.playfun.entity.TaskAdEntity;
import com.dl.playfun.event.TaskMainTabEvent;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.dl.playfun.ui.mine.wallet.diamond.recharge.DiamondRechargeActivity;
import com.dl.playfun.ui.task.webview.FukuokaViewFragment;
import com.dl.playfun.ui.userdetail.detail.UserDetailFragment;
import com.dl.playfun.ui.webview.WebHomeFragment;
import com.dl.playfun.utils.ChatUtils;
import com.dl.playfun.utils.ExceptionReportUtils;
import com.dl.playfun.utils.SystemDictUtils;
import com.dl.playfun.utils.TimeUtils;

import java.util.List;
import java.util.Objects;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;

/**
 * @author wulei
 */
public class BaseParkItemViewModel extends MultiItemViewModel<BaseParkViewModel> {
    //新增广告轮播类型
    public ObservableField<List<AdItemEntity>> itemBannerEntity = new ObservableField<>();

    public ObservableField<Boolean> collectEnable = new ObservableField<>();
    public ObservableField<ParkItemEntity> itemEntity = new ObservableField<>();
    //单次搭讪成功
    public ObservableField<Boolean> accountCollect = new ObservableField<>();
    //条目的点击事件
    public final BindingCommand itemClick = new BindingCommand(() -> {
        try {
            AppContext.instance().logEvent(AppsFlyerEvent.Nearby_Follow);
            int position = viewModel.observableList.indexOf(BaseParkItemViewModel.this);
            Bundle bundle = UserDetailFragment.getStartBundle(itemEntity.get().getId());
            bundle.putInt(UserDetailFragment.ARG_USER_DETAIL_POSITION,position);
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    //搭讪 or  聊天
    public BindingCommand accostOnClickCommand = new BindingCommand(() -> {
        try {
            //拿到position
            if (itemEntity.get().getIsAccost() == 1) {
                ChatUtils.chatUser(itemEntity.get().getImUserId(), itemEntity.get().getId(), itemEntity.get().getNickname(), viewModel);
                AppContext.instance().logEvent(AppsFlyerEvent.homepage_chat);
            } else {
                try {
                    //男女点击搭讪
                    AppContext.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.greet_male : AppsFlyerEvent.greet_female);
                }catch (Exception ignored){

                }
                int position = viewModel.observableList.indexOf(BaseParkItemViewModel.this);
                viewModel.putAccostFirst(position);
                AppContext.instance().logEvent(AppsFlyerEvent.homepage_accost);
            }

        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public BaseParkItemViewModel(@NonNull BaseParkViewModel viewModel, int sex, ParkItemEntity itemEntity) {
        super(viewModel);
        this.collectEnable.set(itemEntity.getSex() != sex);
        this.itemEntity.set(itemEntity);
    }

    public BaseParkItemViewModel(@NonNull BaseParkViewModel viewModel, List<AdItemEntity> itemBannerEntity) {
        super(viewModel);
        this.itemBannerEntity.set(itemBannerEntity);
    }
    //banner点击
    public BindingCommand<Integer> onBannerClickCommand = new BindingCommand<>(index -> {
        try {
            AdItemEntity adItemEntity = itemBannerEntity.get().get(index);
            int typeAct = adItemEntity.getType();
            if(typeAct!=0){
                switch (typeAct){
                    case 5:
                        viewModel.startActivity(DiamondRechargeActivity.class);
                        break;
                }
            }else{
                if(adItemEntity!=null && adItemEntity.getLink()!=null){
                    Bundle bundle = new Bundle();
                    bundle.putString("link", adItemEntity.getLink());
                    viewModel.start(WebHomeFragment.class.getCanonicalName(), bundle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    public String getDistance() {
        String distance = StringUtils.getString(R.string.playfun_unknown);
        Double d = itemEntity.get().getDistance();
        if (d != null) {
            if (d == -1) {
                distance = StringUtils.getString(R.string.playfun_unknown);
            } else if (d == -2) {
                distance = StringUtils.getString(R.string.playfun_keep);
            } else {
                if (d > 1000) {
                    double df = d / 1000;
                    if (df > 999) {
                        distance = String.format(">%.0fkm", df);
                    } else {
                        distance = String.format("%.1fkm", df);
                    }
                } else {
                    distance = String.format("%sm", d.intValue());
                }
            }
        }
        return distance;
    }

    public Integer getDistanceShow() {
        Integer distance = View.GONE;
        Double d = itemEntity.get().getDistance();
        if (d != null) {
            if (d == -1) {
                distance = View.GONE;
            } else {
                distance = View.VISIBLE;
            }
        }
        String onlineStatus = getOnlineStatus();
        if (onlineStatus == null){
            distance = View.GONE;
        }
        return distance;
    }

    public int onLineColor(ParkItemEntity itemEntity){
        if (itemEntity == null)return -1;
        if (itemEntity.getCallingStatus() == 0){
            if (itemEntity.getIsOnline() == 1) {
                return AppContext.instance().getResources().getColor(R.color.green2);
            }
        }else {
            return AppContext.instance().getResources().getColor(R.color.red_9);
        }
        return AppContext.instance().getResources().getColor(R.color.text_9EA1B0);
    }

    public String getOnlineStatus() {
        String onlineStatus = null;
        if (itemEntity.get().getCallingStatus() == 0){
            if (itemEntity.get().getIsOnline() == -1) {
                onlineStatus = null;
            } else if (itemEntity.get().getIsOnline() == 1) {
                onlineStatus = StringUtils.getString(R.string.playfun_on_line);
            } else if (itemEntity.get().getIsOnline() == 0) {
                onlineStatus = null;
            }
        }else if (itemEntity.get().getCallingStatus() == 1){
            onlineStatus = StringUtils.getString(R.string.playfun_calling);
        }else if (itemEntity.get().getCallingStatus() == 2){
            onlineStatus = StringUtils.getString(R.string.playfun_in_video);
        }

        return onlineStatus;
    }

    public String gameUrl(){
        String gameChannel = itemEntity.get().getGameChannel();
        return ConfigManager.getInstance().getGameUrl(gameChannel);
    }

    public int isRealManVisible() {
        if (itemEntity.get().getIsVip() != 1) {
            if (itemEntity.get().getCertification() == 1) {
                return View.VISIBLE;
            } else {
                return View.GONE;
            }
        }else {
            return View.GONE;
        }
    }

    public int isVipVisible() {
        if (itemEntity.get().getSex() == 1 && itemEntity.get().getIsVip() == 1) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public int isGoddessVisible() {
        if (itemEntity.get().getSex() == 0 && itemEntity.get().getIsVip() == 1) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public String getAgeAndConstellation() {
        return String.format(StringUtils.getString(R.string.playfun_mine_age), itemEntity.get().getAge());
    }

    public boolean isEmpty(String obj) {
        return obj == null || obj.length() == 0 || obj.equals("null");
    }

    public int isPaidAlbum() {
        if (itemEntity.get().getAlbumType() == 2) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public int isAuthAlbum() {
        if (itemEntity.get().getAlbumType() == 3) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    //获取工作植页
    public String getOccupationByIdOnNull(){
        if(itemEntity.get()==null || Objects.requireNonNull(itemEntity.get()).getOccupationId()==null){
            return null;
        }
        int occupationId = Objects.requireNonNull(itemEntity.get()).getOccupationId();
        return SystemDictUtils.getOccupationByIdOnNull(occupationId);
    }

}
