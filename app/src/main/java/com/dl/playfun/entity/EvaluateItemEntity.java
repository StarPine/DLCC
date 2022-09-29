package com.dl.playfun.entity;

public class EvaluateItemEntity {

    private int tagId;
    private int number;
    private String name;
    private boolean selected;
    private boolean negativeEvaluate;

    public EvaluateItemEntity() {
    }

    public EvaluateItemEntity(int tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    public EvaluateItemEntity(int tagId, String name, boolean negativeEvaluate) {
        this.tagId = tagId;
        this.name = name;
        this.negativeEvaluate = negativeEvaluate;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNegativeEvaluate() {
        return negativeEvaluate;
    }

    public void setNegativeEvaluate(boolean negativeEvaluate) {
        this.negativeEvaluate = negativeEvaluate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
