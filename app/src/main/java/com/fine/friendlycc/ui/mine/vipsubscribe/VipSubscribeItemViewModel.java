package com.fine.friendlycc.ui.mine.vipsubscribe;

import android.text.Html;
import android.text.Spanned;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.android.billingclient.api.SkuDetails;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.entity.VipPackageItemEntity;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.R;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * 会员充值
 *
 * @author wulei
 */
public class VipSubscribeItemViewModel extends MultiItemViewModel<VipSubscribeViewModel> {

    public ObservableField<VipPackageItemEntity> itemEntity = new ObservableField<>();
    public ObservableField<SkuDetails> skuDetails = new ObservableField<>();

    public String num_ling = "0";
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.vipSubscribeList.indexOf(VipSubscribeItemViewModel.this);
            viewModel.itemClick(position,itemEntity.get());
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public VipSubscribeItemViewModel(@NonNull VipSubscribeViewModel viewModel, VipPackageItemEntity itemEntity, SkuDetails skuDetails) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
        this.skuDetails.set(skuDetails);
    }

    //获取标题文字
    public String getTitleText(){
        VipPackageItemEntity entity = itemEntity.get();
        if (StringUtils.isEmpty(entity.getGoodsTab())) {
            return "";
        } else {
            return entity.getGoodsTab();
        }
    }
    //控制标题是否隐藏
    public int getTitleShow(){
        VipPackageItemEntity entity = itemEntity.get();
        if (!StringUtils.isEmpty(entity.getDiscountLabel())) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }
    //推荐角标显示隐藏
    public Integer isRecommendShow(){
        VipPackageItemEntity entity = itemEntity.get();
        if(entity.getIsRecommend()!=null && entity.getIsRecommend()==1){
            return View.VISIBLE;
        }
        return View.GONE;
    }

    //获取原价
    public Spanned getOriginalPrice() {
        VipPackageItemEntity entity = itemEntity.get();
        if (entity != null && entity.getOriginalPrice() != null) {
            return Html.fromHtml(String.format(StringUtils.getString(R.string.playfun_vip_alert_gold_tag, entity.getOriginalPrice())));
        }
        return null;
    }

    public String getDayPrice() {
        VipPackageItemEntity entity = itemEntity.get();
        if (entity != null && entity.getDiscountLabel() != null) {
            return entity.getDiscountLabel();
        }
        return null;
    }

}
