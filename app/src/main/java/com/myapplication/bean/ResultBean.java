package com.myapplication.bean;

import org.w3c.dom.Comment;

/**
 * 操作结果实体类
 * 
 */

public class ResultBean extends Base {

    private Result result;

    private Comment comment;

    private int relation;

    public Result getResult() {
	return result;
    }

    public void setResult(Result result) {
	this.result = result;
    }

    public int getRelation() {
	return relation;
    }

    public void setRelation(int relation) {
	this.relation = relation;
    }

    public Comment getComment() {
	return comment;
    }

    public void setComment(Comment comment) {
	this.comment = comment;
    }
}
