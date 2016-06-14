package com.myapplication.bean;


import java.util.List;

public class NewsList extends Entity {

    private String title;
    private String author;
    private String source;
    private String indexpic;
    private String createtime;
    private String click;
    private String desc;
    private String template;
    private String comment;
    private String info_class;
    private String state;
    private String message;
    private String videopath;
    private String starttime;
    private String endtime;
    private String content;
    private String isRead;
    private String showtype;
    private String adpic;
    private List<String> articlepic;

    public String getAdpic() {
        return adpic;
    }

    public void setAdpic(String adpic) {
        this.adpic = adpic;
    }

    public List<String> getArticlepic() {
        return articlepic;
    }

    public void setArticlepic(List<String> articlepic) {
        this.articlepic = articlepic;
    }

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInfo_class() {
        return info_class;
    }

    public void setInfo_class(String info_class) {
        this.info_class = info_class;
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

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}