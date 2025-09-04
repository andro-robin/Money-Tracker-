package com.devrobin.moneytracker.MVVM.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class CategoryModel {
    @PrimaryKey(autoGenerate = true)
    private int categoryId;
    private String categoryName;
    private int iconId;
    private boolean isDefault;

    public CategoryModel() {
    }

    public CategoryModel(String categoryName, int iconId) {
        this.categoryName = categoryName;
        this.iconId = iconId;
        this.isDefault = false;
    }

    public CategoryModel(String categoryName, int iconId, boolean isDefault) {
        this.categoryName = categoryName;
        this.iconId = iconId;
        this.isDefault = isDefault;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

}
