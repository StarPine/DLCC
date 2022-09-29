package com.dl.playfun.helper;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.dl.playfun.R;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.base.BaseFragment;
import com.dl.playfun.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.dl.playfun.widget.dialog.MVDialog;

public class DialogHelper {

    /**
     * 显示非VIP评论对话框
     *
     * @param baseFragment
     */
    public static void showNotVipCommentDialog(BaseFragment baseFragment) {
        int sex = ConfigManager.getInstance().getAppRepository().readUserData().getSex();
        MVDialog.getInstance(baseFragment.getActivity())
                .setContent(StringResHelper.getCommentDialogTitle())
                .setConfirmText(StringResHelper.getCommentDialogBtnText())
                .setConfirmOnlick(dialog -> {
                    dialog.dismiss();
                    if (sex == AppConfig.MALE) {
                        baseFragment.startFragment(VipSubscribeFragment.class.getCanonicalName());
                    } else {
                        baseFragment.startFragment(CertificationFemaleFragment.class.getCanonicalName());
                    }
                })
                .chooseType(MVDialog.TypeEnum.CENTER)
                .show();
    }

    public static void showCheckUserNumberDialog(BaseFragment baseFragment, int number) {
        String title = "";
        String content = "";
        String btn = "";
        String toFragmentName = null;
        if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == AppConfig.MALE) {
            if (number <= 0) {
                title = StringUtils.getString(R.string.playfun_today_browse_useup);
            } else {
                title = String.format(StringUtils.getString(R.string.playfun_today_browse_female_count), number);
            }
            toFragmentName = VipSubscribeFragment.class.getCanonicalName();
            content = String.format(StringUtils.getString(R.string.playfun_not_vip_everyday_browse_home_num), ConfigManager.getInstance().getMaxBrowseHomeNumber());
            btn = StringUtils.getString(R.string.playfun_upgrade_membership);
        } else {
            if (number <= 0) {
                title = StringUtils.getString(R.string.playfun_today_browse_useup);
            } else {
                title = String.format(StringUtils.getString(R.string.playfun_today_browse_male_count), number);
            }
            content = String.format(StringUtils.getString(R.string.playfun_not_goddess_everyday_browse_home_num), ConfigManager.getInstance().getMaxBrowseHomeNumber());
            toFragmentName = CertificationFemaleFragment.class.getCanonicalName();
            btn = StringUtils.getString(R.string.playfun_upgrade_goddess);
        }
        String finalToFragmentName = toFragmentName;
        MVDialog.getInstance(baseFragment.getActivity())
                .setTitele(title)
                .setContent(content)
                .setConfirmText(btn)
                .setConfirmOnlick(dialog -> {
                    dialog.dismiss();
                    baseFragment.startFragment(finalToFragmentName);
                })
                .chooseType(MVDialog.TypeEnum.CENTER)
                .show();
    }
}
