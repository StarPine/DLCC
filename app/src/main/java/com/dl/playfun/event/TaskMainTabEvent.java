package com.dl.playfun.event;

/**
 * Author: 彭石林
 * Time: 2021/11/6 15:42
 * Description: This is TaskMainTabEvent
 */
public class TaskMainTabEvent {
    //隐藏任务中心页面
    boolean TaskHiddenFlag = false;
    //tBar按钮导航
    boolean TbarClicked = false;

    public TaskMainTabEvent(boolean taskHiddenFlag) {
        TaskHiddenFlag = taskHiddenFlag;
    }

    public TaskMainTabEvent(boolean taskHiddenFlag,boolean tbarClicked) {
        TaskHiddenFlag = taskHiddenFlag;
        TbarClicked = tbarClicked;
    }

    public boolean isTbarClicked() {
        return TbarClicked;
    }

    public void setTbarClicked(boolean tbarClicked) {
        TbarClicked = tbarClicked;
    }

    public boolean isTaskHiddenFlag() {
        return TaskHiddenFlag;
    }

    public void setTaskHiddenFlag(boolean taskHiddenFlag) {
        TaskHiddenFlag = taskHiddenFlag;
    }
}
