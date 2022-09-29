package com.dl.playfun.ui.userdetail.userdynamic;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.helper.StringResHelper;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.base.BaseRefreshToolbarFragment;
import com.dl.playfun.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.dl.playfun.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.dl.playfun.ui.userdetail.report.ReportUserFragment;
import com.dl.playfun.utils.PictureSelectorUtil;
import com.dl.playfun.widget.dialog.MVDialog;
import com.google.gson.reflect.TypeToken;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentUserDynamicBinding;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 用户动态
 *
 * @author wulei
 */
public class UserDynamicFragment extends BaseRefreshToolbarFragment<FragmentUserDynamicBinding, UserDynamicViewModel> {
    public static final String ARG_USER_DYNAMIC_USER_ID = "arg_user_dynamic_user_id";
    public static final String ARG_USER_DYNAMIC_USER_SEX = "arg_user_dynamic_user_sex";

    private int userId;
    private int sex;
    private EasyPopup mCirclePop;

    public static Bundle getStartBundle(int userId, int sex) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_DYNAMIC_USER_ID, userId);
        bundle.putInt(ARG_USER_DYNAMIC_USER_SEX, sex);
        return bundle;
    }

    @Override
    public void initParam() {
        super.initParam();
        userId = getArguments().getInt(ARG_USER_DYNAMIC_USER_ID, 0);
        sex = getArguments().getInt(ARG_USER_DYNAMIC_USER_SEX, 0);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_user_dynamic;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public UserDynamicViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        UserDynamicViewModel viewModel = ViewModelProviders.of(this, factory).get(UserDynamicViewModel.class);
        viewModel.id.set(userId);
        if (sex == 0) {
            viewModel.titleText.set(getString(R.string.playfun_her_trend));
        } else if (sex == 1) {
            viewModel.titleText.set(getString(R.string.playfun_other_trend));
        }
        return viewModel;
    }

    @Override
    public void initData() {
        super.initData();
//        binding.setAdapter(new UserDynamicRecyclerViewAdapter());
    }

    //    @Override
//    public void initViewObservable() {
//        super.initViewObservable();
//        viewModel.uc.clickMore.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
//                String type = ((Map<String, String>) o).get("type");
//                Integer broadcastId = Integer.valueOf(((Map<String, String>) o).get("broadcastId"));
//                mCirclePop = EasyPopup.create()
//                        .setContentView(UserDynamicFragment.this.getContext(), R.layout.more_item)
////                        .setAnimationStyle(R.style.RightPopAnim)
//                        //是否允许点击PopupWindow之外的地方消失
//                        .setFocusAndOutsideEnable(true)
//                        .setDimValue(0)
//                        .setWidth(350)
//                        .apply();
//
//                LinearLayoutManager layoutManager = (LinearLayoutManager) binding.rcyTrend.getLayoutManager();
//                final View child = layoutManager.findViewByPosition(position);
//                if (child != null) {
//                    mCirclePop.showAtAnchorView(child.findViewById(R.id.iv_more), YGravity.BELOW, XGravity.ALIGN_RIGHT, 0, 0);
//                }
//                TextView stop = mCirclePop.findViewById(R.id.tv_stop);
//                mCirclePop.findViewById(R.id.tv_detele).setVisibility(View.GONE);
//                stop.setText(R.string.report_user_title);
//                stop.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString(ARG_REPORT_TYPE, "broadcast");
//                        bundle.putInt(ARG_REPORT_USER_ID, broadcastId);
//                        startContainerActivity(ReportUserFragment.class.getCanonicalName(), bundle);
//                        mCirclePop.dismiss();
//                    }
//                });
//            }
//        });
//
//        viewModel.uc.clickLike.observe(this, new Observer() {
//            @Override
//            public void onChanged(Object o) {
//                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
//                if (viewModel.observableList.get(position).newsEntityObservableField.get().getIsGive() == 0) {
//                    viewModel.newsGive(position);
//                } else {
//                    ToastUtils.showShort(R.string.already);
//                }
//            }
//        });
//        viewModel.uc.clickComment.observe(this, new Observer() {
//            @Override
//            public void onChanged(Object o) {
//                if (viewModel.isVip || viewModel.sex == 0) {
//                    MVDialog.getInstance(UserDynamicFragment.this.getContext())
//                            .seCommentConfirm(new MVDialog.ConfirmComment() {
//                                @Override
//                                public void clickListItem(Dialog dialog, String comment) {
//                                    String id = ((Map<String, String>) o).get("id");
//                                    String toUserId = ((Map<String, String>) o).get("toUseriD");
//                                    viewModel.newsComment(Integer.valueOf(id), comment, toUserId != null ? Integer.valueOf(toUserId) : null);
//                                    dialog.dismiss();
//                                }
//                            })
//                            .chooseType(MVDialog.TypeEnum.BOTTOMCOMMENT)
//                            .show();
//                } else {
//                    DialogHelper.showNotVipCommentDialog(UserDynamicFragment.this);
//                }
//            }
//        });
//        viewModel.uc.clickImage.observe(this, new Observer() {
//            @Override
//            public void onChanged(Object o) {
//                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
//                String listStr = ((Map<String, String>) o).get("images");
//                List<String> images = GsonUtils.fromJson(listStr, new TypeToken<List<String>>() {
//                }.getType());
//                PictureSelectorUtil.previewImage(UserDynamicFragment.this.getContext(), images, position);
//            }
//        });
//
//    }
    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.clickMore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
                String type = ((Map<String, String>) o).get("type");
                Integer broadcastId = Integer.valueOf(((Map<String, String>) o).get("broadcastId"));
                mCirclePop = EasyPopup.create()
                        .setContentView(UserDynamicFragment.this.getContext(), R.layout.more_item)
