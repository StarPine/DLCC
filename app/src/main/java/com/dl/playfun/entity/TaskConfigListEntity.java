package com.dl.playfun.entity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/10 14:15
 * Description: This is TaskConfigListEntity
 */
public class TaskConfigListEntity {
    //新手任务
    List<TaskConfigItemEntity> newbieTask;
    //每日任务
    List<TaskConfigItemEntity> dailyTask;

    public List<TaskConfigItemEntity> getNewbieTask() {
        return newbieTask;
    }

    public void setNewbieTask(List<TaskConfigItemEntity> newbieTask) {
        this.newbieTask = newbieTask;
    }

    public List<TaskConfigItemEntity> getDailyTask() {
        return dailyTask;
    }

    public void setDailyTask(List<TaskConfigItemEntity> dailyTask) {
        this.dailyTask = dailyTask;
    }
}
