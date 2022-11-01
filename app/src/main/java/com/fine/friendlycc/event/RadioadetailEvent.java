package com.fine.friendlycc.event;

/**
 * 真人認證
 *
 * @author wulei
 */
public class RadioadetailEvent {
    public String radioaType;
    public int type;//1:删除 2：评论关闭开启 3：报名成功 4：节目结束报名 5：节目评论  6：点赞
    public int id;//广播id
    public int isComment;//评论关闭开启
    public String content;
    public Integer toUserId;
    public String toUserName;
    public String userName;

    public RadioadetailEvent() {
    }

    public String getRadioaType() {
        return radioaType;
    }

    public void setRadioaType(String radioaType) {
        this.radioaType = radioaType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
