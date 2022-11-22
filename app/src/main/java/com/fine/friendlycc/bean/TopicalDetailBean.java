package com.fine.friendlycc.bean;

import java.util.List;

public class TopicalDetailBean extends TopicalListBean {


    private List<SignsBeanBean> signs;

    @Override
    public List<SignsBeanBean> getSigns() {
        return signs;
    }

    @Override
    public void setSigns(List<SignsBeanBean> signs) {
        this.signs = signs;
    }

}