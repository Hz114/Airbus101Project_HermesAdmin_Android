package com.example.admin_hermes;

import android.graphics.drawable.Drawable;

public class OrderListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private String subStr ;
    private String subtitleStr ;


    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setSub(String sub) { subStr = sub ; }
    public void setSubtitle(String subtitle) { subtitleStr = subtitle; }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() { return this.descStr ; }
    public String getSub() { return this.subStr ; }
    public String getSubtitle() { return this.subtitleStr ; }
}
