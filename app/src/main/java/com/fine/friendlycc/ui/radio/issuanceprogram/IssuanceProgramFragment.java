package com.fine.friendlycc.ui.radio.issuanceprogram;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aliyun.svideo.crop.CropMediaActivity;
import com.aliyun.svideosdk.common.struct.common.AliyunSnapVideoParam;
import com.aliyun.svideosdk.common.struct.common.VideoDisplayMode;
import com.aliyun.svideosdk.common.struct.common.VideoQuality;
import com.aliyun.svideosdk.common.struct.encoder.VideoCodecs;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.FragmentIssuanceProgramBinding;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.bean.DatingObjItemBean;
import com.fine.friendlycc.bean.ThemeItemBean;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.fine.friendlycc.ui.certification.certificationmale.CertificationMaleFragment;
import com.fine.friendlycc.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.widget.coinpaysheet.CoinPaySheet;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.luck.picture.lib.permissions.PermissionChecker;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class IssuanceProgramFragment extends BaseToolbarFragment<FragmentIssuanceProgramBinding, IssuanceProgramViewModel> {
    public static final String ARG_PROGRAM_ENTITY = "arg_program_entity";
    public static final String ARG_CHOOSE_CITY = "arg_choose_city";
    public static final String ARG_ADDRESS_NAME = "arg_address_name";
    public static final String ARG_ADDRESS = "arg_address";
    public static final String ARG_ADDRESS_LAT = "arg_address_lat";
    public static final String ARG_ADDRESS_LNG = "arg_address_lng";

    private ThemeItemBean themeItemEntity;
    private ConfigItemBean city;
    private String addressName, address;
    private Double lat, lng;

    private TimePickerView pvTime;

    public static Bundle getStartBundle(ThemeItemBean program, ConfigItemBean city) {
        if (program == null) {
            ToastUtils.showShort(R.string.playcc_parameter_error);
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PROGRAM_ENTITY, program);
        if (city != null) {
            bundle.putParcelable(ARG_CHOOSE_CITY, city);
        }
        return bundle;
    }

    public static Bundle getStartBundle(ThemeItemBean program, ConfigItemBean city, String addressName, String address, Double lat, Double lng) {
        if (program == null || city == null || addressName == null) {
            ToastUtils.showShort(R.string.playcc_parameter_error);
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PROGRAM_ENTITY, program);
        bundle.putParcelable(ARG_CHOOSE_CITY, city);
        bundle.putString(ARG_ADDRESS_NAME, addressName);
        bundle.putString(ARG_ADDRESS, address);
        if (lat != null) {
            bundle.putDouble(ARG_ADDRESS_LAT, lat);
        }
        if (lng != null) {
            bundle.putDouble(ARG_ADDRESS_LNG, lng);
        }
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_issuance_program;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public IssuanceProgramViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        IssuanceProgramViewModel issuanceProgramViewModel = ViewModelProviders.of(this, factory).get(IssuanceProgramViewModel.class);
        //issuanceProgramViewModel.chooseProgramItem.set(themeItemEntity);
        issuanceProgramViewModel.chooseCityItem.set(city);
        issuanceProgramViewModel.addressName.set(addressName);
        issuanceProgramViewModel.address.set(address);
        issuanceProgramViewModel.lat.set(lat);
        issuanceProgramViewModel.lng.set(lng);
        return issuanceProgramViewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        themeItemEntity = getArguments().getParcelable(ARG_PROGRAM_ENTITY);
        city = getArguments().getParcelable(ARG_CHOOSE_CITY);
        addressName = getArguments().getString(ARG_ADDRESS_NAME);
        address = getArguments().getString(ARG_ADDRESS);
        lat = getArguments().getDouble(ARG_ADDRESS_LAT);
        lng = getArguments().getDouble(ARG_ADDRESS_LNG);
    }

    ActivityResultLauncher<String> toPermissionIntent = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            AliyunSnapVideoParam mCropParam = new AliyunSnapVideoParam.Builder()
                    .setFrameRate(30)
                    .setGop(250)
                    .setFilterList(null)
                    .setCropMode(VideoDisplayMode.SCALE)
                    .setVideoQuality(VideoQuality.HD)
                    .setVideoCodec(VideoCodecs.H264_HARDWARE)
                    .setResolutionMode(0)
                    .setRatioMode(1)
                    .setCropMode(VideoDisplayMode.SCALE)
                    .setNeedRecord(false)
                    .setMinVideoDuration(3000)
                    .setMaxVideoDuration(60 * 1000 * 1000)
                    .setMinCropDuration(3000)
                    .setSortMode(AliyunSnapVideoParam.SORT_MODE_MERGE)
                    .build();
            AppConfig.isCorpAliyun = true;
            CropMediaActivity.startCropForResult(_mActivity, 2002, mCropParam);
        } else {
            Toast.makeText(_mActivity, R.string.picture_jurisdiction, Toast.LENGTH_SHORT).show();
            if (!ActivityCompat.shouldShowRequestPermissionRationale(_mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // ????????????????????????????????????, ??????????????? false
                PermissionChecker.launchAppDetailsSettings(getContext());
            }
        }
    });

    @Override
    public void initViewObservable() {
        loadDatingDetail();
        viewModel.uc.startVideoActivity.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                toPermissionIntent.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
        viewModel.uc.checkDatingText.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
        viewModel.uc.clickNotVip.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(final Integer typeDating) {
                if(typeDating.intValue()==1){
                    int sex = ConfigManager.getInstance().isMale()?1:0;
                    MVDialog.getInstance(IssuanceProgramFragment.this.getContext())
                            .setContent(getString(R.string.playcc_issuance_tends))
                            .setConfirmText(sex == 1 ? getString(R.string.playcc_to_be_member_issuance) : getString(R.string.playcc_author_free_issuance))
                            .setConfirmTwoText(getString(R.string.playcc_pay_issuance) + "???" + ConfigManager.getInstance().getNewsMoney() + getString(R.string.playcc_element) + "???")
                            .chooseType(MVDialog.TypeEnum.CENTER)
                            .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                @Override
                                public void confirm(MVDialog dialog) {
                                    if (sex == 1) {
                                        viewModel.start(VipSubscribeFragment.class.getCanonicalName());
                                    } else {
                                        if (sex == AppConfig.MALE) {
                                            viewModel.start(CertificationMaleFragment.class.getCanonicalName());
                                            return;
                                        } else if (sex == AppConfig.FEMALE) {
                                            viewModel.start(CertificationFemaleFragment.class.getCanonicalName());
                                            return;
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setConfirmTwoOnclick(new MVDialog.ConfirmTwoOnclick() {
                                @Override
                                public void confirm(MVDialog dialog) {
                                    showDialog(typeDating);
                                }
                            })
                            .getTop2BottomDialog()
                            .show();
                }else{
                    MVDialog.getInstance(IssuanceProgramFragment.this.getContext())
                            .setTitele(getString(R.string.playcc_fragment_issuance_program_title))
                            .setConfirmText(viewModel.sex == 1 ? getString(R.string.playcc_to_be_member_issuance) : getString(R.string.playcc_author_free_issuance))
                            .setConfirmTwoText(getString(R.string.playcc_pay_issuance) + "???" + viewModel.configManager.getTopicalMoney() + getString(R.string.playcc_element) + "???")
                            .chooseType(MVDialog.TypeEnum.CENTER)
                            .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                                @Override
                                public void confirm(MVDialog dialog) {
                                    if (viewModel.sex == 1) {
                                        viewModel.start(VipSubscribeFragment.class.getCanonicalName());
                                    } else {
                                        if (viewModel.sex != null) {
                                            if (viewModel.sex == AppConfig.MALE) {
                                                viewModel.start(CertificationMaleFragment.class.getCanonicalName());
                                                return;
                                            } else if (viewModel.sex == AppConfig.FEMALE) {
                                                viewModel.start(CertificationFemaleFragment.class.getCanonicalName());
                                                return;
                                            }
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setConfirmTwoOnclick(new MVDialog.ConfirmTwoOnclick() {
                                @Override
                                public void confirm(MVDialog dialog) {
                                    showDialog(typeDating);
                                }
                            })
                            .getTop2BottomDialog()
                            .show();
                }
            }
        });
        viewModel.uc.clickTheme.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {

            }
        });
        viewModel.uc.clickAddress.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //????????????
                MVDialog.raDioChooseCity chooseOccupation = new MVDialog.raDioChooseCity() {

                    @Override
                    public void clickListItem(Dialog dialog, ConfigItemBean configItemEntity) {
                        try {
                            CCApplication.instance().logEvent(AppsFlyerEvent.Location);
                            city = configItemEntity;
                            viewModel.chooseCityItem.set(city);
                        } catch (Exception e) {

                        }
                    }
                };
                MVDialog.getCityDialog(getContext(), viewModel.list_chooseCityItem, viewModel.chooseCityItem.get() == null ? null : viewModel.chooseCityItem.get().getId(), chooseOccupation);
            }
        });
    }

    // ??????Fragment??????setFragmentResult()??????????????????????????????????????????
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        try {
            city = data.getParcelable(ARG_CHOOSE_CITY);
            addressName = data.getString(ARG_ADDRESS_NAME);
            address = data.getString(ARG_ADDRESS);
            lat = data.getDouble(ARG_ADDRESS_LAT);
            lng = data.getDouble(ARG_ADDRESS_LNG);
            viewModel.chooseCityItem.set(city);
            viewModel.addressName.set(addressName);
            viewModel.address.set(address);
            viewModel.lat.set(lat);
            viewModel.lng.set(lng);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void loadDatingDetail() {

        DatingObjItemBean datingObjItemEntity5 = new DatingObjItemBean();
        datingObjItemEntity5.setType(0);
        datingObjItemEntity5.setId(1);
        datingObjItemEntity5.setName(StringUtils.getString(R.string.playcc_mood_item_id1));
        datingObjItemEntity5.setSelect(true);
        datingObjItemEntity5.setIconChecked(getResources().getResourceName(R.mipmap.dating_obj_mood1_img));
        viewModel.$datingObjItemEntity = datingObjItemEntity5;
        DatingObjItemBean datingObjItemEntity1 = new DatingObjItemBean();
        datingObjItemEntity1.setType(0);
        datingObjItemEntity1.setId(2);
        datingObjItemEntity1.setName(StringUtils.getString(R.string.playcc_mood_item_id2));
        datingObjItemEntity1.setIconChecked(getResources().getResourceName(R.mipmap.dating_obj_mood2_img));
        DatingObjItemBean datingObjItemEntity2 = new DatingObjItemBean();
        datingObjItemEntity2.setType(0);
        datingObjItemEntity2.setId(3);
        datingObjItemEntity2.setName(StringUtils.getString(R.string.playcc_mood_item_id3));
        datingObjItemEntity2.setIconChecked(getResources().getResourceName(R.mipmap.dating_obj_mood3_img));
        DatingObjItemBean datingObjItemEntity3 = new DatingObjItemBean();
        datingObjItemEntity3.setType(0);
        datingObjItemEntity3.setId(4);
        datingObjItemEntity3.setName(StringUtils.getString(R.string.playcc_mood_item_id4));
        datingObjItemEntity3.setIconChecked(getResources().getResourceName(R.mipmap.dating_obj_mood4_img));
        DatingObjItemBean datingObjItemEntity4 = new DatingObjItemBean();
        datingObjItemEntity4.setType(0);
        datingObjItemEntity4.setId(5);
        datingObjItemEntity4.setName(StringUtils.getString(R.string.playcc_mood_item_id5));
        datingObjItemEntity4.setIconChecked(getResources().getResourceName(R.mipmap.dating_obj_mood5_img));


        DatingObjItemBean datingObjItemEntity = new DatingObjItemBean();
        datingObjItemEntity.setType(0);
        datingObjItemEntity.setId(6);
        datingObjItemEntity.setName(StringUtils.getString(R.string.playcc_mood_item_id6));
        datingObjItemEntity.setIconChecked(getResources().getResourceName(R.mipmap.dating_obj_mood6_img));

        RadioDatingItemViewModel radioThemeItemViewMode0 = new RadioDatingItemViewModel(viewModel, datingObjItemEntity);
        RadioDatingItemViewModel radioThemeItemViewMode1 = new RadioDatingItemViewModel(viewModel, datingObjItemEntity1);
        RadioDatingItemViewModel radioThemeItemViewMode2 = new RadioDatingItemViewModel(viewModel, datingObjItemEntity2);
        RadioDatingItemViewModel radioThemeItemViewMode3 = new RadioDatingItemViewModel(viewModel, datingObjItemEntity3);
        RadioDatingItemViewModel radioThemeItemViewMode4 = new RadioDatingItemViewModel(viewModel, datingObjItemEntity4);
        RadioDatingItemViewModel radioThemeItemViewMode5 = new RadioDatingItemViewModel(viewModel, datingObjItemEntity5);
        viewModel.objItems.add(radioThemeItemViewMode5);
        viewModel.objItems.add(radioThemeItemViewMode1);
        viewModel.objItems.add(radioThemeItemViewMode2);
        viewModel.objItems.add(radioThemeItemViewMode3);
        viewModel.objItems.add(radioThemeItemViewMode4);
        viewModel.objItems.add(radioThemeItemViewMode0);

    }
    private void showDialog(int type) {
        int payType = 0;
        String titles = StringUtils.getString(R.string.playcc_issuance_tends);
        if(type==1){//??????
            payType = 8;
            titles = StringUtils.getString(R.string.playcc_issuance_tends);
        }else{
            payType = 9;//??????
            titles = StringUtils.getString(R.string.playcc_send_show);
        }

        new CoinPaySheet.Builder(mActivity).setPayParams(payType, ConfigManager.getInstance().getAppRepository().readUserData().getId(), titles, false, new CoinPaySheet.CoinPayDialogListener() {
            @Override
            public void onPaySuccess(CoinPaySheet sheet, String orderNo, Integer payPrice) {
                sheet.dismiss();
                ToastUtils.showShort(R.string.playcc_pay_success);
                viewModel.sendConfirm();
            }
            @Override
            public void toGooglePlayView() {
                toRecharge();
            }
        }).build().show();
    }
    /**
     * ?????????
     */
    private void toRecharge() {
        Intent intent = new Intent(mActivity, DialogDiamondRechargeActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.pop_enter_anim, 0);
    }

    @Override
    public void initData() {
        super.initData();
        binding.postContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvSize.setText(charSequence.length()+"/120");
                viewModel.programDesc.set(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}