package com.fine.friendlycc.kl.viewmodel;

import android.text.SpannableString;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2021/12/17 17:02
 * Description: This is VideoCallChatingItemViewModel
 */
public class VideoCallChatingItemViewModel extends MultiItemViewModel<VideoCallViewModel> {
    public ObservableField<SpannableString> itemText = new ObservableField<>();
    public ObservableField<String> imgPath = new ObservableField<>();
    public ObservableBoolean sendGiftBag = new ObservableBoolean(false);
    public BindingCommand sendGiftBagOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (sendGiftBag.get()) {
                AppContext.instance().logEvent(AppsFlyerEvent.videocall_public_gift);
                viewModel.uc.callGiftBagAlert.call();
            }
        }
    });

    public VideoCallChatingItemViewModel(@NonNull VideoCallViewModel viewModel, SpannableString stringBuilder, String img, boolean sendGiftBag) {
        super(viewModel);
        this.itemText.set(stringBuilder);
        this.imgPath.set(img);
        this.sendGiftBag.set(sendGiftBag);
    }
}
