package com.fine.friendlycc.ui.mine.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentWalletBinding;
import com.fine.friendlycc.bean.GoodsBean;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.fine.friendlycc.ui.certification.certificationmale.CertificationMaleFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DiamondRechargeActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.widget.dialog.MVDialog;


/**
 * @author wulei
 */
public class WalletFragment extends BaseToolbarFragment<FragmentWalletBinding, WalletViewModel> implements View.OnClickListener{

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_wallet;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public WalletViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(WalletViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.getUserAccount();
        binding.btnExchangeGameCoin.setOnClickListener(this);
        viewModel.certification.observe(this,event -> {
            MVDialog.getInstance(WalletFragment.this.getContext())
                    .setTitele(getString(R.string.playcc_fragment_certification_tip))
                    .setContent(getString(R.string.playcc_fragment_certification_content))
                    .setConfirmText(getString(R.string.playcc_task_fragment_task_new11))
                    .chooseType(MVDialog.TypeEnum.CENTER)
                    .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                        @Override
                        public void confirm(MVDialog dialog) {
                            if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == AppConfig.MALE) {
                                viewModel.start(CertificationMaleFragment.class.getCanonicalName());
                                return;
                            } else if (ConfigManager.getInstance().getAppRepository().readUserData().getSex() == AppConfig.FEMALE) {
                                viewModel.start(CertificationFemaleFragment.class.getCanonicalName());
                                return;
                            }
                            com.blankj.utilcode.util.ToastUtils.showShort(R.string.playcc_sex_unknown);
                            dialog.dismiss();
                        }
                    })
                    .chooseType(MVDialog.TypeEnum.CENTER)
                    .show();
        });
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_exchange_game_coin) {
            Intent intent = new Intent(mActivity, DiamondRechargeActivity.class);
            startActivity(intent);
        }
    }

    //??????????????????act
    ActivityResultLauncher<Intent> toGooglePlayIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.e("????????????????????????","=========");
        if (result.getData() != null) {
            Intent intentData = result.getData();
            GoodsBean goodsEntity = (GoodsBean) intentData.getSerializableExtra("goodsEntity");
            if(goodsEntity!=null){
                Log.e("????????????","===============");
            }
        }
    });

}