package com.fine.friendlycc.ui.mine.trace.man;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.bean.TraceBean;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2021/8/4 14:06
 * Description: This is TraceManItemViewModel
 */
public class TraceManItemViewModel extends MultiItemViewModel<TraeManViewModel> {

    public Integer mhWidth = 90;
    public Integer mhHeight = 40;

    public ObservableField<TraceBean> itemEntity = new ObservableField<>();
    public TraeManViewModel traeManViewModel;
    public Integer isPlay = -1;
    public BindingCommand AlertVipOnClickCommand = new BindingCommand(() -> {
        if (traeManViewModel == null) {
            return;
        }
        if (isPlay == 1) {
            try {
                viewModel.toUserDetails(itemEntity.get().getId());
            } catch (Exception e) {
                ExceptionReportUtils.report(e);
            }
            //viewModel.toUserDetails(itemEntity.get().getId());
            return;
        }
        traeManViewModel.uc.clickVip.call();
    });

    public TraceManItemViewModel(@NonNull @NotNull TraeManViewModel viewModel, TraceBean itemEntity, Integer isPlay) {
        super(viewModel);
        this.traeManViewModel = viewModel;
        this.itemEntity.set(itemEntity);
        this.isPlay = isPlay;
    }

    public String getTime() {
        String time = itemEntity.get().getTime();
        String data = StringUtils.getString(R.string.playcc_mine_trace_time_exp);
        try {
            Date oldDate = Utils.format.parse(time);
            Date oneDay = new Date();
            long oneHour = oneDay.getTime() / (1000 * 60 * 60);
            long oldHour = oldDate.getTime() / (1000 * 60 * 60);
            long hour = oneHour - oldHour;
            if (hour <= 1) {
                return data;
            } else if (1 < hour && hour <= 2) {
                return StringUtils.getString(R.string.playcc_mine_trace_time_exp2);
            } else if (2 < hour && hour <= 3) {
                return StringUtils.getString(R.string.playcc_mine_trace_time_exp3);
            } else if (3 < hour && hour <= 4) {
                return StringUtils.getString(R.string.playcc_mine_trace_time_exp4);
            } else if (4 < hour && hour <= 5) {
                return StringUtils.getString(R.string.playcc_mine_trace_time_exp5);
            } else if (5 < hour && hour <= 6) {
                return StringUtils.getString(R.string.playcc_mine_trace_time_exp6);
            } else if (6 < hour && hour <= 24) {
                return StringUtils.getString(R.string.playcc_mine_trace_time_exp7);
            } else if (24 < hour && hour <= 48) {
                return StringUtils.getString(R.string.playcc_mine_trace_time_exp8);
            } else if (48 < hour) {
                return StringUtils.getString(R.string.playcc_mine_trace_time_exp9);
            }
        } catch (Exception e) {
            return data;
        }
        return data;

    }

    public boolean getIsVip() {
        return isPlay.intValue() == 1;
    }

    public String getText() {
        return String.format(StringUtils.getString(R.string.playcc_mine_trace_man_hint), itemEntity.get().getNumber());
    }

    public String getAgeAndConstellation() {
        return String.format(StringUtils.getString(R.string.playcc_mine_age), itemEntity.get().getAge());
    }

    public Drawable getVipGodsImg(TraceBean traceEntity) {
        if (traceEntity != null) {
            if (traceEntity.getIsVip() == 1) {
                if (traceEntity.getSex() == 1) {
                    return CCApplication.instance().getDrawable(R.drawable.ic_vip);
                } else {//女生
                    return CCApplication.instance().getDrawable(R.drawable.ic_good_goddess);
                }
            } else {
                if (traceEntity.getCertification() != null && traceEntity.getCertification() == 1) {
                    return CCApplication.instance().getDrawable(R.drawable.ic_real_people);
                }
            }
        }

        return null;
    }

    public String gameUrl(String gameChannel) {
        return ConfigManager.getInstance().getGameUrl(gameChannel);
    }

    public boolean isEmpty(String obj){
        return obj == null || obj.equals("");
    }
}