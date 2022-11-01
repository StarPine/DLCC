package com.fine.friendlycc.event;

/**
 * @author wulei
 */
public class LikeChangeEvent {
    private Object from;
    private Integer userId;
    private boolean isLike;

    public LikeChangeEvent(Integer userId, boolean isLike) {
        this.userId = userId;
        this.isLike = isLike;
    }

    public LikeChangeEvent(Object from, Integer userId, boolean isLike) {
        this.from = from;
        this.userId = userId;
        this.isLike = isLike;
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
