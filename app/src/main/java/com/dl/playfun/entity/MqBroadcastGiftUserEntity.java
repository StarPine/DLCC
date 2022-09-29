package com.dl.playfun.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: 彭石林
 * Time: 2022/7/5 18:50
 * Description: This is MqBroadcastGiftEntity
 */
public class MqBroadcastGiftUserEntity implements Parcelable {
    private Integer id;
    private String imId;
    private Integer sex;
    private String nickname;
    private String avatar;
    private Integer isVip;
    private Integer certification;

    protected MqBroadcastGiftUserEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        imId = in.readString();
        if (in.readByte() == 0) {
            sex = null;
        } else {
            sex = in.readInt();
        }
        nickname = in.readString();
        avatar = in.readString();
        if (in.readByte() == 0) {
            isVip = null;
        } else {
            isVip = in.readInt();
        }
        if (in.readByte() == 0) {
            certification = null;
        } else {
            certification = in.readInt();
        }
    }

    public static final Creator<MqBroadcastGiftUserEntity> CREATOR = new Creator<MqBroadcastGiftUserEntity>() {
        @Override
        public MqBroadcastGiftUserEntity createFromParcel(Parcel in) {
            return new MqBroadcastGiftUserEntity(in);
        }

        @Override
        public MqBroadcastGiftUserEntity[] newArray(int size) {
            return new MqBroadcastGiftUserEntity[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getCertification() {
        return certification;
    }

    public void setCertification(Integer certification) {
        this.certification = certification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(imId);
        if (sex == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sex);
        }
        dest.writeString(nickname);
        dest.writeString(avatar);
        if (isVip == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isVip);
        }
        if (certification == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(certification);
        }
    }

    @Override
    public String toString() {
        return "MqBroadcastGiftUserEntity{" +
                "id=" + id +
                ", imId='" + imId + '\'' +
                ", sex=" + sex +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isVip=" + isVip +
                ", certification=" + certification +
                '}';
    }
}
