package com.fine.friendlycc.ui.mine.broadcast.mytrends;

import static com.fine.friendlycc.ui.radio.radiohome.RadioViewModel.RadioRecycleType_New;
import static me.goldze.mvvmhabit.utils.StringUtils.getString;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.bean.CommentBean;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.trenddetail.TrendDetailFragment;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.trenddetail.TrendDetailViewModel;
import com.fine.friendlycc.ui.radio.radiohome.RadioViewModel;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


/**
 * @author wulei
 */
public class CommentItemViewModel extends MultiItemViewModel<BaseViewModel> {
    private final int id;
    private final String type;
    private final boolean isSelf;
    private final boolean isMore;
    public ObservableField<CommentBean> commentBeanObservableField = new ObservableField<>();
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            try {
                if (isMore) {
                    try {
                        if (type.equals(RadioRecycleType_New)){
                            if (viewModel instanceof TrendDetailViewModel) {
                                ApiUitl.isShow = true;
                                ((TrendDetailViewModel) viewModel).newsDetail();
                                return;
                            }
                            Bundle bundle = TrendDetailFragment.getStartBundle(id);
                            viewModel.start(TrendDetailFragment.class.getCanonicalName(), bundle);
                        }
                    } catch (Exception e) {
                        ExceptionReportUtils.report(e);
                    }
                } else if (isSelf && commentBeanObservableField.get().getUser().getId() != ConfigManager.getInstance().getAppRepository().readUserData().getId()) {
                    Map<String, String> data = new HashMap<>();
                    data.put("id", String.valueOf(id));
                    data.put("toUseriD", commentBeanObservableField.get().getUser() == null ? null : String.valueOf(commentBeanObservableField.get().getUser().getId()));
                    data.put("toUserName", commentBeanObservableField.get().getUser() == null ? null : String.valueOf(commentBeanObservableField.get().getUser().getNickname()));
                    data.put("type", type);
                    if (viewModel instanceof MyTrendsViewModel) {
                        ((MyTrendsViewModel) viewModel).uc.clickComment.setValue(data);
                    } else if (viewModel instanceof TrendDetailViewModel) {
                        ((TrendDetailViewModel) viewModel).uc.clickComment.setValue(data);
                    }else if (viewModel instanceof RadioViewModel) {
                        ((RadioViewModel) viewModel).radioUC.clickComment.setValue(data);
                    }
                }
            } catch (Exception e) {
                ExceptionReportUtils.report(e);
            }
        }
    });
    private boolean isShow;//临时新增变量。用来显示隐藏超过5条数据

    public CommentItemViewModel(@NonNull BaseViewModel viewModel, CommentBean commentBean, int id, String type, boolean isSelf, boolean isMore) {
        super(viewModel);
        this.commentBeanObservableField.set(commentBean);
        this.id = id;
        this.type = type;
        this.isSelf = isSelf;
        this.isMore = isMore;
        if (id == -1) {
            isShow = true;
        }
    }

    public String getMessageContent() {
        String content = "";
        if (isShow || isMore) {
            content = String.format("<font color='#666666' size='24'>%s</font>", getString(R.string.playcc_check_more_commment));
        } else if (commentBeanObservableField.get().getTouser() == null) {
            content = String.format("<font color='#A72DFE' size='24'>%s：</font>" + commentBeanObservableField.get().getContent(), commentBeanObservableField.get().getUser().getNickname());
        } else {
            content = String.format("<font color='#A72DFE' size='24' >%s</font> " + StringUtils.getString(R.string.playcc_recovery), commentBeanObservableField.get().getUser().getNickname()) +
                    String.format(" <font color='#A72DFE' size='24'>%s：</font>" + commentBeanObservableField.get().getContent(), commentBeanObservableField.get().getTouser().getNickname());
        }
        return content;
    }

}