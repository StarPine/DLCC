package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author liaosf
 * @date 2021/12/17 18:41
 */
public class TaskRewardReceiveEntity {
//            "reward_type": 1,
//            "task_type": "新手任務",
//            "task_name": "完善個人檔案",
//            "msg": "恭喜獲得新台幣 +10.00"
    @SerializedName("reward_type")
    private Integer rewardType;

    @SerializedName("task_type")
    private String taskType;

    @SerializedName("task_name")
    private String taskName;

    private String msg;

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TaskRewardReceiveEntity{" +
                "rewardType=" + rewardType +
                ", taskType='" + taskType + '\'' +
                ", taskName='" + taskName + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

}
