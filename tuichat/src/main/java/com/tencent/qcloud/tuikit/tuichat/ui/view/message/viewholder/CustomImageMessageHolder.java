package com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.TUIThemeManager;
import com.tencent.qcloud.tuicore.component.imageEngine.impl.CornerTransform;
import com.tencent.qcloud.tuicore.component.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.message.CustomImageMessageBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;

/**
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/6/29 15:47
 * 修改备注：DL add 自定义图片holder
 */
public class CustomImageMessageHolder extends MessageContentHolder{

    private final ImageView customImage;
    private static final int DEFAULT_MAX_SIZE = 540;
    private static final int DEFAULT_RADIUS = 20;


    public CustomImageMessageHolder(View itemView) {
        super(itemView);
        customImage = itemView.findViewById(R.id.iv_custom_image);
    }

    @Override
    public void layoutVariableViews(TUIMessageBean msg, int position) {
        msgArea.setBackground(null);
        String imagePath = TUIChatUtils.getFullImageUrl(((CustomImageMessageBean) msg).getImgPath());
        CornerTransform transform = new CornerTransform(TUILogin.getAppContext(), DEFAULT_RADIUS);
        RequestOptions options = new RequestOptions().centerCrop().transform(transform);
        Glide.with(TUIChatService.getAppContext())
                .asBitmap()
                .load(imagePath)
                .error(R.drawable.chat_custom_image_error)
                .centerCrop()
                .placeholder(R.drawable.chat_custom_image_load)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options)
                .into(new ImageViewTarget<Bitmap>(customImage) {
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource != null) {
                            customImage.setImageBitmap(resource);
                        }
                    }
                });
        customImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onImageClick(msg);
                }
            }
        });

        customImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMessageLongClick(view, position, msg);
                }
                return true;
            }
        });
    }

    @Override
    public int getVariableLayout() {
        return R.layout.custom_image_message_layout;
    }

    private ViewGroup.LayoutParams getImageParams(ViewGroup.LayoutParams params, final CustomImageMessageBean msg) {
        if (msg.getImgWidth() == 0 || msg.getImgHeight() == 0) {
            return params;
        }
        if (msg.getImgWidth() > msg.getImgHeight()) {
            params.width = DEFAULT_MAX_SIZE;
            params.height = DEFAULT_MAX_SIZE * msg.getImgHeight() / msg.getImgWidth();
        } else {
            params.width = DEFAULT_MAX_SIZE * msg.getImgWidth() / msg.getImgHeight();
            params.height = DEFAULT_MAX_SIZE;
        }
        return params;
    }

    private ViewGroup.LayoutParams getImageParams(ViewGroup.LayoutParams params, final int width, final int height) {
        if (width == 0 || height == 0) {
            return params;
        }
//        params.width = 480;
//        params.height = DEFAULT_MAX_SIZE;
//        if (width > height) {
//            params.width = DEFAULT_MAX_SIZE;
//            params.height = DEFAULT_MAX_SIZE * height / width;
//        } else {
//            params.width = DEFAULT_MAX_SIZE * width / height;
//            params.height = DEFAULT_MAX_SIZE;
//        }
        return params;
    }

}
