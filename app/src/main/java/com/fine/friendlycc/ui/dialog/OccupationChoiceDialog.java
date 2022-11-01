package com.fine.friendlycc.ui.dialog;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;

import com.fine.friendlycc.R;

/**
 * 选择职业
 *
 * @author yuanzw
 */
public class OccupationChoiceDialog extends BaseDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.CityDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP);
//        mWindow.setWindowAnimations(R.style.TopAnimation);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1]);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_occupation_choice;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);

    }

}
