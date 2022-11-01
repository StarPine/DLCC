package com.fine.friendlycc.ui.mine.broadcast.mytrends;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.GsonUtils;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.trenddetail.TrendDetailViewModel;
import com.fine.friendlycc.ui.radio.radiohome.RadioViewModel;
import com.fine.friendlycc.ui.userdetail.userdynamic.UserDynamicViewModel;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


/**
 * @author wulei
 */
public class ImageItemViewModel extends MultiItemViewModel<BaseViewModel> {
    public ObservableField<String> path = new ObservableField<>("a");
    public List<String> images = new ArrayList<>();
    private int position = 0;
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            Map<String, String> data = new HashMap<>();
            data.put("position", String.valueOf(position));
            data.put("images", GsonUtils.toJson(images));
            if (viewModel instanceof MyTrendsViewModel) {
                ((MyTrendsViewModel) viewModel).uc.clickImage.setValue(data);
            } else if (viewModel instanceof RadioViewModel) {
                ((RadioViewModel) viewModel).radioUC.clickImage.setValue(data);
            }  else if (viewModel instanceof TrendDetailViewModel) {
                ((TrendDetailViewModel) viewModel).uc.clickImage.setValue(data);
            } else if (viewModel instanceof UserDynamicViewModel) {
                ((UserDynamicViewModel) viewModel).uc.clickImage.setValue(data);
            }
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public ImageItemViewModel(@NonNull BaseViewModel viewModel, String path, int position, List<String> images) {
        super(viewModel);
        this.path.set(path);
        this.images = images;
        this.position = position;
    }

}
