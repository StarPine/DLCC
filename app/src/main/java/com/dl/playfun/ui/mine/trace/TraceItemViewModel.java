package com.dl.playfun.ui.mine.trace;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.entity.TraceEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.mine.trace.list.TraceListViewModel;
import com.dl.playfun.utils.ExceptionReportUtils;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2021/8/3 11:38
 * Description: This is TraceItemViewModel
 */
public class TraceItemViewModel extends MultiItemViewModel<TraceListViewModel> {

    public ObservableField<TraceEntity> itemEntity = new ObservableField<>();
    public ObservableField<Integer> grend = new ObservableField<>();
    public BindingCommand traceOnClickCommand = new BindingCommand(() -> {
        if (!ObjectUtils.isEmpty(grend.get())) {
            if (grend.get().intValue() == 0) {
                //拿到position
                int position = viewModel.observableList.indexOf(TraceItemViewModel.this);
                if(position==-1){
                    return;
                }
                viewModel.uc.clickDelLike.setValue(position);
            } else if (grend.get().intValue() == 1) {
                //拿到position
                int position = viewModel.observableList.indexOf(TraceItemViewModel.this);
                if(position==-1){
                    return;
                }
                if (itemEntity.get().getFollow()) {
                    viewModel.uc.clickDelLike.setValue(position);
                } else {
                    //拿到position
                    viewModel.addLike(position);
                }
            }
        }
    });
    private TraceListViewModel traceListViewModel;
    public BindingCommand UserOnClickCommand = new BindingCommand(() -> {
        if (traceListViewModel == null) {
            return;
        }
        try {
            viewModel.traceViewModel.toUserDetails(itemEntity.get().getId());
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }

    });

    public TraceItemViewModel(@NonNull @NotNull TraceListViewModel viewModel, TraceEntity traceEntity, Integer grend) {
        super(viewModel);
        traceListViewModel = viewModel;
        this.itemEntity.set(traceEntity);
        this.grend.set(grend);
    }

    public int traceLive() {
        int value = grend.get().intValue();
        if (value == 0) {
            return itemEntity.get().getFollow() ? View.GONE : View.VISIBLE;
        } else {
            return View.GONE;
        }
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
            return itemEntity.get().getFollow() ? View.VISIBLE : View.GONE;
        }
        return View.GONE;
    }

    public String getAgeAndConstellation() {
        return String.format(StringUtils.getString(R.string.playfun_mine_age), itemEntity.get().getAge());
    }

    public boolean isEmpty(String obj) {
        return obj == null || obj.length() == 0 || obj.equals("null");
    }

    public String gameUrl(){
        String gameChannel = itemEntity.get().getGameChannel();
        return ConfigManager.getInstance().getGameUrl(gameChannel);
//        return ConfigManager.getInstance().getGameUrl("1642158125");
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
