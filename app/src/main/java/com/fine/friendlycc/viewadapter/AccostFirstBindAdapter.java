package com.fine.friendlycc.viewadapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.fine.friendlycc.R;

/**
 * Author: 彭石林
 * Time: 2022/4/8 21:15
 * Description: 单次搭讪成功后播放动画
 */
public class AccostFirstBindAdapter {

    @BindingAdapter(value = {"accountCollect"}, requireAll = false)
    public static void setAccostFirst(LottieAnimationView itemLottie, Boolean accountCollect) {
        try {
            if (accountCollect!=null && accountCollect.booleanValue()) {
                if (itemLottie != null) {
                    itemLottie.setImageAssetsFolder("images/");
                    itemLottie.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            itemLottie.removeAnimatorListener(this);
                            itemLottie.setVisibility(View.GONE);
                        }
                    });
                    if (!itemLottie.isAnimating()) {
                        itemLottie.setVisibility(View.VISIBLE);
                        itemLottie.setAnimation(R.raw.accost_animation);
                        itemLottie.playAnimation();
                    }
                }
            }else{
                if (itemLottie!=null && itemLottie.isAnimating()) {
                    itemLottie.clearAnimation();
                    itemLottie.removeAllAnimatorListeners();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
