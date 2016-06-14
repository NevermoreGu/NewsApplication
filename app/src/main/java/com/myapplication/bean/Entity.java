package com.myapplication.bean;

/**
 * 实体类
 */
public abstract class Entity extends Base {

    protected int id;

    protected String key;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
