package com.dl.playfun.ui.vest.second;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.entity.BaseUserBeanEntity;
import com.dl.playfun.entity.BroadcastBeanEntity;
import com.dl.playfun.entity.BroadcastEntity;
import com.dl.playfun.entity.CommentEntity;
import com.dl.playfun.entity.GiveUserBeanEntity;
import com.dl.playfun.entity.NewsEntity;
import com.dl.playfun.entity.ParkItemEntity;
import com.dl.playfun.event.ZoomInPictureEvent;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.mine.broadcast.myall.MyAllBroadcastViewModel;
import com.dl.playfun.ui.mine.broadcast.mytrends.CommentItemViewModel;
import com.dl.playfun.ui.mine.broadcast.mytrends.HeadItemViewModel;
import com.dl.playfun.ui.mine.broadcast.mytrends.ImageItemViewModel;
import com.dl.playfun.ui.mine.broadcast.mytrends.MyTrendsViewModel;
import com.dl.playfun.ui.mine.broadcast.mytrends.trenddetail.TrendDetailFragment;
import com.dl.playfun.ui.mine.broadcast.mytrends.trenddetail.TrendDetailViewModel;
import com.dl.playfun.ui.radio.radiohome.RadioViewModel;
import com.dl.playfun.ui.userdetail.detail.UserDetailFragment;
import com.dl.playfun.ui.userdetail.userdynamic.UserDynamicViewModel;
import com.dl.playfun.utils.ExceptionReportUtils;
import com.dl.playfun.utils.ListUtils;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

import static com.dl.playfun.ui.mine.broadcast.mytrends.HeadItemViewModel.Type_New;
import static com.dl.playfun.ui.radio.radiohome.RadioViewModel.RadioRecycleType_New;

/**
 * @author wulei
 */
public class VestSecondTrendItemViewModel extends MultiItemViewModel<BaseViewModel> {

    public ObservableField<BroadcastEntity> broadcastEntity = new ObservableField<>();

    public VestSecondTrendItemViewModel(@NonNull VestSecondViewModel viewModel, BroadcastEntity broadcastEntity) {
        super(viewModel);
        this.broadcastEntity.set(broadcastEntity);
    }

}
