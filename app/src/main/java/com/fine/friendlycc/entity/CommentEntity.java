package com.fine.friendlycc.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

public class CommentEntity extends BaseObservable {
    /**
     * id : 1
     * user_id : 5
     * to_user_id : null
     * content : 好东西
     * created_at : 2020-05-08 14:37:20
     * user : {"id":5,"nickname":"小四"}
     * touser : {"id":5,"nickname":"小四"}
     */

    private Integer id;
    @SerializedName("user_id")
    private Integer userId;
    @SerializedName("to_user_id")
    private Integer toUserId;
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    private UserBean user;
    private TouserBean touser;

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }


    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);
    }

    @Bindable
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
    }

    @Bindable
    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
        notifyPropertyChanged(BR.toUserId);
    }

    @Bindable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        notifyPropertyChanged(BR.createdAt);
    }

    @Bindable
    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public TouserBean getTouser() {
        return touser;
    }

    public void setTouser(TouserBean touser) {
        this.touser = touser;
        notifyPropertyChanged(BR.touser);
    }

    public static class UserBean extends BaseObservable {
        /**
         * id : 5
         * nickname : 小四
         */

        private int id;
        private String nickname;

        @Bindable
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
            notifyPropertyChanged(BR.id);
        }

        @Bindable
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
            notifyPropertyChanged(BR.nickname);
        }
    }

    public static class TouserBean extends BaseObservable {
        /**
         * id : 5
         * nickname : 小四
         */

        private int id;
        private String nickname;

        @Bindable
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
            notifyPropertyChanged(BR.id);
        }

        @Bindable
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
            notifyPropertyChanged(BR.nickname);
        }
    }
}

