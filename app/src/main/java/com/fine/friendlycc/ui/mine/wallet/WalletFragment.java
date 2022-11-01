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

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.FragmentWalletBinding;
import com.fine.friendlycc.entity.GameCoinBuy;
import com.fine.friendlycc.entity.GoodsEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.fine.friendlycc.ui.certification.certificationmale.CertificationMaleFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DiamondRechargeActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.widget.coinrechargesheet.CoinExchargeItegralPayDialog;
import com.fine.friendlycc.widget.dialog.MVDialog;

import me.goldze.mvvmhabit.utils.ToastUtils;

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
        binding.btnGameCoinTopup.setOnClickListener(this);
        viewModel.certification.observe(this,event -> {
            MVDialog.getInstance(WalletFragment.this.getContext())
                    .setTitele(getString(R.string.playfun_fragment_certification_tip))
                    .setContent(getString(R.string.playfun_fragment_certification_content))
                    .setConfirmText(getString(R.string.playfun_task_fragment_task_new11))
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
                            com.blankj.utilcode.util.ToastUtils.showShort(R.string.playfun_sex_unknown);
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
        }else if(R.id.btn_game_coin_topup == v.getId()){
            CoinExchargeItegralPayDialog coinRechargeSheetView = new CoinExchargeItegralPayDialog(getContext(),mActivity);
            coinRechargeSheetView.show();
            coinRechargeSheetView.setCoinRechargeSheetViewListener(new CoinExchargeItegralPayDialog.CoinRechargeSheetViewListener() {
                @Override
                public void onPaySuccess(CoinExchargeItegralPayDialog sheetView, GameCoinBuy sel_goodsEntity) {
                    sheetView.endGooglePlayConnect();
                    sheetView.dismiss();
                    MVDialog.getInstance(WalletFragment.this.getContext())
                            .setTitele(StringUtils.getString(R.string.playfun_recharge_coin_success))
                            .setConfirmText(StringUtils.getString(R.string.playfun_confirm))
                            .setConfirmOnlick(dialog -> {
                                dialog.dismiss();
                                viewModel.getUserAccount();
                            })
                            .chooseType(MVDialog.TypeEnum.CENTER)
                            .show();
                }

                @Override
                public void onPayFailed(CoinExchargeItegralPayDialog sheetView, String msg) {
                    sheetView.dismiss();
                    ToastUtils.showShort(msg);
                    AppContext.instance().logEvent(AppsFlyerEvent.Failed_to_top_up);
                }
            });
        }


    }

    //跳转谷歌支付act
    ActivityResultLauncher<Intent> toGooglePlayIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.e("进入支付页面回调","=========");
        if (result.getData() != null) {
            Intent intentData = result.getData();
            GoodsEntity goodsEntity = (GoodsEntity) intentData.getSerializableExtra("goodsEntity");
            if(goodsEntity!=null){
                Log.e("支付成功","===============");
            }
        }
    });

}
