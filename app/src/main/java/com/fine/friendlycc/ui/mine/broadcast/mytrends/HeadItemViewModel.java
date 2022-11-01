package com.fine.friendlycc.ui.mine.broadcast.mytrends;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.ui.mine.broadcast.mytrends.givelist.GiveListFragment;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


/**
 * @author wulei
 */
public class HeadItemViewModel extends MultiItemViewModel<BaseViewModel> {
    public static final String Type_New = "new";
    public static final String Type_Topical = "topical";

    public ObservableField<String> path = new ObservableField<>("");
    public ObservableField<Integer> sex = new ObservableField<>(0);
    public ObservableField<Integer> userId = new ObservableField<>();
    public ObservableField<Integer> moreCount = new ObservableField<>();
    public ObservableField<String> type = new ObservableField<>("");
    public ObservableField<Integer> id = new ObservableField<>();
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            Bundle bundle = UserDetailFragment.getStartBundle(userId.get());
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    //查看点赞列表
    public BindingCommand morePhotoOnClickCommand = new BindingCommand(() -> {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id.get());
            bundle.putString("type", type.get());
            viewModel.start(GiveListFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public HeadItemViewModel(@NonNull BaseViewModel viewModel, String path, Integer userId, Integer sex, Integer moreCount, String type, Integer id) {
        super(viewModel);
        this.sex.set(sex);
        this.path.set(path);
        this.userId.set(userId);
        this.moreCount.set(moreCount);
        this.type.set(type);
        this.id.set(id);
    }

}
