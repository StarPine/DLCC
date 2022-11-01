package com.fine.friendlycc.ui.mine.wallet.girl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentWalletDollarMoneyBinding;
import com.fine.friendlycc.ui.certification.certificationmale.CertificationMaleFragment;


/**
 * Author: 彭石林
 * Time: 2021/12/3 14:41
 * Description: 台币收益页面
 */
public class TwDollarMoneyFragment extends BaseToolbarFragment<FragmentWalletDollarMoneyBinding, TwDollarMoneyViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_wallet_dollar_money;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public TwDollarMoneyViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(TwDollarMoneyViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.loadDatas(1);
        viewModel.uc.startRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.autoRefresh();
            }
        });
        viewModel.uc.certification.observe(this,event -> {
            MVDialog.getInstance(TwDollarMoneyFragment.this.getContext())
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

        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.finishRefresh(100);
            }
        });

        //监听上拉加载完成
        viewModel.uc.finishLoadmore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.finishLoadMore(100);
            }
        });

    }

}
