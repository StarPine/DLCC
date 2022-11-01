package com.fine.friendlycc.viewadapter;

import androidx.databinding.BindingAdapter;

import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.widget.BurnImageView;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class BurnImageViewAdapter {
    @BindingAdapter(value = {"burnImgEnt", "addWaterMark", "burnCommand", "tapCommand"}, requireAll = false)
    public static void setImageUri(BurnImageView burnImageView, AlbumPhotoEntity burnImgEnt, boolean addWaterMark, final BindingCommand burnCommand, final BindingCommand tapCommand) {
        burnImageView.setAll(burnImgEnt, addWaterMark, burnCommand, tapCommand);
//        try {
//            String url = StringUtil.getFullImageUrl(burnImgPath);
//            if (addWaterMark) {
//                url = StringUtil.getFullImageWatermarkUrl(burnImgPath);
//            }
//            if (isBurn) {
//                if (isBurn) {
//                    if (burnStatus == 1) {
//                        burnImageView.showBurnedMsg();
//                    } else {
//                        burnImageView.showBurnMsg();
//                    }
//                } else {
//                    burnImageView.hideBurnMsg();
//                }
//                Glide.with(burnImageView.getContext())
//                        .load(url)
//                        .apply(bitmapTransform(new MvBlurTransformation()))
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.black_background)
//                                .error(R.drawable.default_placeholder_img))
//                        .into(burnImageView.getPhotoView());
//            } else {
//                burnImageView.hideBurnMsg();
//                Glide.with(burnImageView.getContext())
//                        .load(url)
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.default_placeholder_img)
//                                .error(R.drawable.default_placeholder_img))
//                        .into(burnImageView.getPhotoView());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
