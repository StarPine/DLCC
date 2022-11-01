package com.fine.friendlycc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

/**
 * @author wulei
 */
public class ThemeItemEntity extends BaseObservable implements Parcelable {

    public static final Creator<ThemeItemEntity> CREATOR = new Creator<ThemeItemEntity>() {
        @Override
        public ThemeItemEntity createFromParcel(Parcel source) {
            return new ThemeItemEntity(source);
        }

        @Override
        public ThemeItemEntity[] newArray(int size) {
            return new ThemeItemEntity[size];
        }
    };
    public boolean select;
    /**
     * =============主题详情（在点击节目列表进入二级页面用）=============
     **/
    @SerializedName("theme_id")
    public Integer themeId;
    public String cover;
    public String desc;
    @SerializedName("top_tool_icon")
    public String topToolIcon;
    /**
     * id : 1
     * title : 健康运动
     * icon : https://www.baidu.com
     */

    private Integer id;
    private String title;
    private String icon;
    @SerializedName("key_world")
    private String keyWord;
    @SerializedName("small_icon")
    private String smallIcon;
    @SerializedName("is_top")
    private Integer isTop;

    public ThemeItemEntity() {
    }

    protected ThemeItemEntity(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.icon = in.readString();
        this.keyWord = in.readString();
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        notifyPropertyChanged(BR.selected);
        this.select = select;
    }

    /**
     * =============主题详情（在点击节目列表进入二级页面用）=============
     **/

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTopToolIcon() {
        return topToolIcon;
    }

    public void setTopToolIcon(String topToolIcon) {
        this.topToolIcon = topToolIcon;
    }

    /**
     * =============主题详情（在点击节目列表进入二级页面用）=============
     **/


    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.icon);
        dest.writeString(this.keyWord);
    }
}
