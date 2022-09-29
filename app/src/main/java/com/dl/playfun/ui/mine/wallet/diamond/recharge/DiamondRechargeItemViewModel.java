package com.dl.playfun.ui.mine.wallet.diamond.recharge;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.Utils;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.entity.GoodsEntity;
import com.dl.playfun.entity.VipPackageItemEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.mine.vipsubscribe.VipSubscribeItemViewModel;
import com.dl.playfun.ui.mine.vipsubscribe.VipSubscribeViewModel;
import com.dl.playfun.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * 特权item
 *
 * @author wulei
 */
public class DiamondRechargeItemViewModel extends MultiItemViewModel<DiamondRechargeViewModel> {

    public ObservableField<GoodsEntity> itemEntity = new ObservableField<>();


    public DiamondRechargeItemViewModel(@NonNull DiamondRechargeViewModel viewModel, GoodsEntity itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.diamondRechargeList.indexOf(DiamondRechargeItemViewModel.this);
            viewModel.itemClick(position,itemEntity.get());
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public String getPriceText(){
        return itemEntity.get().getSymbol() + itemEntity.get().getSalePrice();
    }

    public Drawable getItemBg(){
        int type = itemEntity.get().getType();
        int isRecommend = itemEntity.get().getIsRecommend();
        if (type == 2){
            return Utils.getApp().getResources().getDrawable(R.drawable.bg_diamond_recharge_item_vip);
        }else if (type == 1){
            if (isRecommend == 1){
                return Utils.getApp().getResources().getDrawable(R.drawable.bg_diamond_recharge_item_recommend);
            }
        }
        return Utils.getApp().getResources().getDrawable(R.drawable.bg_diamond_recharge_item_nomal);
    }

    public Drawable getTipsBg(){
        int type = itemEntity.get().getType();
        int isRecommend = itemEntity.get().getIsRecommend();
        if (type == 1){
            if (isRecommend == 1){
                return Utils.getApp().getResources().getDrawable(R.drawable.bg_diamond_recharge_item_tips_recommend);
            }
        }
        return Utils.getApp().getResources().getDrawable(R.drawable.bg_diamond_recharge_item_child);
    }

    public int getFlagBg(){
        int type = itemEntity.get().getType();
        int limit = itemEntity.get().getLimit();
        int isRecommend = itemEntity.get().getIsRecommend();
        if (type == 1){
            if (isRecommend == 1){
                return 1;
            }
        }
        return 0;
    }

    public boolean isShowFlag(){
        int type = itemEntity.get().getType();
        int limit = itemEntity.get().getLimit();
        if (type == 2){
            return true;
        }else {
            if (limit == 1){
                return true;
            }
        }
        return false;
    }


}
