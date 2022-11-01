package com.fine.friendlycc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;

/**
 * @author wulei
 */
public class EvaluateObjEntity extends BaseObservable implements Parcelable {
    public static final Creator<EvaluateObjEntity> CREATOR = new Creator<EvaluateObjEntity>() {
        @Override
        public EvaluateObjEntity createFromParcel(Parcel source) {
            return new EvaluateObjEntity(source);
        }

        @Override
        public EvaluateObjEntity[] newArray(int size) {
            return new EvaluateObjEntity[size];
        }
    };
    private boolean isChoose = false;
    private Integer id;
    private String name;
    private int type;

    public EvaluateObjEntity() {
    }

    protected EvaluateObjEntity(Parcel in) {
        this.isChoose = in.readByte() != 0;
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.type = in.readInt();
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        dest.writeInt(this.type);
    }
}
