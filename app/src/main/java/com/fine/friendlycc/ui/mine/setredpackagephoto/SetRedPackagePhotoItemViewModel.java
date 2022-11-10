package com.fine.friendlycc.ui.mine.setredpackagephoto;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.R;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
@SuppressLint("SupportAnnotationUsage")
public class SetRedPackagePhotoItemViewModel extends MultiItemViewModel<SetRedPackagePhotoViewModel> {

    public ObservableField<AlbumPhotoEntity> itemEntity = new ObservableField<>();

    public ObservableField<Integer> moreCount = new ObservableField<>(0);
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.items.indexOf(SetRedPackagePhotoItemViewModel.this);
            viewModel.itemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public SetRedPackagePhotoItemViewModel(@NonNull SetRedPackagePhotoViewModel viewModel, AlbumPhotoEntity itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

    public String getPhotoShowName() {
        if (!ConfigManager.getInstance().isMale()) {
            if (itemEntity.get().getVerificationType() == 0) {
                return StringUtils.getString(R.string.playcc_audit_failed);
            } else if (itemEntity.get().getVerificationType() == -1) {
                return StringUtils.getString(R.string.playcc_photo_in_review);
            } else if (itemEntity.get().getVerificationType() == 1) {
                return StringUtils.getString(R.string.playcc_self);
            }
        }
        return "";
    }


}
