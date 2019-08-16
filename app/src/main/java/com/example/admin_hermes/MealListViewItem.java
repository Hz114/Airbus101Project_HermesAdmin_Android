package com.example.admin_hermes;

import android.graphics.drawable.Drawable;

public class MealListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private Drawable subIconDrawable;
    private String subStr ;


    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setSubIcon(Drawable subIcon) {
        subIconDrawable = subIcon ;
    }
    public void setSub(String sub) {
        subStr = sub ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
    public Drawable getSubIcon() { return  this.subIconDrawable; }
    public String getSub() {
        return this.subStr ;
    }

}
