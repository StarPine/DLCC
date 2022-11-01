package com.fine.friendlycc.ui.mine.broadcast.mytrends.trenddetail;

import static com.fine.friendlycc.ui.userdetail.report.ReportUserFragment.ARG_REPORT_TYPE;
import static com.fine.friendlycc.ui.userdetail.report.ReportUserFragment.ARG_REPORT_USER_ID;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.entity.NewsEntity;
import com.fine.friendlycc.helper.DialogHelper;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.PictureSelectorUtil;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.google.gson.reflect.TypeToken;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentTrendDetailBinding;
import com.fine.friendlycc.ui.userdetail.report.ReportUserFragment;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 动态详情Fragment
 *
 * @author wulei
 */
public class TrendDetailFragment extends BaseToolbarFragment<FragmentTrendDetailBinding, TrendDetailViewModel> {
    public static final String ARG_TREND_DETAIL_ID = "arg_trend_detail_id";

    private int id;
    private EasyPopup mCirclePop;

    private final boolean playStatus = false;

    public static Bundle getStartBundle(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TREND_DETAIL_ID, id);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_trend_detail;
    }

    @Override
    public void initParam() {
        super.initParam();
        ApiUitl.isShow = false;//控制显示隐藏评论消息
        id = getArguments().getInt(ARG_TREND_DETAIL_ID, 0);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public TrendDetailViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        TrendDetailViewModel trendDetailViewModel = ViewModelProviders.of(this, factory).get(TrendDetailViewModel.class);
        trendDetailViewModel.newsEntityObservableField.set(new NewsEntity());
        trendDetailViewModel.newsEntityObservableField.get().setId(id);
        return trendDetailViewModel;
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppContext.isShowNotPaid = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        AppContext.isShowNotPaid = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
//        binding.
        viewModel.uc.clickMore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                mCirclePop = EasyPopup.create()
                        .setContentView(TrendDetailFragment.this.getContext(), R.layout.more_item)
//                        .setAnimationStyle(R.style.RightPopAnim)
                        //是否允许点击PopupWindow之外的地方消失
                        .setFocusAndOutsideEnable(true)
                        .setDimValue(0)
                        .setWidth(350)
                        .apply();


                mCirclePop.showAtAnchorView(binding.ivMore, YGravity.BELOW, XGravity.ALIGN_RIGHT, 0, 0);
                TextView stop = mCirclePop.findViewById(R.id.tv_stop);
                if (viewModel.newsEntityObservableField.get() == null || viewModel.newsEntityObservableField.get().getUser() == null) {//新增
                    return;
                }
                if (viewModel.userId == viewModel.newsEntityObservableField.get().getUser().getId()) {
                    stop.setText(viewModel.newsEntityObservableField.get().getBroadcast().getIsComment() == 1 ? getString(R.string.playfun_open_comment) : getString(R.string.playfun_fragment_issuance_program_no_comment));
                    stop.setVisibility(View.GONE);
                } else {
                    mCirclePop.findViewById(R.id.tv_detele).setVisibility(View.GONE);
                    stop.setText(getString(R.string.playfun_report_user_title));
                }

                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewModel.userId == viewModel.newsEntityObservableField.get().getUser().getId()) {
                            viewModel.setComment();
                        } else {
                            AppContext.instance().logEvent(AppsFlyerEvent.Report_2);
                            Bundle bundle = new Bundle();
                            bundle.putString(ARG_REPORT_TYPE, "broadcast");
                            bundle.putInt(ARG_REPORT_USER_ID, viewModel.newsEntityObservableField.get().getBroadcast().getId());
                            startContainerActivity(ReportUserFragment.class.getCanonicalName(), bundle);
                        }
                        mCirclePop.dismiss();
                    }
                });
                mCirclePop.findViewById(R.id.tv_detele).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MVDialog.getInstance(TrendDetailFragment.this.getContext())
                                .setContent(getString(R.string.playfun_comfirm_delete_trend))
                                .chooseType(MVDialog.TypeEnum.CENTER)
                                .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                    @Override
                                    public void confirm(MVDialog dialog) {
                                        viewModel.deleteNews();
                                        dialog.dismiss();
                                    }
                                })
                                .chooseType(MVDialog.TypeEnum.CENTER)
                                .show();
                        mCirclePop.dismiss();
                    }
                });
            }
        });

        viewModel.uc.clickComment.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if (ConfigManager.getInstance().getAppRepository().readUserData().getIsVip() == 1 || (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == AppConfig.FEMALE && ConfigManager.getInstance().getAppRepository().readUserData().getCertification() == 1)) {
                    MVDialog.getInstance(TrendDetailFragment.this.getContext())
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
                    DialogHelper.showNotVipCommentDialog(TrendDetailFragment.this);
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
                PictureSelectorUtil.previewImage(TrendDetailFragment.this.getContext(), images, position);
            }
        });
//        viewModel.uc.clickIisDelete.observe(this, new Observer() {
//            @Override
//            public void onChanged(Object o) {
//                binding.rlayoutDelete.setVisibility(viewModel.isDetele.get()?View.VISIBLE:View.GONE);
//            }
//        });
    }

}