package com.dl.playfun.ui.userdetail.theirphotoalbum;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.entity.AlbumPhotoEntity;
import com.dl.playfun.ui.viewmodel.BaseTheirPhotoAlbumViewModel;
import com.dl.playfun.utils.ExceptionReportUtils;
import com.dl.playfun.R;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
@SuppressLint("SupportAnnotationUsage")
public class TheirPhotoAlbumItemViewModel extends ItemViewModel<BaseTheirPhotoAlbumViewModel> {

    public ObservableField<AlbumPhotoEntity> itemEntity = new ObservableField<>();

    public ObservableField<Integer> moreCount = new ObservableField<>(0);
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(TheirPhotoAlbumItemViewModel.this);
            viewModel.itemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    //更多相册点击事件
    public BindingCommand morePhotoOnClickCommand = new BindingCommand(() -> {
        try {
            Bundle bundle = TheirPhotoAlbumFragment.getStartBundle(Integer.parseInt(viewModel.userId.get().toString()));
            viewModel.start(TheirPhotoAlbumFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public TheirPhotoAlbumItemViewModel(@NonNull BaseTheirPhotoAlbumViewModel viewModel, AlbumPhotoEntity itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

    public String getPhotoShowName() {
        String name = "";
        if (itemEntity.get().getIsRedPackage() == 1) {
            if (itemEntity.get().getIsBurn() == 1) {
                if (itemEntity.get().getType() == 1) {
                    name = StringUtils.getString(R.string.playfun_reading_after_burn_red_photo);
                } else if (itemEntity.get().getType() == 2) {
                    name = StringUtils.getString(R.string.playfun_reading_after_burn_red_photo);
                }
                if (itemEntity.get().getBurnStatus() == 1) {
                    name = StringUtils.getString(R.string.playfun_burned);
                }
            } else {
                if (itemEntity.get().getType() == 1) {
                    name = StringUtils.getString(R.string.playfun_redpackage_photo);
                } else if (itemEntity.get().getType() == 2) {
                    name = StringUtils.getString(R.string.playfun_red_package_video);
                }
            }
        } else if (itemEntity.get().getIsBurn() == 1) {
            if (itemEntity.get().getType() == 1) {
                name = StringUtils.getString(R.string.playfun_reading_after_burn_photo);
            } else if (itemEntity.get().getType() == 2) {
                name = StringUtils.getString(R.string.playfun_reading_after_burn_video);
            }
            if (itemEntity.get().getBurnStatus() == 1) {
                name = StringUtils.getString(R.string.playfun_burned);
            }
        }
        return name;
    }

    @SuppressLint("NewApi")
    public int getPhotoTextColor() {
        int color = 0;
        if (itemEntity.get().getBurnStatus() == 1) {
            color = ColorUtils.getColor(R.color.gray_light);
        } else {
            color = ColorUtils.getColor(R.color.red_E944C4);
        }
        return color;
    }

    @SuppressLint("NewApi")
    public Drawable getLeftTagBackground() {
        if (itemEntity.get().getIsRedPackage() == 1) {
            if (itemEntity.get().getBurnStatus() == 1) {
                return ResourceUtils.getDrawable(R.drawable.photo_mark_gray_left);
            } else {
                return ResourceUtils.getDrawable(R.drawable.photo_mark_red_left);
            }
        } else if (itemEntity.get().getIsBurn() == 1) {
            if (itemEntity.get().getBurnStatus() == 1) {
                return ResourceUtils.getDrawable(R.drawable.photo_mark_gray_left);
            } else {
                return ResourceUtils.getDrawable(R.drawable.photo_mark_red_left);
            }
        } else {
            return ResourceUtils.getDrawable(R.drawable.photo_mark_red_left);
        }
    }

    @SuppressLint("NewApi")
    public int getBorderColor() {
        int color = ColorUtils.getColor(R.color.red_E944C4);
        if (itemEntity.get().getIsRedPackage() == 1) {
            if (itemEntity.get().getBurnStatus() == 1) {
                color = ColorUtils.getColor(R.color.gray_light);
            } else {
                color = ColorUtils.getColor(R.color.red_E944C4);
            }
        }
        if (itemEntity.get().getIsBurn() == 1) {
            if (itemEntity.get().getBurnStatus() == 1) {
                color = ColorUtils.getColor(R.color.gray_light);
            } else {
                color = ColorUtils.getColor(R.color.red_E944C4);
            }
        }
        return color;
    }


    public boolean hasBorder() {
        if (itemEntity.get().getIsRedPackage() == 1) {
            return true;
        } else if (itemEntity.get().getIsBurn() == 1) {
            if (itemEntity.get().getBurnStatus() == 1) {
                return true;
            }
            return true;
        }
        return false;
    }


    public float getBorderWith() {
        if (itemEntity.get().getIsRedPackage() == 1) {
            return SizeUtils.dp2px(2);
        } else if (itemEntity.get().getIsBurn() == 1) {
            if (itemEntity.get().getBurnStatus() == 1) {
                return SizeUtils.dp2px(2);
            }
            return SizeUtils.dp2px(2);
        }
        return SizeUtils.dp2px(0);
    }

}
