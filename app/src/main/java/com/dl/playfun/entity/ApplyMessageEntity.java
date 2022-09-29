package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dl.playfun.BR;
import com.google.gson.annotations.SerializedName;

/**
 * @author litchi
 */
public class ApplyMessageEntity {

    /**
     * id : 1
     * user_id : 5
     * to_user_id : 4
     * type : 1
     * apply_id : 1
     * created_at : 2020-06-14 17:02:37
     * updated_at : 2020-06-14 17:02:41
     * user : {"id":4,"nickname":"修改","avatar":null}
     * apply : {"id":1,"img":"images/微信图片_20200419083113.png","status":0}
     */

    private int id;
    private int type;
    @SerializedName("apply_id")
    private int applyId;
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    private ItemUserEntity user;
    private ApplyBean apply;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ItemUserEntity getUser() {
        return user;
    }

    public void setUser(ItemUserEntity user) {
        this.user = user;
    }

    public ApplyBean getApply() {
        return apply;
    }

    public void setApply(ApplyBean apply) {
        this.apply = apply;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public class ApplyBean extends BaseObservable {
        /**
         * id : 1
         * img : images/微信图片_20200419083113.png
         * status : 0
         */

        private int id;
        private String img;
        private int status;
        @SerializedName("is_view")
        private int isView;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Bindable
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
            notifyPropertyChanged(BR.status);
        }

        public int getIsView() {
            return isView;
        }

        public void setIsView(int isView) {
            this.isView = isView;
        }
    }
}
