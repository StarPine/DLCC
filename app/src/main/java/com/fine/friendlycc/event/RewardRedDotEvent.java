package com.fine.friendlycc.event;

/**
 * @Description：奖励小红点事件
 * @Author： liaosf
 * @Date： 2021/12/18 19:43
 * 修改备注：
 */
public class RewardRedDotEvent {
    boolean isHaveReward;

    public RewardRedDotEvent(boolean isHaveReward) {
        this.isHaveReward = isHaveReward;
    }

    public boolean isHaveReward() {
        return isHaveReward;
    }

    public void setHaveReward(boolean haveReward) {
        isHaveReward = haveReward;
    }
}
