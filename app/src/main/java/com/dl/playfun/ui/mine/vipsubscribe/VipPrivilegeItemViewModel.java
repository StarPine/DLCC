package com.dl.playfun.ui.mine.vipsubscribe;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.android.billingclient.api.SkuDetails;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.entity.VipPackageItemEntity;
import com.dl.playfun.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.Utils;

/**
 * 特权item
 *
 * @author wulei
 */
public class VipPrivilegeItemViewModel extends MultiItemViewModel<VipSubscribeViewModel> {

    public ObservableField<VipPackageItemEntity.PrivilegesBean> itemEntity = new ObservableField<>();


    public VipPrivilegeItemViewModel(@NonNull VipSubscribeViewModel viewModel, VipPackageItemEntity.PrivilegesBean itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

}
