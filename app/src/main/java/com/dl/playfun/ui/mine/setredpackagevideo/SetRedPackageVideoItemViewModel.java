package com.dl.playfun.ui.mine.setredpackagevideo;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.entity.AlbumPhotoEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.utils.ExceptionReportUtils;
import com.dl.playfun.R;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
@SuppressLint("SupportAnnotationUsage")
public class SetRedPackageVideoItemViewModel extends MultiItemViewModel<SetRedPackageVideoViewModel> {

    public ObservableField<AlbumPhotoEntity> itemEntity = new ObservableField<>();

    public ObservableField<Integer> moreCount = new ObservableField<>(0);
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.items.indexOf(SetRedPackageVideoItemViewModel.this);
            viewModel.itemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public SetRedPackageVideoItemViewModel(@NonNull SetRedPackageVideoViewModel viewModel, AlbumPhotoEntity itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

    public String getPhotoShowName() {
        if (!ConfigManager.getInstance().isMale()) {
            if (itemEntity.get().getVerificationType() == 0) {
                return StringUtils.getString(R.string.playfun_audit_failed);
            } else if (itemEntity.get().getVerificationType() == -1) {
                return StringUtils.getString(R.string.playfun_photo_in_review);
            } else if (itemEntity.get().getVerificationType() == 1) {
                return StringUtils.getString(R.string.playfun_self);
            }
        }
        return "";
    }


}
