package com.fine.friendlycc.ui.userdetail.photobrowse;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.AlbumPhotoBean;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class PhotoBrowseItemViewModel extends ItemViewModel<PhotoBrowseViewModel> {
    public ObservableField<AlbumPhotoBean> itemEntity = new ObservableField<>();
    public ObservableField<Integer> redPackagePrice = new ObservableField<>();
    public ObservableField<Integer> playStatus = new ObservableField<>(0);
    public BindingCommand itemClick = new BindingCommand(() -> {
        viewModel.onItemClick();
    });
    public BindingCommand burnedCommand = new BindingCommand(() -> {
        try {
            int position = viewModel.items.indexOf(PhotoBrowseItemViewModel.this);
            viewModel.itemPhotoBurned(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand videoCompletionCommand = new BindingCommand(() -> {
        try {
            int position = viewModel.items.indexOf(PhotoBrowseItemViewModel.this);
            viewModel.itemVideoPlayCompletion(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand clickGiveRedPackage = new BindingCommand(() -> {
        try {
            int position = viewModel.items.indexOf(PhotoBrowseItemViewModel.this);
            viewModel.payRedPackage(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public PhotoBrowseItemViewModel(@NonNull PhotoBrowseViewModel viewModel, AlbumPhotoBean itemEntity, Integer redPackagePrice) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
        this.redPackagePrice.set(redPackagePrice);
    }

    public Integer getPosition() {
        int position = 0;
        if (viewModel instanceof PhotoBrowseViewModel) {
            position = viewModel.items.indexOf(PhotoBrowseItemViewModel.this);
        }
        return position;
    }
}