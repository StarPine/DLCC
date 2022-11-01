package com.fine.friendlycc.ui.userdetail.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.widget.picchoose.PicChooseItemEntity;
import com.fine.friendlycc.widget.picchoose.PicChooseView;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentReportUserBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wulei
 */
public class ReportUserFragment extends BaseToolbarFragment<FragmentReportUserBinding, ReportUserViewModel> {

    public static final String ARG_REPORT_USER_ID = "arg_report_user_id";
    public static final String ARG_REPORT_TYPE = "arg_report_type";

    private String type;
    private int userId;

    public static Bundle getStartBundle(String type, int userId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_REPORT_TYPE, type);
        bundle.putInt(ARG_REPORT_USER_ID, userId);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_report_user;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ReportUserViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        ReportUserViewModel reportUserViewModel = ViewModelProviders.of(this, factory).get(ReportUserViewModel.class);
        reportUserViewModel.setType(type);
        reportUserViewModel.setId(userId);
        return reportUserViewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        type = getArguments().getString(ARG_REPORT_TYPE);
        userId = getArguments().getInt(ARG_REPORT_USER_ID);
    }

    @Override
    public void initData() {
        super.initData();
        binding.picChooseView.setShowCamera(false);
        binding.picChooseView.setMaxSelectNum(5);
        binding.picChooseView.setGridCount(3);
        binding.picChooseView.setOnMediaOperateListener(new PicChooseView.OnMediaOperateListener() {
            @Override
            public void onMediaChooseCancel() {

            }

            @Override
            public void onMediaChoosed(List<PicChooseItemEntity> medias) {
                if (medias != null && !medias.isEmpty()) {
                    List<String> filePaths = new ArrayList<>();
                    for (PicChooseItemEntity media : medias) {
                        filePaths.add(media.getSrc());
                    }
                    viewModel.setFilePaths(filePaths);
                }
            }

            @Override
            public void onMediaDelete(List<PicChooseItemEntity> medias, PicChooseItemEntity delMedia) {

            }

        });
    }

}
