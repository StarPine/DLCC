package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

public class BroadcastBeanBean extends BaseObservable {
    /**
     * id : 1
     * is_comment : 0
     * give_count : 0
     * broadcastable_id : 1
     */

    private int id;
    @SerializedName("is_comment")
    private int isComment;
    @SerializedName("give_count")
    private int giveCount;
    @SerializedName("broadcastable_id")
    private int broadcastableId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Bindable
    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
        notifyPropertyChanged(BR.isComment);
    }

    @Bindable
    public int getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
        notifyPropertyChanged(BR.giveCount);
    }

    public int getBroadcastableId() {
        return broadcastableId;
    }

    public void setBroadcastableId(int broadcastableId) {
        this.broadcastableId = broadcastableId;
    }
}