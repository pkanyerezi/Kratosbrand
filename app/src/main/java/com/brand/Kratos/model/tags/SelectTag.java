package com.brand.Kratos.model.tags;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectTag {

    private String name;
    private boolean isSelected = false;

    private int id;
    public SelectTag(String name, boolean isSelected,Integer id) {
        this.id = id;
        this.name = name;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}