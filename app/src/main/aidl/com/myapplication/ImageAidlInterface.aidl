package com.myapplication;

import java.util.List;
import com.myapplication.Image;

interface ImageAidlInterface {

    List<Image> getImage(int type);
}
