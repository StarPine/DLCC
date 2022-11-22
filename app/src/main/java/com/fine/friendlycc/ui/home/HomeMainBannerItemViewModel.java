package com.fine.friendlycc.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.AdItemBean;
import com.fine.friendlycc.event.TaskMainTabEvent;
import com.fine.friendlycc.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DiamondRechargeActivity;
import com.fine.friendlycc.ui.webview.WebHomeFragment;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;

/**
 * Author: 彭石林
 * Time: 2022/7/25 14:41
 * Description: 首页广告banner图
 */
public class HomeMainBannerItemViewModel extends MultiItemViewModel<HomeMainViewModel> {

    public ObservableField<AdItemBean> itemEntity = new ObservableField<>();

    public HomeMainBannerItemViewModel(@NonNull HomeMainViewModel viewModel,AdItemBean adItemEntity) {
        super(viewModel);
        this.itemEntity.set(adItemEntity);
    }

    public BindingCommand clickBanner = new BindingCommand(() -> {
        try {
            AdItemBean adItemEntity = itemEntity.get();
            int typeAct = adItemEntity.getType();
            if(typeAct!=0){
                //客户端跳转类型 1:会员中心 2：任务中心 3：天天福袋 4：推币机 5:钻石储值
                switch (typeAct){
                    case 1:
                        viewModel.start(VipSubscribeFragment.class.getCanonicalName());
                        break;
                    case 2:
                        RxBus.getDefault().post(new TaskMainTabEvent(false,true));
                        break;
                    case 3:
                        RxBus.getDefault().post(new TaskMainTabEvent(true,true));
                        break;
                    case 4:
                        viewModel.uc.coinPusherRoomEvent.postValue(null);
                        break;
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
}