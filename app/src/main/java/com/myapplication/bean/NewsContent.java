package com.myapplication.bean;

public class NewsContent extends Entity {

    private String state; //状态
    private String message; //提示信息
    private String title; //标题
    private String info_class; //类别
    private String author;
    private String source;
    private String indexpic;
    private String content;
    private String createtime; //日期
    private String click; //点击次数
    private String desc; //简介
    private String comment; //评论数
    private String videopath; //播放地址
    private int starttime; //开始时间
    private int endtime; //结束时间
    private String allow_comment; //评论 0不容许评论 1容许
    private int iscomplete; //报名 0为没有报名1已经报名
    private int count; //报名 人数


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInfo_class() {
        return info_class;
    }

    public void setInfo_class(String info_class) {
        this.info_class = info_class;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIndexpic() {
        return indexpic;
    }

    public void setIndexpic(String indexpic) {
        this.indexpic = indexpic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public String getAllow_comment() {
        return allow_comment;
    }

    public void setAllow_comment(String allow_comment) {
        this.allow_comment = allow_comment;
    }

    public int getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(int iscomplete) {
        this.iscomplete = iscomplete;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
