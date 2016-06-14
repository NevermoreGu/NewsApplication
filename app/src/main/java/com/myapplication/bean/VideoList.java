package com.myapplication.bean;

public class VideoList extends Entity{

    private String title;
    private String author;
    private String source;
    private String indexpic;
    private String videopath;
    private int createtime;
    private int click;
    private String desc;
    private String template;
    private int comment;
    private String info_class;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setIndexpic(String indexpic) {
        this.indexpic = indexpic;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public void setInfo_class(String info_class) {
        this.info_class = info_class;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public String getIndexpic() {
        return indexpic;
    }

    public String getVideopath() {
        return videopath;
    }

    public int getCreatetime() {
        return createtime;
    }

    public int getClick() {
        return click;
    }

    public String getDesc() {
        return desc;
    }

    public String getTemplate() {
        return template;
    }

    public int getComment() {
        return comment;
    }

    public String getInfo_class() {
        return info_class;
    }
}
