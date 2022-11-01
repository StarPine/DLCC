package com.fine.friendlycc.event;

import com.fine.friendlycc.entity.AlbumPhotoEntity;

/**
 * Author: 彭石林
 * Time: 2022/8/4 10:15
 * Description: This is PhotoCallCoverEvent
 */
public class PhotoCallCoverEvent {

    private AlbumPhotoEntity albumPhotoEntity;

    public AlbumPhotoEntity getAlbumPhotoEntity() {
        return albumPhotoEntity;
    }

    public void setAlbumPhotoEntity(AlbumPhotoEntity albumPhotoEntity) {
        this.albumPhotoEntity = albumPhotoEntity;
    }

    public PhotoCallCoverEvent(AlbumPhotoEntity albumPhotoEntity) {
        this.albumPhotoEntity = albumPhotoEntity;
    }
}
