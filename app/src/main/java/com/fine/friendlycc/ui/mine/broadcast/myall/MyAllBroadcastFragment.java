package com.fine.friendlycc.ui.mine.broadcast.myall;

import static com.fine.friendlycc.ui.radio.radiohome.RadioViewModel.RadioRecycleType_New;
import static com.fine.friendlycc.ui.userdetail.report.ReportUserFragment.ARG_REPORT_TYPE;
import static com.fine.friendlycc.ui.userdetail.report.ReportUserFragment.ARG_REPORT_USER_ID;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.FragmentMyAllBroadcastBinding;
import com.fine.friendlycc.helper.DialogHelper;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseRefreshFragment;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.TrendItemViewModel;
import com.fine.friendlycc.ui.radio.issuanceprogram.IssuanceProgramFragment;
import com.fine.friendlycc.ui.userdetail.report.ReportUserFragment;
import com.fine.friendlycc.utils.PictureSelectorUtil;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.google.gson.reflect.TypeToken;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: ?????????
 * Time: 2021/10/9 12:26
 * Description: This is MyAllBroadcastFragment
 */
public class MyAllBroadcastFragment extends BaseRefreshFragment<FragmentMyAllBroadcastBinding, MyAllBroadcastViewModel> {
    private EasyPopup mCirclePop;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.fragment_my_all_broadcast;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MyAllBroadcastViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(MyAllBroadcastViewModel.class);
    }


    @Override
    public void initData() {
        super.initData();

        binding.rcyBroadcast.setOnScrollListener(new RecyclerView.OnScrollListener() {
            final boolean scrollState = false;
            public int firstVisibleItem, lastVisibleItem, visibleCount;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                switch (newState) {
//                    case RecyclerView.SCROLL_STATE_IDLE: //????????????
//                        scrollState = false;
//                        try{
//                            autoPlayVideo(recyclerView);
//                        }catch (Exception e) {
//
//                        }
//                        break;
//                    case RecyclerView.SCROLL_STATE_DRAGGING: //????????????
//                        scrollState = true;
//                        break;
//                    case RecyclerView.SCROLL_STATE_SETTLING: //????????????
//                        scrollState = true;
//                        break;
//                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;

                //??????0???????????????
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //?????????????????????
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //?????????????????????TAG
                    if (GSYVideoManager.instance().getPlayTag().equals("SampleCoverVideoPlayer")
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
                        //??????????????????????????????????????????????????????????????????
                        try {
                            GSYVideoManager.releaseAllVideos();
                        } catch (Exception e) {

                        }
                        viewModel.adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }
    }

    /**
     * @return void
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [hidden]
     * @Date 2021/8/4
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        }else {
            try {
                GSYVideoManager.releaseAllVideos();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.clickMore.observe(this, o -> {
            Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
            String type = ((Map<String, String>) o).get("type");
            Integer broadcastId = Integer.valueOf(((Map<String, String>) o).get("broadcastId"));
            mCirclePop = EasyPopup.create()
                    .setContentView(MyAllBroadcastFragment.this.getContext(), R.layout.more_item)
                    //??????????????????PopupWindow?????????????????????
                    .setFocusAndOutsideEnable(true)
                    .setDimValue(0)
                    .setWidth(350)
                    .apply();
            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.rcyBroadcast.getLayoutManager();
            final View child = layoutManager.findViewByPosition(position);
            if (child != null) {
                mCirclePop.showAtAnchorView(child.findViewById(R.id.iv_more), YGravity.BELOW, XGravity.ALIGN_RIGHT, 0, 0);
            }
            TextView stop = mCirclePop.findViewById(R.id.tv_stop);

            boolean isSelf = false;
            if (type.equals(RadioRecycleType_New)) {
                if (viewModel.userId == ((TrendItemViewModel) viewModel.observableList.get(position)).newsEntityObservableField.get().getUser().getId()) {
                    stop.setText(((TrendItemViewModel) viewModel.observableList.get(position)).newsEntityObservableField.get().getBroadcast().getIsComment() == 0 ? getString(R.string.playcc_fragment_issuance_program_no_comment) : getString(R.string.playcc_open_comment));
                    stop.setVisibility(View.GONE);
                    isSelf = true;
                } else {
                    mCirclePop.findViewById(R.id.tv_detele).setVisibility(View.GONE);
                    stop.setText(getString(R.string.playcc_report_user_title));
                    isSelf = false;
                }
            }

            boolean finalIsSelf = isSelf;
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalIsSelf) {
                        viewModel.setComment(position, type);
                    } else {
                        CCApplication.instance().logEvent(AppsFlyerEvent.Report);
                        Bundle bundle = new Bundle();
                        bundle.putString(ARG_REPORT_TYPE, "broadcast");
                        bundle.putInt(ARG_REPORT_USER_ID, broadcastId);
                        startContainerActivity(ReportUserFragment.class.getCanonicalName(), bundle);
                    }
                    mCirclePop.dismiss();
                }
            });
            mCirclePop.findViewById(R.id.tv_detele).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MVDialog.getInstance(MyAllBroadcastFragment.this.getContext())
                            .setContent(type.equals(RadioRecycleType_New) ? getString(R.string.playcc_comfirm_delete_trend) : getString(R.string.playcc_confirm_delete_program))
                            .chooseType(MVDialog.TypeEnum.CENTER)
                            .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                @Override
                                public void confirm(MVDialog dialog) {
                                    if (type.equals(RadioRecycleType_New)) {
                                        viewModel.deleteNews(position);
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .chooseType(MVDialog.TypeEnum.CENTER)
                            .show();
                    mCirclePop.dismiss();
                }
            });

        });

        /**
         * ????????????
         */
        viewModel.uc.programSubject.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                CCApplication.instance().logEvent(AppsFlyerEvent.Dating);
                viewModel.start(IssuanceProgramFragment.class.getCanonicalName());
            }
        });
        //??????
        viewModel.uc.clickLike.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
                String type = ((Map<String, String>) o).get("type");
                if (type.equals(RadioRecycleType_New)) {
                    if (((TrendItemViewModel) viewModel.observableList.get(position)).newsEntityObservableField.get().getIsGive() == 0) {
                        viewModel.newsGive(position);
                    } else {
                        ToastUtils.showShort(R.string.playcc_already);
                    }
                }
            }
        });
        viewModel.uc.clickComment.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                viewModel.initUserDate();
                String id = ((Map<String, String>) o).get("id");
                String toUserId = ((Map<String, String>) o).get("toUseriD");
                String type = ((Map<String, String>) o).get("type");
                String toUserName = ((Map<String, String>) o).get("toUserName");
                if (ConfigManager.getInstance().getAppRepository().readUserData().getIsVip() == 1 || (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == AppConfig.FEMALE && ConfigManager.getInstance().getAppRepository().readUserData().getCertification() == 1)) {
                    MVDialog.getInstance(MyAllBroadcastFragment.this.getContext())
                            .seCommentConfirm(new MVDialog.ConfirmComment() {
                                @Override
                                public void clickListItem(Dialog dialog, String comment) {
                                    if (StringUtils.isEmpty(comment)) {
                                        ToastUtils.showShort(R.string.playcc_warn_input_comment);
                                        return;
                                    }
                                    dialog.dismiss();
                                    if (type.equals(RadioRecycleType_New)) {
                                        viewModel.newsComment(Integer.valueOf(id), comment, toUserId != null ? Integer.valueOf(toUserId) : null, toUserName);
                                    }
                                }
                            })
                            .chooseType(MVDialog.TypeEnum.BOTTOMCOMMENT)
                            .show();
                } else {
                    DialogHelper.showNotVipCommentDialog(MyAllBroadcastFragment.this);
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
                PictureSelectorUtil.previewImage(MyAllBroadcastFragment.this.getContext(), images, position);
            }
        });
        NotificationManagerCompat notification = NotificationManagerCompat.from(getContext());
        boolean isEnabled = notification.areNotificationsEnabled();
        if (!isEnabled) {
            //???????????????
            MMAlertDialog.DialogNotification(getContext(), true, new MMAlertDialog.DilodAlertInterface() {
                @Override
                public void confirm(DialogInterface dialog, int which, int sel_Index) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("android.provider.extra.APP_PACKAGE", getContext().getPackageName());
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("app_package", getContext().getPackageName());
                        intent.putExtra("app_uid", getContext().getApplicationInfo().uid);
                        startActivity(intent);
                    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
                    } else if (Build.VERSION.SDK_INT >= 15) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
                    }
                    startActivity(intent);
                }

                @Override
                public void cancel(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }
}
