package com.fine.friendlycc.ui.mine.trtctest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.fine.friendlycc.entity.TokenEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.google.firebase.messaging.FirebaseMessaging;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentTrtcTestBinding;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import me.goldze.mvvmhabit.utils.KLog;

/**
 * @author wulei
 */
public class TrtcTestFragment extends BaseToolbarFragment<FragmentTrtcTestBinding, TrtcTestViewModel> {

    private StringBuilder sb = null;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_trtc_test;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    binding.pushToken.setText(task.getResult());
                });

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TokenEntity tokenEntity = ConfigManager.getInstance().getAppRepository().readLoginInfo();
                sb = new StringBuilder();
                TRTCCloud.sharedInstance(mActivity).setListener(new TRTCCloudListener() {
                    @Override
                    public void onSpeedTest(TRTCCloudDef.TRTCSpeedTestResult trtcSpeedTestResult, int completedCount, int totalCount) {
                        String log = String.format("速度测试(第%d次/共%d次) %s", completedCount, totalCount, trtcSpeedTestResult);
                        KLog.d("TRTCTest", log);
                        sb.append(log).append("\n\n");
                        binding.tvLog.setText(sb.toString());

                    }
                });
                //TRTCCloud.sharedInstance(mActivity).startSpeedTest(AppConfig.IM_APP_KEY, tokenEntity.getUserID(), tokenEntity.getUserSig());
            }
        });
    }
}
