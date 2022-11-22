package com.fine.friendlycc.ui.mine.vipsubscribe;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.VipPackageItemBean;

import me.goldze.mvvmhabit.base.MultiItemViewModel;

/**
 * 特权item
 *
 * @author wulei
 */
public class VipPrivilegeItemViewModel extends MultiItemViewModel<VipSubscribeViewModel> {

    public ObservableField<VipPackageItemBean.PrivilegesBean> itemEntity = new ObservableField<>();


    public VipPrivilegeItemViewModel(@NonNull VipSubscribeViewModel viewModel, VipPackageItemBean.PrivilegesBean itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

}