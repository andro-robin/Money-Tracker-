package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.devrobin.moneytracker.MVVM.MainViewModel.CategoryViewModel;
import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityAddCategoryBinding;

public class AddCategoryActivity extends AppCompatActivity {

    private ActivityAddCategoryBinding addCategoryBinding;
    private CategoryViewModel categoryViewModel;
    private CategoryModel currentCategory;
    private boolean isEditMode = false;
    private int selectedIconId = R.drawable.others; // Default icon

    // Available icons
    private final int[] iconIds = {
            R.drawable.others, R.drawable.food, R.drawable.shopping, R.drawable.travel, R.drawable.rent,
            R.drawable.electronics, R.drawable.clothing, R.drawable.health, R.drawable.pet,
            R.drawable.gift, R.drawable.phone, R.drawable.beauty, R.drawable.social
    };
    private final int[] iconResources = {
            R.drawable.others, R.drawable.food, R.drawable.shopping, R.drawable.travel, R.drawable.rent,
            R.drawable.electronics, R.drawable.clothing, R.drawable.health, R.drawable.pet,
            R.drawable.gift, R.drawable.phone, R.drawable.beauty, R.drawable.social
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        addCategoryBinding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(addCategoryBinding.getRoot());

        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Check if editing existing category
        int categoryId = getIntent().getIntExtra("category_id", -1);
        if (categoryId != -1) {
            isEditMode = true;
            loadCategory(categoryId);
            addCategoryBinding.tvTitle.setText("Edit Category");
        }

        setupViews();
        setupClickListeners();
        setupIconGrid();


    }

    private void setupViews() {
        // Set default icon
        addCategoryBinding.ivSelectedIcon.setImageResource(iconResources[0]);
    }

    private void setupClickListeners() {

        // Back button
        addCategoryBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Save button
        addCategoryBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCategory();
            }
        });

    }


    private void setupIconGrid() {
        // Set up icon selection grid
        for (int i = 0; i < addCategoryBinding.iconGrid.getChildCount() && i < iconResources.length; i++) {
            View iconView = addCategoryBinding.iconGrid.getChildAt(i);
            if (iconView instanceof androidx.appcompat.widget.AppCompatImageView) {

                androidx.appcompat.widget.AppCompatImageView imageView = (androidx.appcompat.widget.AppCompatImageView) iconView;
                final int iconIndex = i; // Make effectively final
                imageView.setImageResource(iconResources[iconIndex]);
                imageView.setTag(iconIds[iconIndex]);
                imageView.setOnClickListener(v -> selectIcon(imageView, iconIds[iconIndex]));

            }
        }
    }

    private void selectIcon(androidx.appcompat.widget.AppCompatImageView imageView, int iconId) {
        // Reset all icon backgrounds
        for (int i = 0; i < addCategoryBinding.iconGrid.getChildCount(); i++) {
            View child = addCategoryBinding.iconGrid.getChildAt(i);
            if (child instanceof androidx.appcompat.widget.AppCompatImageView) {
                child.setBackgroundResource(R.drawable.icon_background);
            }
        }

        // Set selected icon background
        imageView.setBackgroundResource(R.drawable.selected_icon_background);
        selectedIconId = iconId;
        addCategoryBinding.ivSelectedIcon.setImageResource(iconId);
    }

    private void loadCategory(int categoryId) {
        categoryViewModel.getCategoryById(categoryId).observe(this, categoryModel -> {
            if (categoryModel != null) {
                currentCategory = categoryModel;
                addCategoryBinding.etCategoryName.setText(categoryModel.getCategoryName());
                selectedIconId = categoryModel.getIconId();
                addCategoryBinding.ivSelectedIcon.setImageResource(selectedIconId);

                // Highlight the selected icon in the grid
                for (int i = 0; i < addCategoryBinding.iconGrid.getChildCount(); i++) {
                    View child = addCategoryBinding.iconGrid.getChildAt(i);
                    if (child instanceof androidx.appcompat.widget.AppCompatImageView) {
                        if ((Integer) child.getTag() == selectedIconId) {
                            child.setBackgroundResource(R.drawable.selected_icon_background);
                        } else {
                            child.setBackgroundResource(R.drawable.icon_background);
                        }
                    }
                }
            }
        });
    }

    private void saveCategory() {
        String categoryName = addCategoryBinding.etCategoryName.getText().toString().trim();

        // Validation
        if (categoryName.isEmpty()) {
            addCategoryBinding.etCategoryName.setError("Please enter category name");
            return;
        }

        if (isEditMode && currentCategory != null) {
            // Update existing category
            currentCategory.setCategoryName(categoryName);
            currentCategory.setIconId(selectedIconId);

            categoryViewModel.updateCategory(currentCategory);
            Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Create new category
            CategoryModel newCategory = new CategoryModel(categoryName, selectedIconId, false);

            categoryViewModel.insertCategory(newCategory);
            Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}