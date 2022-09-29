package com.dl.playfun.event;

/**
 * 头像修改
 *
 * @author wulei
 */
public class AvatarChangeEvent {
    private String avatarPath;

    public AvatarChangeEvent(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
