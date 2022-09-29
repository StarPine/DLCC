package com.dl.playfun.event;

/**
 * Author: 彭石林
 * Time: 2021/10/22 10:38
 * Description: This is RefreshUserDataEvent
 */
public class RefreshUserDataEvent {
    private String sound;
    private Integer soundTime;

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Integer getSoundTime() {
        return soundTime;
    }

    public void setSoundTime(Integer soundTime) {
        this.soundTime = soundTime;
    }

    public RefreshUserDataEvent(String sound, Integer soundTime) {
        this.sound = sound;
        this.soundTime = soundTime;
    }

    public RefreshUserDataEvent() {
    }
}
