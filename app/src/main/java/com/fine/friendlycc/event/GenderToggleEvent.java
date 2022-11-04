package com.fine.friendlycc.event;

/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/4 15:32
 */
public class GenderToggleEvent {
    private Boolean isMale;
    private int index;

    public Boolean isMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public GenderToggleEvent() {
    }

    public GenderToggleEvent(Boolean isMale, int index) {
        this.isMale = isMale;
        this.index = index;
    }
}
