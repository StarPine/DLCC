package com.fine.friendlycc.ui.mine.level;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.bean.LevelPageInfoBean;
import com.fine.friendlycc.bean.LevelSelectInfoBean;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2022/6/18 18:17
 * Description: This is LevelEquityItemViewModel
 */
public class LevelEquityItemViewModel extends MultiItemViewModel<LevelEquityViewModel> {

    public ObservableBoolean checkCurrent = new ObservableBoolean(false);
    public ObservableField<LevelSelectInfoBean.LevelInfo> levelInfoData = new ObservableField<>();

    public BindingCommand itemClick = new BindingCommand(() -> {
        int idx = viewModel.observableListTitle.indexOf(LevelEquityItemViewModel.this);
        viewModel.titleRcvItemClick(idx, true);
    });

    public LevelEquityItemViewModel(@NonNull LevelEquityViewModel viewModel, boolean check, LevelSelectInfoBean.LevelInfo levelInfo) {
        super(viewModel);
        checkCurrent.set(check);
        levelInfoData.set(levelInfo);
    }

    /**
     * @return java.lang.String
     * @Desc TODO(获取文字模板)
     * @author 彭石林
     * @parame []
     * @Date 2022/6/21
     */
    public String getChatMessage() {
        LevelSelectInfoBean.LevelInfo levelInfo = levelInfoData.get();
        if (!ObjectUtils.isEmpty(levelInfo)) {
            LevelPageInfoBean.CoinsRangeInfo chatCoinsRange = levelInfo.getChatCoinsRange();
            if (chatCoinsRange != null) {
                String chatMessage = StringUtils.getString(R.string.fragment_level_text_chat);
                return String.format(chatMessage, chatCoinsRange.getFrom(), chatCoinsRange.getTo());
            }
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @Desc TODO(获取文字模板)
     * @author 彭石林
     * @parame []
     * @Date 2022/6/21
     */
    public String getAudioMessage() {
        LevelSelectInfoBean.LevelInfo levelInfo = levelInfoData.get();
        if (!ObjectUtils.isEmpty(levelInfo)) {
            LevelPageInfoBean.CoinsRangeInfo voiceCoinsRange = levelInfo.getVoiceCoinsRange();
            if (voiceCoinsRange != null) {
                String chatMessage = StringUtils.getString(R.string.fragment_level_text_audio);
                return String.format(chatMessage, voiceCoinsRange.getFrom(), voiceCoinsRange.getTo());
            }
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @Desc TODO(获取文字模板)
     * @author 彭石林
     * @parame []
     * @Date 2022/6/21
     */
    public String getVideoMessage() {
        LevelSelectInfoBean.LevelInfo levelInfo = levelInfoData.get();
        if (!ObjectUtils.isEmpty(levelInfo)) {
            LevelPageInfoBean.CoinsRangeInfo videoCoinsRange = levelInfo.getVideoCoinsRange();
            if (videoCoinsRange != null) {
                String chatMessage = StringUtils.getString(R.string.fragment_level_text_video);
                return String.format(chatMessage, videoCoinsRange.getFrom(), videoCoinsRange.getTo());
            }
        }
        return null;
    }

}