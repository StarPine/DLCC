package com.fine.friendlycc.ui.mine.myphotoalbum;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.bean.AlbumPhotoBean;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.viewmodel.BaseMyPhotoAlbumViewModel;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
@SuppressLint("SupportAnnotationUsage")
public class MyPhotoAlbumItemViewModel extends ItemViewModel<BaseMyPhotoAlbumViewModel> {

    public ObservableField<AlbumPhotoBean> itemEntity = new ObservableField<>();

    public ObservableField<Integer> moreCount = new ObservableField<>(0);
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(MyPhotoAlbumItemViewModel.this);
            viewModel.itemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    //条目的点击事件
    public BindingCommand itemLongClick = new BindingCommand(() -> {
//        try {
//            int position = viewModel.observableList.indexOf(MyPhotoAlbumItemViewModel.this);
//            viewModel.deleteAlbumPhoto(itemEntity.get().getId());
//        } catch (Exception e) {
//            ExceptionReportUtils.report(e);
//        }
    });
    //更多相册点击事件
    public BindingCommand morePhotoOnClickCommand = new BindingCommand(() -> {
        try {
            viewModel.start(MyPhotoAlbumFragment.class.getCanonicalName());
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public MyPhotoAlbumItemViewModel(@NonNull BaseMyPhotoAlbumViewModel viewModel, AlbumPhotoBean itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

    public String getPhotoShowName() {
        if (viewModel.getRepository().readUserData().getSex() == 0) {
            if (itemEntity.get().getVerificationType() == 0) {
                return StringUtils.getString(R.string.playcc_audit_failed);
            } else if (itemEntity.get().getVerificationType() == -1) {
                return StringUtils.getString(R.string.playcc_photo_in_review);
            }
        }

        if (itemEntity.get().getIsRedPackage() == 1 && itemEntity.get().getIsBurn() == 1) {
            return StringUtils.getString(R.string.playcc_reading_after_burn_red_photo);
        }
        if (itemEntity.get().getIsRedPackage() == 1) {
            if (itemEntity.get().getType() == 2) {
                return StringUtils.getString(R.string.playcc_red_package_video);
            }
            return StringUtils.getString(R.string.playcc_fragment_certification_female_read_phone);
        }
        if (itemEntity.get().getIsBurn() == 1) {
            if (itemEntity.get().getType() == 2) {
                return StringUtils.getString(R.string.playcc_reading_after_burn_video);
            }
            return StringUtils.getString(R.string.playcc_reading_after_burn_photo);
        }
        return "";
    }

    @SuppressLint("NewApi")
    public int getPhotoTextColor() {
        if (viewModel.getRepository().readUserData().getSex() == 0 && itemEntity.get().getVerificationType() != 1) {
            return ColorUtils.getColor(R.color.white);
        }
        if (itemEntity.get().getIsRedPackage() == 1) {
            return ColorUtils.getColor(R.color.white);
        } else if (itemEntity.get().getIsBurn() == 1) {
            if (itemEntity.get().getBurnStatus() == 1) {
                return ColorUtils.getColor(R.color.white);
            } else {
                return ColorUtils.getColor(R.color.white);
            }
        }
        return ColorUtils.getColor(R.color.gray_light);
    }

    @SuppressLint("NewApi")
    public Drawable getLeftTagBackground() {
        if (viewModel.getRepository().readUserData().getSex() == 0 && itemEntity.get().getVerificationType() != 1) {
            return null;
        }
        if (itemEntity.get().getIsRedPackage() == 1) {
            return ResourceUtils.getDrawable(R.drawable.photo_mark_red_left);
        } else if (itemEntity.get().getIsBurn() == 1) {
            return ResourceUtils.getDrawable(R.drawable.photo_mark_red_left);
        } else {
            return null;
        }
    }

    @SuppressLint("NewApi")
    public Drawable getLeftTagImg() {
        if (viewModel.getRepository().readUserData().getSex() == 0 && itemEntity.get().getVerificationType() != 1) {
            return null;
        }
        if (itemEntity.get().getIsRedPackage() == 1) {
            return ResourceUtils.getDrawable(R.drawable.ic_red_package);
        } else if (itemEntity.get().getIsBurn() == 1) {
            return ResourceUtils.getDrawable(R.drawable.ic_hot);
        } else {
            return null;
        }
    }

    @SuppressLint("NewApi")
    public Drawable getBottomTextBackground() {
        if (viewModel.getRepository().readUserData().getSex() == 0 && itemEntity.get().getVerificationType() != 1) {
            return ResourceUtils.getDrawable(R.drawable.photo_mark_gray_bottom);
        }
        if (itemEntity.get().getIsRedPackage() == 1) {
            return ResourceUtils.getDrawable(R.drawable.photo_mark_red_bottom);
        } else if (itemEntity.get().getIsBurn() == 1) {
            if (itemEntity.get().getBurnStatus() == 1) {
                return ResourceUtils.getDrawable(R.drawable.photo_mark_red_bottom);
            } else {
                return ResourceUtils.getDrawable(R.drawable.photo_mark_red_bottom);
            }
        } else {
            return ResourceUtils.getDrawable(R.drawable.photo_mark_tran_bottom);
        }
    }

    @SuppressLint("NewApi")
    public int getBorderColor() {
        if (itemEntity.get().getIsRedPackage() == 1) {
            return ColorUtils.getColor(R.color.red_E944C4);
        } else if (itemEntity.get().getIsBurn() == 1) {
            return ColorUtils.getColor(R.color.red_E944C4);
        }
        return ColorUtils.getColor(R.color.gray_light);
    }

    public boolean hasBorder() {
        try {
            if (viewModel.getRepository().readUserData().getSex() == 0 && itemEntity.get().getVerificationType() != 1) {
                return false;
            }
            if (itemEntity.get().getIsRedPackage() == 1) {
                return true;
            } else if (itemEntity.get().getIsBurn() == 1) {
                if (itemEntity.get().getBurnStatus() == 1) {
                    return true;
                }
                return true;
            }
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
        return false;
    }

    public float getBorderWith() {
        try {
            if (viewModel.getRepository().readUserData().getSex() == 0 && itemEntity.get().getVerificationType() != 1) {
                return 0;
            }
            if (itemEntity.get().getIsRedPackage() == 1) {
                return SizeUtils.dp2px(2);
            } else if (itemEntity.get().getIsBurn() == 1) {
                if (itemEntity.get().getBurnStatus() == 1) {
                    return SizeUtils.dp2px(2);
                }
                return SizeUtils.dp2px(2);
            }
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
        return SizeUtils.dp2px(0);
    }

}