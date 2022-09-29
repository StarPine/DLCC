package com.dl.playfun.ui.mine.likelist;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.entity.TraceEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.R;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2021/8/4 10:55
 * Description: This is LikeItemViewModel
 */
public class LikeItemViewModel extends MultiItemViewModel<LikeListViewModel> {

    private final LikeListViewModel likeListViewModel;
    public ObservableField<TraceEntity> itemEntity = new ObservableField<>();
    public ObservableField<Integer> grend = new ObservableField<>();
    public BindingCommand traceOnClickCommand = new BindingCommand(() -> {
        if (!ObjectUtils.isEmpty(grend.get())) {
            if (grend.get().intValue() == 0) {
                //拿到position
                int position = viewModel.observableList.indexOf(LikeItemViewModel.this);
                viewModel.uc.clickDelLike.setValue(position);
            } else if (grend.get().intValue() == 1) {
                //拿到position
                int position = viewModel.observableList.indexOf(LikeItemViewModel.this);
                if (itemEntity.get().getFollow()) {
                    viewModel.uc.clickDelLike.setValue(position);
                } else {
                    //拿到position
                    viewModel.addLike(position);
                }
            }
        }
    });
    public BindingCommand UserOnClickCommand = new BindingCommand(() -> {
        viewModel.toUserDetails(itemEntity.get().getId());
    });

    public LikeItemViewModel(@NonNull @NotNull LikeListViewModel viewModel, TraceEntity traceEntity, Integer grend) {
        super(viewModel);
        likeListViewModel = viewModel;
        this.itemEntity.set(traceEntity);
        this.grend.set(grend);
    }

    public int traceLive() {
        if (!ObjectUtils.isEmpty(grend.get())) {
            int value = grend.get().intValue();
            if (value == 0) {
                return itemEntity.get().getFollow() ? View.GONE : View.VISIBLE;
            } else {
                return View.GONE;
            }
        }
        return View.GONE;
    }

    public int traceLive2() {
        if (!ObjectUtils.isEmpty(grend.get())) {
            int value = grend.get().intValue();
            if (value == 1) {
                return itemEntity.get().getFollow() ? View.GONE : View.VISIBLE;
            } else {
                return View.GONE;
            }
        }
        return View.GONE;
    }

    public int traceLive3() {
        if (!ObjectUtils.isEmpty(grend.get())) {
            int value = grend.get().intValue();
            if (value == 0) {
                return itemEntity.get().getFollow() ? View.VISIBLE : View.GONE;
            } else {
                return View.GONE;
            }
        }
        return View.GONE;
    }

    public String gameUrl(){
        String gameChannel = itemEntity.get().getGameChannel();
        return ConfigManager.getInstance().getGameUrl(gameChannel);
    }

    public String getAgeAndConstellation() {
        return String.format(StringUtils.getString(R.string.playfun_age_and_constellation_only_age), itemEntity.get().getAge());
    }

    public boolean isEmpty(String obj) {
        return obj == null || obj.length() == 0 || obj.equals("null");
    }

    public int isRealManVisible() {
        if (itemEntity.get().getIsVip() != 1) {
            if (itemEntity.get().getCertification() == 1) {
                return View.VISIBLE;
            } else {
                return View.GONE;
            }
        }else {
            return View.GONE;
        }
    }

    public int isVipVisible() {
        if (itemEntity.get().getSex() != null && itemEntity.get().getSex() == 1 && itemEntity.get().getIsVip() == 1) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public int isGoddessVisible() {
        if (itemEntity.get().getSex() == 0 && itemEntity.get().getIsVip() == 1) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }
}
