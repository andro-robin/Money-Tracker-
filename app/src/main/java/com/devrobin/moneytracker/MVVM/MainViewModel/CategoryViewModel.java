package com.devrobin.moneytracker.MVVM.MainViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.MVVM.Repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<CategoryModel>> allCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
    }

    public void insertCategory(CategoryModel categoryModel) {
        categoryRepository.insertCategory(categoryModel);
    }

    public void updateCategory(CategoryModel categoryModel) {
        categoryRepository.updateCategory(categoryModel);
    }

    public void deleteCategory(CategoryModel categoryModel) {
        categoryRepository.deleteCategory(categoryModel);
    }

    public void deleteAllCategories() {
        categoryRepository.deleteAllCategories();
    }

    public LiveData<List<CategoryModel>> getAllCategories() {
        return allCategories;
    }

    public LiveData<CategoryModel> getCategoryById(int categoryId) {
        return categoryRepository.getCategoryById(categoryId);
    }

    public LiveData<List<CategoryModel>> getDefaultCategories() {
        return categoryRepository.getDefaultCategories();
    }

    public LiveData<List<CategoryModel>> getCustomCategories() {
        return categoryRepository.getCustomCategories();
    }
}