package com.fine.friendlycc.event;

/**
 * 主界面tab-event
 */
public class MainTabEvent {
    String tabName;

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public MainTabEvent(String tabName) {
        this.tabName = tabName;
    }
}
