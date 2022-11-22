package com.fine.friendlycc.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

/**
 * @author wulei
 */
public class ConfigItemBean extends BaseObservable implements Parcelable {
    public static final Creator<ConfigItemBean> CREATOR = new Creator<ConfigItemBean>() {
        @Override
        public ConfigItemBean createFromParcel(Parcel source) {
            return new ConfigItemBean(source);
        }

        @Override
        public ConfigItemBean[] newArray(int size) {
            return new ConfigItemBean[size];
        }
    };
    private boolean isChoose = false;
    private Integer id;
    private String name;
    private String icon;
    @SerializedName("theme_id")
    private Integer themeId;
    @SerializedName("small_icon")
    private String smallIcon;

    public ConfigItemBean() {
    }

    protected ConfigItemBean(Parcel in) {
        this.isChoose = in.readByte() != 0;
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.icon = in.readString();
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    @Bindable
    public boolean getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
        notifyPropertyChanged(BR.isChoose);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
    }
}