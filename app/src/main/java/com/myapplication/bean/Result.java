package com.myapplication.bean;

import java.io.Serializable;

/**
 * 数据操作结果实体类
 * 
 */
public class Result implements Serializable {

    private int errorCode;

    private String errorMessage;

    public boolean OK() {
	return errorCode == 1;
    }

    public int getErrorCode() {
	return errorCode;
    }

    public void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }
}
