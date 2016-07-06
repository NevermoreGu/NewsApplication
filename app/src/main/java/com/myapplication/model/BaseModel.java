package com.myapplication.model;

public abstract class BaseModel<T> {

    protected T details;

    public String cmd;
    public int result;
    public String resultNote;
    public int pages;
    public int pageNo;
}
