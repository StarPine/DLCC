package com.dl.playfun.helper;

import android.net.Uri;
import android.os.Bundle;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.dl.playfun.ui.certification.certificationmale.CertificationMaleFragment;
import com.dl.playfun.ui.mine.invitewebdetail.InviteWebDetailFragment;
import com.dl.playfun.ui.mine.myphotoalbum.MyPhotoAlbumFragment;
import com.dl.playfun.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.dl.playfun.ui.mine.webdetail.WebDetailFragment;
import com.dl.playfun.viewmodel.BaseViewModel;

import java.util.Set;

public class JumpHelper {

    public static void jump(BaseViewModel viewModel, String jumpUri) {
        if (StringUtils.isEmpty(jumpUri)) {
            return;
        }
        Uri uri = Uri.parse(jumpUri);
        String scheme = uri.getScheme();
        if ("play_fun".equals(scheme)) {
            String host = uri.getHost();
            if (StringUtils.isEmpty(host)) {
                return;
            }
            Bundle bundle = new Bundle();
            if (!StringUtils.isEmpty(uri.getQuery())) {
                Set<String> keys = uri.getQueryParameterNames();
                for (String key : keys) {
                    String p = uri.getQueryParameter(key);
                    bundle.putString(key, p);
                }
            }
            if ("invitation".equals(host)) {
                viewModel.start(InviteWebDetailFragment.class.getCanonicalName(), InviteWebDetailFragment.getStartBundle(ConfigManager.getInstance().getAppRepository().readApiConfigManagerEntity().getPlayFunApiUrl() + ConfigManager.getInstance().getAppRepository().readUserData().getInviteUrl()));
            } else if ("vip".equals(host)) {
                viewModel.start(VipSubscribeFragment.class.getCanonicalName());
            } else if ("certification".equals(host)) {
                if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() != null) {
                    if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == AppConfig.MALE) {
                        viewModel.start(CertificationMaleFragment.class.getCanonicalName());
                    } else if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == AppConfig.FEMALE) {
                        viewModel.start(CertificationFemaleFragment.class.getCanonicalName());
                    }
                }
            } else if ("photo_album".equals(host)) {
                viewModel.start(MyPhotoAlbumFragment.class.getCanonicalName());
            }
        } else if ("activity".equals(scheme)) {//任意跳转页面
            String host = uri.getHost();
            if (StringUtils.isEmpty(host)) {
                return;
            }
            Bundle bundle = new Bundle();
            if (!StringUtils.isEmpty(uri.getQuery())) {
                Set<String> keys = uri.getQueryParameterNames();
                for (String key : keys) {
                    String p = uri.getQueryParameter(key);
                    bundle.putString(key, p);
                }
            }
            viewModel.start(uri.getHost());
        } else {
            viewModel.start(WebDetailFragment.class.getCanonicalName(), WebDetailFragment.getStartBundle(jumpUri));
        }
    }
}
