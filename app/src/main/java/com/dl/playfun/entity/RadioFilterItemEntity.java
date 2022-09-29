package com.dl.playfun.entity;

public  class RadioFilterItemEntity<T> {
    private String name;
    private T data;

    public RadioFilterItemEntity(String name, T data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return name;
    }
}