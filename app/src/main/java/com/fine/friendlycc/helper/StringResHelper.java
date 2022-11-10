package com.fine.friendlycc.helper;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.R;
import com.fine.friendlycc.manager.ConfigManager;

public class StringResHelper {

    private static int readSex() {
        return ConfigManager.getInstance().getAppRepository().readUserData().getSex();
    }

    public static String getCommentDialogTitle() {
        if (readSex() == AppConfig.MALE) {
            return StringUtils.getString(R.string.playcc_only_member_comment);
        } else {
            if (ConfigManager.getInstance().getAppRepository().readUserData().getCertification() == 1) {
                return StringUtils.getString(R.string.playcc_dialog_goddess_comment_title);
            } else {
                return StringUtils.getString(R.string.playcc_warn_no_certification);
            }
        }
    }

    public static String getCommentDialogBtnText() {
        if (readSex() == AppConfig.MALE) {
            return StringUtils.getString(R.string.playcc_to_be_member_comment);
        } else {
            if (ConfigManager.getInstance().getAppRepository().readUserData().getCertification() == 1) {
                return StringUtils.getString(R.string.playcc_dialog_goddess_comment_button_text);
            } else {
                return StringUtils.getString(R.string.playcc_dialog_goddess_comment_cert_button_text);
            }
        }
    }
}
