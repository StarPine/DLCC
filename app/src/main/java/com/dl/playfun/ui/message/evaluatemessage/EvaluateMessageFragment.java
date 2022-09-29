package com.dl.playfun.ui.message.evaluatemessage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.app.Injection;
import com.dl.playfun.entity.EvaluateEntity;
import com.dl.playfun.entity.EvaluateItemEntity;
import com.dl.playfun.entity.EvaluateObjEntity;
import com.dl.playfun.ui.base.BaseRefreshToolbarFragment;
import com.dl.playfun.ui.dialog.CommitEvaluateDialog;
import com.dl.playfun.ui.dialog.MyEvaluateDialog;
import com.dl.playfun.utils.PictureSelectorUtil;
import com.dl.playfun.widget.dialog.MVDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentEvaluateMessageBinding;
import com.dl.playfun.ui.userdetail.report.ReportUserFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wulei
 */
public class EvaluateMessageFragment extends BaseRefreshToolbarFragment<FragmentEvaluateMessageBinding, EvaluateMessageViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_evaluate_message;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public EvaluateMessageViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(EvaluateMessageViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        viewModel.uc.clickEvaluate.observe(this, map -> {
            List<EvaluateObjEntity> list = null;
            int sex = (int) map.get("sex");
            if (sex == 1) {
                list = Injection.provideDemoRepository().readMaleEvaluateConfig();
            } else {
                list = Injection.provideDemoRepository().readFemaleEvaluateConfig();
            }
            List<EvaluateItemEntity> items = new ArrayList<>();
            for (EvaluateObjEntity configEntity : list) {
                EvaluateItemEntity evaluateItemEntity = new EvaluateItemEntity(configEntity.getId(), configEntity.getName(), configEntity.getType() == 1);
                items.add(evaluateItemEntity);
                List<EvaluateEntity> evaluateEntities = (List<EvaluateEntity>) map.get("evaluates");
                for (EvaluateEntity evaluateEntity : evaluateEntities) {
                    if (configEntity.getId() == evaluateEntity.getTagId()) {
                        evaluateItemEntity.setNumber(evaluateEntity.getNumber());
                    }
                }
            }
            MyEvaluateDialog dialog = new MyEvaluateDialog(sex == 1 ? MyEvaluateDialog.TYPE_USER_MALE : MyEvaluateDialog.TYPE_USER_FEMALE, items);
            dialog.setEvaluateDialogListener(new MyEvaluateDialog.EvaluateDialogListener() {
                @Override
                public void onEvaluateClick(MyEvaluateDialog dialog) {
                    dialog.dismiss();
                    CommitEvaluateDialog commitEvaluateDialog = new CommitEvaluateDialog(items);
                    commitEvaluateDialog.show(getChildFragmentManager(), CommitEvaluateDialog.class.getCanonicalName());
                    commitEvaluateDialog.setCommitEvaluateDialogListener((dialog12, entity) -> {
                        dialog12.dismiss();
//                        if (entity != null) {
//                            if (entity.getTagId() == 5 || entity.getTagId() == 6) {
//
//                            } else {
//                                int userId = (int) map.get("userId");
//                                viewModel.commitUserEvaluate(userId, entity.getTagId(), null);
//                            }
//                        }

                        if (entity != null) {
                            int userId = (int) map.get("userId");
                            if (entity.isNegativeEvaluate()) {
                                MVDialog.getInstance(mActivity)
                                        .setContent(getString(R.string.playfun_provide_screenshot))
                                        .setConfirmText(getString(R.string.playfun_choose_photo))
                                        .setConfirmOnlick(dialog1 -> {
                                            dialog1.dismiss();
                                            PictureSelectorUtil.selectImage(mActivity, false, 1, new OnResultCallbackListener<LocalMedia>() {
                                                @Override
                                                public void onResult(List<LocalMedia> result) {
                                                    if (!result.isEmpty()) {
                                                        viewModel.commitNegativeEvaluate(userId, entity.getTagId(), result.get(0).getCompressPath());
                                                    }
                                                }

                                                @Override
                                                public void onCancel() {
                                                }
                                            });
                                        })
                                        .chooseType(MVDialog.TypeEnum.CENTERWARNED)
                                        .show();
                            } else {
                                viewModel.commitUserEvaluate(userId, entity.getTagId(), null);
                            }
                        }
                    });
                }

                @Override
                public void onAnonymousReportClick(MyEvaluateDialog dialog) {
                    dialog.dismiss();
                    int userId = (int) map.get("userId");
                    Bundle bundle = ReportUserFragment.getStartBundle("home", userId);
                    ReportUserFragment fragment = new ReportUserFragment();
                    fragment.setArguments(bundle);
                    start(fragment);
                }
            });
            dialog.show(getChildFragmentManager(), MyEvaluateDialog.class.getCanonicalName());
        });
        viewModel.uc.clickAppealEvaluate.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                MVDialog.getInstance(mActivity)
                        .setContent(getString(R.string.playfun_appeal_review))
                        .setConfirmText(StringUtils.getString(R.string.playfun_confirm))
                        .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                            @Override
                            public void confirm(MVDialog dialog) {
                                dialog.dismiss();
                                viewModel.commitEvaluateAppeal(integer);
                            }
                        })
                        .chooseType(MVDialog.TypeEnum.CENTER)
                        .show();
            }
        });
        viewModel.uc.clickDelete.observe(this, integer -> MVDialog.getInstance(mActivity)
                .setContent(getString(R.string.playfun_comfirm_delete_message))
                .chooseType(MVDialog.TypeEnum.CENTERWARNED)
                .setConfirmOnlick(dialog -> {
                    dialog.dismiss();
                    viewModel.deleteMessage(integer);
                })
                .show());
    }
}
