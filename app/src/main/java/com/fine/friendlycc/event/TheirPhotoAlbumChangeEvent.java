package com.fine.friendlycc.event;

/**
 * ta的相册改变事件
 *
 * @author wulei
 */
public class TheirPhotoAlbumChangeEvent {
    public static final int TYPE_PAY_RED_PACKAGE = 1001;
    public static final int TYPE_BURN = 1002;

    private int type;
    private Integer photoId;

    public static TheirPhotoAlbumChangeEvent genPayRedPackagePhotoEvent(Integer photoId) {
        TheirPhotoAlbumChangeEvent event = new TheirPhotoAlbumChangeEvent();
        event.setType(TYPE_PAY_RED_PACKAGE);
        event.setPhotoId(photoId);
        return event;
    }

    public static TheirPhotoAlbumChangeEvent genBurnPhotoEvent(Integer photoId) {
        TheirPhotoAlbumChangeEvent event = new TheirPhotoAlbumChangeEvent();
        event.setType(TYPE_BURN);
        event.setPhotoId(photoId);
        return event;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

}
