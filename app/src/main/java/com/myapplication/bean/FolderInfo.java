package com.myapplication.bean;

import java.util.List;

public class FolderInfo {

    public String name;
    public String path;
    public ImageInfo cover;
    public List<ImageInfo> imageInfos;

    @Override
    public boolean equals(Object o) {
        try {
            FolderInfo other = (FolderInfo) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
