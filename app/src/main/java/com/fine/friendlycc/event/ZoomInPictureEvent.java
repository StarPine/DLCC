package com.fine.friendlycc.event;

/**
 * @Description：放大图片
 * @Author： liaosf
 * @Date： 2022/1/10 19:27
 * 修改备注：
 */
public class ZoomInPictureEvent {
    String drawable;

    public ZoomInPictureEvent(String drawable) {
        this.drawable = drawable;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }
}
