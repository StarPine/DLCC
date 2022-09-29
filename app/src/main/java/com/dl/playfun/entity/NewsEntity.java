package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dl.playfun.BR;
import com.dl.playfun.utils.ListUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsEntity extends BaseObservable {

    /**
     * id : 1
     * content : 内容
     * images : ["https://www.apiview.com","https://www.apiview.com"]
     * created_at : 2020-05-07 17:00:05
     * user_id : 5
     * user : {"id":5,"nickname":"广州","sex":0,"is_vip":0,"certification":0}
     * broadcast : {"id":3,"is_comment":0,"give_count":0,"broadcastable_id":1}
     */

    private int id;
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("user_id")
    private int userId;
    private BaseUserBeanEntity user;
    private BroadcastBeanEntity broadcast;
    private List<String> images;
    @SerializedName("is_give")
    private int isGive;
    @SerializedName("give_count")
    private int giveCount;
    private List<GiveUserBeanEntity> give_user;
    private List<CommentEntity> comment;
    //评论总数
    @SerializedName("comment_number")
    private Integer commentNumber;
    private Integer giveSize;
    //视频MP4
    private String video;
    //心情
    @SerializedName("news_type")
    private Integer newsType;
    @SerializedName("game_channel")
    private String gamechannel;

    //当前用户im id
    @SerializedName("imId")
    private String ImUserId;
    //对方用户 IM iD
    @SerializedName("toImId")
    private String ImToUserId;

    public String getImUserId() {
        return ImUserId;
    }

    public void setImUserId(String imUserId) {
        ImUserId = imUserId;
    }

    public String getImToUserId() {
        return ImToUserId;
    }

    public void setImToUserId(String imToUserId) {
        ImToUserId = imToUserId;
    }
    public String getGamechannel() {
        return gamechannel;
    }

    public void setGamechannel(String gamechannel) {
        this.gamechannel = gamechannel;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public Integer getNewsType() {
        return newsType;
    }

    public void setNewsType(Integer newsType) {
        this.newsType = newsType;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Bindable
    public Integer getGiveSize() {
        if (giveSize == null) {
            if (ListUtils.isEmpty(give_user)) {
                return 0;
            } else {
                return give_user.size();
            }
        } else {
            return giveSize;
        }
    }

    public void setGiveSize(Integer giveSize) {
        this.giveSize = giveSize;
        notifyPropertyChanged(BR.giveSize);
    }

    @Bindable
    public int getIsGive() {
        return isGive;
    }

    public void setIsGive(int isGive) {
        this.isGive = isGive;
        notifyPropertyChanged(BR.isGive);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BaseUserBeanEntity getUser() {
        return user;
    }

    public void setUser(BaseUserBeanEntity user) {
        this.user = user;
    }

    public BroadcastBeanEntity getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(BroadcastBeanEntity broadcast) {
        this.broadcast = broadcast;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
    }

    @Bindable
    public List<GiveUserBeanEntity> getGive_user() {
        return give_user;
    }

    public void setGive_user(List<GiveUserBeanEntity> give_user) {
        this.give_user = give_user;
        notifyPropertyChanged(BR.give_user);
    }

    @Bindable
    public List<CommentEntity> getComment() {
        return comment;
    }

    public void setComment(List<CommentEntity> comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }

}
