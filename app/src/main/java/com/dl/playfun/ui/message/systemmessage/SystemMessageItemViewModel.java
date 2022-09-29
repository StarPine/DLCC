package com.dl.playfun.ui.message.systemmessage;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.entity.SystemMessageEntity;
import com.dl.playfun.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.dl.playfun.utils.ExceptionReportUtils;
import com.dl.playfun.R;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * @author wulei
 */
public class SystemMessageItemViewModel extends MultiItemViewModel<SystemMessageViewModel> {

    public ObservableField<SystemMessageEntity> itemEntity = new ObservableField<>();
    public BindingCommand itemClickClick = new BindingCommand(() -> {
        try {
            if (itemEntity.get().getType() == 3) {
                viewModel.start(VipSubscribeFragment.class.getCanonicalName());
            }
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand itemLongClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(SystemMessageItemViewModel.this);
            viewModel.uc.clickDelete.postValue(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public SystemMessageItemViewModel(@NonNull SystemMessageViewModel viewModel, SystemMessageEntity messageEntity) {
        super(viewModel);
        this.itemEntity.set(messageEntity);
    }

    public String getMoreText() {
        if (itemEntity.get().getType() == 3) {
            return StringUtils.getString(R.string.playfun_immediately_renewal);
        }
        return "";
    }

    public int getMoreVisible() {
        if (itemEntity.get().getType() == 3) {
            return View.VISIBLE;
        }
        return View.GONE;
    }
}
