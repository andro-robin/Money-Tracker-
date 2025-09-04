package com.devrobin.moneytracker.Views.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devrobin.moneytracker.MVVM.MainViewModel.CategoryViewModel;
import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.adapter.CategorySettingsAdapter;
import com.devrobin.moneytracker.databinding.ActivityCategorySettingsBinding;

import java.util.ArrayList;
import java.util.List;

public class CategorySettingsActivity extends AppCompatActivity {

    private ActivityCategorySettingsBinding categoryBinding;
    private CategorySettingsAdapter categoryAdapter;
    private ArrayList<CategoryModel> categoryArraList;
    private CategoryViewModel categoryViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        categoryBinding = ActivityCategorySettingsBinding.inflate(getLayoutInflater());
        setContentView(categoryBinding.getRoot());


        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Initialize RecyclerView
        categoryArraList = new ArrayList<>();
        categoryAdapter = new CategorySettingsAdapter(this, categoryArraList, new CategorySettingsAdapter.onCategoryItemClickListener() {
            @Override
            public void categoryItemClick(CategoryModel categoryModel) {
                // Handle category item click - could be used for editing
                openAddCategoryActivity(categoryModel);
            }
        });

        // Set edit click listener
        categoryAdapter.setEditClickListener(new CategorySettingsAdapter.onEditClickListener() {
            @Override
            public void onEditClick(CategoryModel categoryModel) {
                // Handle category item click - could be used for editing
                openAddCategoryActivity(categoryModel);
            }
        });

        // Set delete click listener
        categoryAdapter.setDeleteClickListener(new CategorySettingsAdapter.onDeleteClickListener() {
            @Override
            public void onDeleteClick(CategoryModel categoryModel) {
                showDeleteConfirmation(categoryModel);
            }
        });

        // Setup RecyclerView
        categoryBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryBinding.recyclerView.setAdapter(categoryAdapter);
        categoryBinding.recyclerView.setHasFixedSize(false);

        // Setup back button
        categoryBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        categoryBinding.btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddCategoryActivity(null);
            }
        });

        categoryViewModel.getAllCategories().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                categoryArraList.clear();

                if (categoryModels != null){
                    categoryArraList.addAll(categoryModels);
                }

                categoryAdapter.notifyDataSetChanged();
            }
        });

    }

    private void openAddCategoryActivity(CategoryModel categoryModel){
        // Prevent editing default categories
        if (categoryModel != null && categoryModel.isDefault()){
            Toast.makeText(this, "Default categories cannot be edited", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, AddCategoryActivity.class);

        if (categoryModel != null){

            intent.putExtra("category_id", categoryModel.getCategoryId());
            intent.putExtra("category_name", categoryModel.getCategoryName());
            intent.putExtra("category_icon", categoryModel.getIconId());
            intent.putExtra("is_default", categoryModel.isDefault());
            Toast.makeText(this, "Editing category: " + categoryModel.getCategoryName(), Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Adding new Category", Toast.LENGTH_SHORT).show();
        }

        startActivity(intent);

    }


    private void showDeleteConfirmation(CategoryModel categoryModel){

        String message;

        if (categoryModel.isDefault()){
            message = "Are your sure you want to delete the default category " + categoryModel.getCategoryName() + "'? This action cannot be undone";
        }
        else {
            message = "Are you sure you want to delete " + categoryModel.getCategoryName() + "'? This action cannot be undone";
        }


        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage(message)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        categoryViewModel.deleteCategory(categoryModel);
                        Toast.makeText(CategorySettingsActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh category list when returning from AddCategoryActivity
        if (categoryViewModel != null){
            categoryViewModel.getAllCategories();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}