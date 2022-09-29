package com.dl.playfun.event;

import com.dl.playfun.entity.AlbumPhotoEntity;

import java.util.List;

/**
 * 我的相册改变事件
 *
 * @author wulei
 */
public class MyPhotoAlbumChangeEvent {
    public static final int TYPE_REFRESH = 1001;
    public static final int TYPE_DELETE = 1002;
    public static final int TYPE_SET_BURN = 1003;
    public static final int TYPE_CANCEL_BURN = 1004;
    public static final int TYPE_SET_DATA = 1005;
    public static final int TYPE_SET_RED_PACKAGE = 1006;
    public static final int TYPE_CANCEL_RED_PACKAGE = 1007;

    private int type;
    private Integer photoId;
    private List<AlbumPhotoEntity> photos;

    public static MyPhotoAlbumChangeEvent genRefreshEvent() {
        MyPhotoAlbumChangeEvent event = new MyPhotoAlbumChangeEvent();
        event.setType(TYPE_REFRESH);
        return event;
    }

    public static MyPhotoAlbumChangeEvent genSetDataEvent(List<AlbumPhotoEntity> photos) {
        MyPhotoAlbumChangeEvent event = new MyPhotoAlbumChangeEvent();
        event.setType(TYPE_SET_DATA);
        event.setPhotos(photos);
        return event;
    }

    public static MyPhotoAlbumChangeEvent genSetBurnEvent(Integer photoId) {
        MyPhotoAlbumChangeEvent event = new MyPhotoAlbumChangeEvent();
        event.setType(TYPE_SET_BURN);
        event.setPhotoId(photoId);
        return event;
    }

    public static MyPhotoAlbumChangeEvent genCancelBurnEvent(Integer photoId) {
        MyPhotoAlbumChangeEvent event = new MyPhotoAlbumChangeEvent();
        event.setType(TYPE_CANCEL_BURN);
        event.setPhotoId(photoId);
        return event;
    }

    public static MyPhotoAlbumChangeEvent genDeleteEvent(Integer photoId) {
        MyPhotoAlbumChangeEvent event = new MyPhotoAlbumChangeEvent();
        event.setType(TYPE_DELETE);
        event.setPhotoId(photoId);
        return event;
    }

    public static MyPhotoAlbumChangeEvent genSetRedPackageEvent(Integer photoId) {
        MyPhotoAlbumChangeEvent event = new MyPhotoAlbumChangeEvent();
        event.setType(TYPE_SET_RED_PACKAGE);
        event.setPhotoId(photoId);
        return event;
    }

    public static MyPhotoAlbumChangeEvent genCancelRedPackageEvent(Integer photoId) {
        MyPhotoAlbumChangeEvent event = new MyPhotoAlbumChangeEvent();
        event.setType(TYPE_CANCEL_RED_PACKAGE);
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

    public List<AlbumPhotoEntity> getPhotos() {
        return photos;
    }

    public void setPhotos(List<AlbumPhotoEntity> photos) {
        this.photos = photos;
    }
}
