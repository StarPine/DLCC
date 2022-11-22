package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/8/13 12:02
 * Description: 收获地址
 */
public class AddressBean extends BaseObservable {

    private Integer id;
    private Integer user_id;
    private String contacts;//联系人
    private String city;//城市
    private String are;//区街道
    private String address;//详细地址
    private String phone;
    @SerializedName("is_default")
    private Integer isDefault; //添加默认地址

    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    private Boolean checked;

    public AddressBean() {
    }

    @Bindable
    public Boolean getChecked() {
        return isDefault == 1;
    }

    public void setChecked(Boolean checked, Integer status) {
        this.checked = checked;
        setIsDefault(status);
        notifyPropertyChanged(BR.checked);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAre() {
        return are;
    }

    public void setAre(String are) {
        this.are = are;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", contacts='" + contacts + '\'' +
                ", city='" + city + '\'' +
                ", are='" + are + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", isDefault=" + isDefault +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}