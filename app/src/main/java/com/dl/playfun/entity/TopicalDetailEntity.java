package com.dl.playfun.entity;

import java.util.List;

public class TopicalDetailEntity extends TopicalListEntity {


    private List<SignsBeanEntity> signs;

    @Override
    public List<SignsBeanEntity> getSigns() {
        return signs;
    }

    @Override
    public void setSigns(List<SignsBeanEntity> signs) {
        this.signs = signs;
    }

}
