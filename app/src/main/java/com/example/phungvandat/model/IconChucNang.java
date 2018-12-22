package com.example.phungvandat.model;

import java.io.Serializable;

/**
 * Created by PHUNGVANDAT on 4/11/2018.
 */
public class IconChucNang implements Serializable {
    Integer imgIcon;
    String chucNang;

    public IconChucNang(Integer imgIcon, String chucNang) {
        this.imgIcon = imgIcon;
        this.chucNang = chucNang;
    }

    public Integer getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(Integer imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getChucNang() {
        return chucNang;
    }

    public void setChucNang(String chucNang) {
        this.chucNang = chucNang;
    }
}