//                        .setAnimationStyle(R.style.RightPopAnim)
                        //是否允许点击PopupWindow之外的地方消失
                        .setFocusAndOutsideEnable(true)
                        .setDimValue(0)
                        .setWidth(350)
                        .apply();

                LinearLayoutManager layoutManager = (LinearLayoutManager) binding.rcyTrend.getLayoutManager();
                final View child = layoutManager.findViewByPosition(position);
                if (child != null) {
                    mCirclePop.showAtAnchorView(child.findViewById(R.id.iv_more), YGravity.BELOW, XGravity.ALIGN_RIGHT, 0, 0);
                }
                TextView stop = mCirclePop.findViewById(R.id.tv_stop);
                mCirclePop.findViewById(R.id.tv_detele).setVisibility(View.GONE);
                stop.setText(R.string.playfun_report_user_title);
                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ReportUserFragment.ARG_REPORT_TYPE, "broadcast");
                        bundle.putInt(ReportUserFragment.ARG_REPORT_USER_ID, broadcastId);
                        startContainerActivity(ReportUserFragment.class.getCanonicalName(), bundle);
                        mCirclePop.dismiss();
                    }
                });
            }
        });

        viewModel.uc.clickLike.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
                if (viewModel.observableList.get(position).newsEntityObservableField.get().getIsGive() == 0) {
                    viewModel.newsGive(position);
                } else {
                    ToastUtils.showShort(R.string.playfun_already);
                }
            }
        });
        //动态评论
        viewModel.uc.clickComment.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if (viewModel.isVip || viewModel.sex == 0) {
                    MVDialog.getInstance(UserDynamicFragment.this.getContext())
                            .seCommentConfirm(new MVDialog.ConfirmComment() {
                                @Override
                                public void clickListItem(Dialog dialog, String comment) {
                                    if (StringUtils.isEmpty(comment)) {
                                        ToastUtils.showShort(R.string.playfun_warn_input_comment);
                                        return;
                                    }
                                    dialog.dismiss();
                                    String id = ((Map<String, String>) o).get("id");
                                    String toUserId = ((Map<String, String>) o).get("toUseriD");
                                    String toUserName = ((Map<String, String>) o).get("toUserName");
                                    viewModel.newsComment(Integer.valueOf(id), comment, toUserId != null ? Integer.valueOf(toUserId) : null, toUserName);
                                }
                            })
                            .chooseType(MVDialog.TypeEnum.BOTTOMCOMMENT)
                            .show();
                } else {
                    MVDialog.getInstance(UserDynamicFragment.this.getContext())
                            .setContent(StringResHelper.getCommentDialogTitle())
                            .setConfirmText(StringResHelper.getCommentDialogBtnText())
                            .chooseType(MVDialog.TypeEnum.CENTER)
                            .setConfirmOnlick(dialog -> {
                                dialog.dismiss();
                                if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == 1) {
                                    viewModel.start(VipSubscribeFragment.class.getCanonicalName());
                                } else {
                                    viewModel.start(CertificationFemaleFragment.class.getCanonicalName());
                                }
                            })
                            .chooseType(MVDialog.TypeEnum.CENTER)
                            .show();
                }
            }
        });
        viewModel.uc.clickImage.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
                String listStr = ((Map<String, String>) o).get("images");
                List<String> images = GsonUtils.fromJson(listStr, new TypeToken<List<String>>() {
                }.getType());
                PictureSelectorUtil.previewImage(UserDynamicFragment.this.getContext(), images, position);
            }
        });

    }
}
