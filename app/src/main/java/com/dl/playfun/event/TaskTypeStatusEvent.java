package com.dl.playfun.event;

/**
 * @Description：
 * @Author： liaosf
 * @Date： 2021/12/20 15:37
 * 修改备注：
 */
public class TaskTypeStatusEvent {
    private int dayAccost; //主動搭訕1次
    private int accostThree; //主动搭訕3人
    private int dayCommentNews;  //評論動態1次
    private int dayGiveNews; //點讚動態1次

    public TaskTypeStatusEvent() {
    }

    public int getDayAccost() {
        return dayAccost;
    }

    public void setDayAccost(int dayAccost) {
        this.dayAccost = dayAccost;
    }

    public int getAccostThree() {
        return accostThree;
    }

    public void setAccostThree(int accostThree) {
        this.accostThree = accostThree;
    }

    public int getDayCommentNews() {
        return dayCommentNews;
    }

    public void setDayCommentNews(int dayCommentNews) {
        this.dayCommentNews = dayCommentNews;
    }

    public int getDayGiveNews() {
        return dayGiveNews;
    }

    public void setDayGiveNews(int dayGiveNews) {
        this.dayGiveNews = dayGiveNews;
    }

    @Override
    public String toString() {
        return "TaskTypeStatusEvent{" +
                "dayAccost=" + dayAccost +
                ", accostThree=" + accostThree +
                ", dayCommentNews=" + dayCommentNews +
                ", dayGiveNews=" + dayGiveNews +
                '}';
    }
}
