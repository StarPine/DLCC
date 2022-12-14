package com.fine.friendlycc.ui.radio.radiohome;

import static com.fine.friendlycc.ui.userdetail.report.ReportUserFragment.ARG_REPORT_TYPE;
import static com.fine.friendlycc.ui.userdetail.report.ReportUserFragment.ARG_REPORT_USER_ID;

import android.app.Dialog;
import android.content.Context;
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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.FragmentRadioBinding;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.bean.RadioFilterItemBean;
import com.fine.friendlycc.helper.DialogHelper;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseRefreshFragment;
import com.fine.friendlycc.ui.dialog.CityChooseDialog;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.TrendItemViewModel;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.ui.userdetail.report.ReportUserFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.PictureSelectorUtil;
import com.fine.friendlycc.widget.AppBarStateChangeListener;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.fine.friendlycc.widget.dropdownfilterpop.DropDownFilterPopupWindow;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.reflect.TypeToken;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class RadioFragment extends BaseRefreshFragment<FragmentRadioBinding, RadioViewModel> {
    private Context mContext;
    private EasyPopup mCirclePop;

    private CityChooseDialog cityChooseDialog;

    private List<RadioFilterItemBean> radioFilterListData;
    private DropDownFilterPopupWindow radioFilterPopup;
    private Integer radioFilterCheckIndex;
    private List<ConfigItemBean> citys;

    private RadioFragmentLifecycle radioFragmentLifecycle;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_radio;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        try {
            if (AudioPlayer.getInstance().isPlaying()) {
                AudioPlayer.getInstance().stopPlay();
            }
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }
    }

    @Override
    public RadioViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(RadioViewModel.class);
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

        if(hidden){
            if (AudioPlayer.getInstance().isPlaying()) {
                AudioPlayer.getInstance().stopPlay();
            }
        }
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }

    }

    @Override
    public void initData() {
        super.initData();
        radioFragmentLifecycle = new RadioFragmentLifecycle();
        //???radioFragmentLifecycle??????View?????????????????????
        getLifecycle().addObserver(radioFragmentLifecycle);
        citys = ConfigManager.getInstance().getAppRepository().readCityConfig();
        ConfigItemBean nearItemEntity = new ConfigItemBean();
        nearItemEntity.setId(-1);
        nearItemEntity.setName(getStringByResId(R.string.playcc_tab_female_1));
        citys.add(0, nearItemEntity);

        radioFilterListData = new ArrayList<>();
        radioFilterListData.add(new RadioFilterItemBean<>(getString(R.string.playcc_radio_selected_zuiz),2));
        radioFilterListData.add(new RadioFilterItemBean<>(getString(R.string.playcc_just_look_lady), 0));
        radioFilterListData.add(new RadioFilterItemBean<>(getString(R.string.playcc_just_look_man), 1));
        radioFilterPopup =  new DropDownFilterPopupWindow(mActivity, radioFilterListData);
        radioFilterCheckIndex = 0;
        radioFilterPopup.setSelectedPosition(radioFilterCheckIndex);
        radioFilterPopup.setOnItemClickListener((popupWindow, position) -> {
            popupWindow.dismiss();
            RadioFilterItemBean obj =radioFilterListData.get(position);
            radioFilterCheckIndex = position;
            if (obj.getData() == null) {
                viewModel.setSexId(null);
            } else {
                if (((Integer) obj.getData()).intValue() == 0) {
                    CCApplication.instance().logEvent(AppsFlyerEvent.Male_Only);
                } else {
                    CCApplication.instance().logEvent(AppsFlyerEvent.Female_Only);
                }
                if(((Integer)obj.getData()).intValue() == 2){//????????????
                    viewModel.type = 1;//????????????
                    viewModel.cityId = null;
                    viewModel.gameId = null;
                    viewModel.setIsCollect(1);
                    CCApplication.instance().logEvent(AppsFlyerEvent.Follow_Only);
                }else{
                    viewModel.setSexId((Integer) obj.getData());
                }
                viewModel.tarckingTitle.set(obj.getName());
            }
        });

        binding.appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                if (state == State.COLLAPSED) {
                    //????????????
                    binding.llBg.setVisibility(View.GONE);
                } else if (state == State.EXPANDED) {
                    //????????????
                    binding.llBg.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.refreshLayout.autoRefresh();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        CCApplication.instance().logEvent(AppsFlyerEvent.Broadcast);
        mContext = this.getContext();
        //????????????
        viewModel.radioUC.startBannerEvent.observe(this, unused -> {
            if(viewModel.radioItemsAdUser.size()>2){
                binding.rcvAduser.setPlaying(true);
                //binding.rcvAduser.scrollToPosition(2);
            }
        });
        //??????banner??????
        viewModel.radioUC.clickBannerIdx.observe(this, integer -> {
            binding.rcvAduser.scrollToPosition(integer);
        });
        //??????????????????
        viewModel.radioUC.sendDialogViewEvent.observe(this, event -> {
            googleCoinValueBox();
        });
        //????????????
        viewModel.radioUC.otherBusy.observe(this, o -> {
            TraceDialog.getInstance(getContext())
                    .chooseType(TraceDialog.TypeEnum.CENTER)
                    .setTitle(StringUtils.getString(R.string.playcc_other_busy_title))
                    .setContent(StringUtils.getString(R.string.playcc_other_busy_text))
                    .setConfirmText(StringUtils.getString(R.string.playcc_mine_trace_delike_confirm))
                    .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                        @Override
                        public void confirm(Dialog dialog) {

                            dialog.dismiss();
                        }
                    }).TraceVipDialog().show();
        });
        //?????? ???????????? ??? ???
        viewModel.radioUC.clickTacking.observe(this, unused -> {
            radioFilterPopup.setSelectedPosition(radioFilterCheckIndex);
            radioFilterPopup.showAsDropDown(binding.llTracking);
        });
        //????????????
        viewModel.radioUC.clickRegion.observe(this, unused -> {
            if(cityChooseDialog==null){
                cityChooseDialog = new CityChooseDialog(getContext(),citys,viewModel.cityId);
            }
            cityChooseDialog.show();
            cityChooseDialog.setCityChooseDialogListener((dialog1, itemEntity) -> {
                if(itemEntity!=null){
                    if(itemEntity.getId()!=null && itemEntity.getId()==-1){
                        viewModel.cityId = null;
                    }else{
                        viewModel.cityId = itemEntity.getId();
                    }

                    viewModel.regionTitle.set(itemEntity.getName());
                }else{
                    viewModel.cityId = null;
                    viewModel.regionTitle.set(StringUtils.getString(R.string.playcc_tab_female_1));
                }
                binding.refreshLayout.autoRefresh();
                dialog1.dismiss();
            });
        });
        //????????????
        viewModel.radioUC.zoomInp.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String drawable) {
                TraceDialog.getInstance(getContext())
                        .getImageDialog(mContext,drawable).show();
            }
        });

        viewModel.radioUC.clickMore.observe(this, o -> {
            Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
            String type = ((Map<String, String>) o).get("type");
            Integer broadcastId = Integer.valueOf(((Map<String, String>) o).get("broadcastId"));
            mCirclePop = EasyPopup.create()
                    .setContentView(RadioFragment.this.getContext(), R.layout.more_item)
//                        .setAnimationStyle(R.style.RightPopAnim)
                    //??????????????????PopupWindow?????????????????????
                    .setFocusAndOutsideEnable(true)
                    .setDimValue(0)
                    .setWidth(350)
                    .apply();

            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.rcvRadio.getLayoutManager();
            final View child = layoutManager.findViewByPosition(position);
            if (child != null) {
                mCirclePop.showAtAnchorView(child.findViewById(R.id.iv_more), YGravity.BELOW, XGravity.ALIGN_RIGHT, 0, 0);
            }
            TextView stop = mCirclePop.findViewById(R.id.tv_stop);

            boolean isSelf = false;
            if (type.equals(RadioViewModel.RadioRecycleType_New)) {
                if (viewModel.userId == ((TrendItemViewModel) viewModel.radioItems.get(position)).newsEntityObservableField.get().getUser().getId()) {
                    stop.setText(((TrendItemViewModel) viewModel.radioItems.get(position)).newsEntityObservableField.get().getBroadcast().getIsComment() == 0 ? getString(R.string.playcc_fragment_issuance_program_no_comment) : getString(R.string.playcc_open_comment));
                    stop.setVisibility(View.GONE);
                    isSelf = true;
                } else {
                    mCirclePop.findViewById(R.id.tv_detele).setVisibility(View.GONE);
                    stop.setText(getString(R.string.playcc_report_user_title));
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
                    MVDialog.getInstance(RadioFragment.this.getContext())
                            .setContent(type.equals(RadioViewModel.RadioRecycleType_New) ? getString(R.string.playcc_comfirm_delete_trend) : getString(R.string.playcc_confirm_delete_program))
                            .chooseType(MVDialog.TypeEnum.CENTER)
                            .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                @Override
                                public void confirm(MVDialog dialog) {
                                    if (type.equals(RadioViewModel.RadioRecycleType_New)) {
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

        viewModel.radioUC.clickLike.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
                String type = ((Map<String, String>) o).get("type");
                if (type.equals(RadioViewModel.RadioRecycleType_New)) {
                    if (((TrendItemViewModel) viewModel.radioItems.get(position)).newsEntityObservableField.get().getIsGive() == 0) {
                        viewModel.newsGive(position);
                    } else {
                        ToastUtils.showShort(R.string.playcc_already);
                    }
                }
            }
        });
        viewModel.radioUC.clickComment.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                viewModel.initUserDate();
                String id = ((Map<String, String>) o).get("id");
                String toUserId = ((Map<String, String>) o).get("toUseriD");
                String type = ((Map<String, String>) o).get("type");
                String toUserName = ((Map<String, String>) o).get("toUserName");
                if (ConfigManager.getInstance().getAppRepository().readUserData().getIsVip() == 1 || (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == AppConfig.FEMALE && ConfigManager.getInstance().getAppRepository().readUserData().getCertification() == 1)) {
                    MVDialog.getInstance(RadioFragment.this.getContext())
                            .seCommentConfirm(new MVDialog.ConfirmComment() {
                                @Override
                                public void clickListItem(Dialog dialog, String comment) {
                                    if (StringUtils.isEmpty(comment)) {
                                        ToastUtils.showShort(R.string.playcc_warn_input_comment);
                                        return;
                                    }
                                    dialog.dismiss();
                                    if (type.equals(RadioViewModel.RadioRecycleType_New)) {
                                        viewModel.newsComment(Integer.valueOf(id), comment, toUserId != null ? Integer.valueOf(toUserId) : null, toUserName);
                                    }
                                }
                            })
                            .chooseType(MVDialog.TypeEnum.BOTTOMCOMMENT)
                            .show();
                } else {
                    DialogHelper.showNotVipCommentDialog(RadioFragment.this);
                }

            }
        });
        viewModel.radioUC.clickImage.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Integer position = Integer.valueOf(((Map<String, String>) o).get("position"));
                String listStr = ((Map<String, String>) o).get("images");
                List<String> images = GsonUtils.fromJson(listStr, new TypeToken<List<String>>() {
                }.getType());
                PictureSelectorUtil.previewImage(RadioFragment.this.getContext(), images, position);
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

    //????????????
    private void googleCoinValueBox() {
        CCApplication.instance().logEvent(AppsFlyerEvent.Top_up);
        Intent intent = new Intent(mActivity, DialogDiamondRechargeActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.pop_enter_anim, 0);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
    }

}