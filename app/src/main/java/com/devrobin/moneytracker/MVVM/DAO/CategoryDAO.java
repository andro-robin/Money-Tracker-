package com.devrobin.moneytracker.MVVM.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.devrobin.moneytracker.MVVM.Model.CategoryModel;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert
    void insertCategory(CategoryModel categoryModel);

    @Update
    void updateCategory(CategoryModel categoryModel);

    @Delete
    void deleteCategory(CategoryModel categoryModel);

    @Query("SELECT * FROM category_table ORDER BY categoryName ASC")
    LiveData<List<CategoryModel>> getAllCategories();

    @Query("SELECT * FROM category_table WHERE categoryId = :categoryId")
    LiveData<CategoryModel> getCategoryById(int categoryId);

    @Query("SELECT * FROM category_table WHERE isDefault = 1")
    LiveData<List<CategoryModel>> getDefaultCategories();

    @Query("SELECT * FROM category_table WHERE isDefault = 0")
    LiveData<List<CategoryModel>> getCustomCategories();

    @Query("DELETE FROM category_table")
    void deleteAllCategories();
}

