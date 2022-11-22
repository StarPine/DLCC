package com.fine.friendlycc.bean;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/10 14:15
 * Description: This is TaskConfigListBean
 */
public class TaskConfigListBean {
    //新手任务
    List<TaskConfigItemBean> newbieTask;
    //每日任务
    List<TaskConfigItemBean> dailyTask;

    public List<TaskConfigItemBean> getNewbieTask() {
        return newbieTask;
    }

    public void setNewbieTask(List<TaskConfigItemBean> newbieTask) {
        this.newbieTask = newbieTask;
    }

    public List<TaskConfigItemBean> getDailyTask() {
        return dailyTask;
    }

    public void setDailyTask(List<TaskConfigItemBean> dailyTask) {
        this.dailyTask = dailyTask;
    }
}