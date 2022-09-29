package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dl.playfun.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/8/9 15:16
 * Description: This is TaskConfigItemEntity
 */
public class TaskConfigItemEntity extends BaseObservable {
    private Integer id;
    @SerializedName("task_type")
    private Integer taskType;//任务类型、每日、新手
    private String name;
    private String icon;
    private String link;
    private String slug;
    @SerializedName("reward_type")
    private Integer rewardType;
    @SerializedName("reward_value")
    private String rewardValue;
    private Integer status;

    @SerializedName("finish_number")
    private Integer finishNumber;
    @SerializedName("total_number")
    private Integer totalNumber;

    //自定义刷新使用字段
    private Boolean isRefresh;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSulg() {
        return slug;
    }

    public void setSulg(String slug) {
        this.slug = slug;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(String rewardValue) {
        this.rewardValue = rewardValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFinishNumber() {
        return finishNumber;
    }

    public void setFinishNumber(Integer finishNumber) {
        this.finishNumber = finishNumber;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public void setRefresh(Boolean refresh) {
        isRefresh = refresh;
    }

    @Bindable
    public Boolean getRefresh() {
        return isRefresh;
    }

    public void setRefresh(Boolean refresh, Integer status) {
        isRefresh = refresh;
        setStatus(status);
        notifyPropertyChanged(BR.refresh);
    }

    @Override
    public String toString() {
        return "TaskConfigItemEntity{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", link='" + link + '\'' +
                ", slug='" + slug + '\'' +
                ", rewardType=" + rewardType +
                ", rewardValue='" + rewardValue + '\'' +
                ", status=" + status +
                ", finishNumber=" + finishNumber +
                ", totalNumber=" + totalNumber +
                ", isRefresh=" + isRefresh +
                '}';
    }
}
