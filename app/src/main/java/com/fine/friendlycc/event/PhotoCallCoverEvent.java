package com.fine.friendlycc.event;

import com.fine.friendlycc.bean.AlbumPhotoBean;

/**
 * Author: 彭石林
 * Time: 2022/8/4 10:15
 * Description: This is PhotoCallCoverEvent
 */
public class PhotoCallCoverEvent {

    private AlbumPhotoBean albumPhotoEntity;

    public AlbumPhotoBean getAlbumPhotoEntity() {
        return albumPhotoEntity;
    }

    public void setAlbumPhotoEntity(AlbumPhotoBean albumPhotoEntity) {
        this.albumPhotoEntity = albumPhotoEntity;
    }

    public PhotoCallCoverEvent(AlbumPhotoBean albumPhotoEntity) {
        this.albumPhotoEntity = albumPhotoEntity;
    }
}