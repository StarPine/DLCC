package com.fine.friendlycc.ui.message.systemmessagegroup;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.entity.MessageGroupEntity;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.R;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class SystemMessageGroupItemViewModel extends MultiItemViewModel<SystemMessageGroupViewModel> {

    public ObservableField<MessageGroupEntity> itemEntity = new ObservableField<>();
    public ObservableField<Integer> unreadCount = new ObservableField<>(0);
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(SystemMessageGroupItemViewModel.this);
            unreadCount.set(0);
            viewModel.onItemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public SystemMessageGroupItemViewModel(@NonNull SystemMessageGroupViewModel viewModel, MessageGroupEntity itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
        unreadCount.set(itemEntity.getUnreadNumber());
    }

    @BindingAdapter({"icon_src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    public String getTitleName() {
        switch (itemEntity.get().getMold()) {
            case "system":
                return StringUtils.getString(R.string.playfun_system_message);
            case "sign":
                return StringUtils.getString(R.string.playfun_sign_message_title);
            case "give":
                return StringUtils.getString(R.string.playfun_fragment_give_list_title);
            case "evaluate":
                return StringUtils.getString(R.string.playfun_fragment_evaluate_message_title);
            case "comment":
                return StringUtils.getString(R.string.playfun_fragment_custom_chat_input_title);
            case "broadcast":
                return StringUtils.getString(R.string.playfun_fragment_broadcast_message_title);
            case "apply":
                return StringUtils.getString(R.string.playfun_application_erad);
            case "profit":
                return StringUtils.getString(R.string.playfun_profit_message_title);
            default:
                return StringUtils.getString(R.string.playfun_unknown);
        }
    }

    public int getModIcon() {
        switch (itemEntity.get().getMold()) {
            case "system":
                return R.drawable.icon_msg_system;
            case "sign":
                return R.drawable.icon_msg_sign_up;
            case "give":
                return R.drawable.icon_msg_like;
            case "evaluate":
                return R.drawable.icon_msg_evaluate;
            case "comment":
                return R.drawable.icon_msg_comment;
            case "broadcast":
                return R.drawable.icon_msg_radio;
            case "apply":
                return R.drawable.icon_msg_check;
            case "profit":
                return R.drawable.icon_msg_earnings;
            default:
                return R.drawable.default_placeholder_img;
        }
    }

}
