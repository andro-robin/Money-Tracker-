package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.MainViewModel.TransViewModel;
import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.adapter.CategoryAdapter;
import com.devrobin.moneytracker.databinding.ActivityCategorySettingsBinding;

import java.util.ArrayList;

public class CategorySettings extends AppCompatActivity {

    private ActivityCategorySettingsBinding categoryBinding;
    private CategoryAdapter categoryAdapter;
    private CategoryModel categoryModel;
    private ArrayList<CategoryModel> categoryArraList;
    private CategoryAdapter.onItemClickListener listener;
    private TransViewModel transViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        categoryBinding = ActivityCategorySettingsBinding.inflate(getLayoutInflater());
        setContentView(categoryBinding.getRoot());


        setSupportActionBar(categoryBinding.mainToolbar);
        categoryBinding.mainToolbar.setTitle("Category Settings");


        setUpAdapterRecyclerView();

    }

    private void setUpAdapterRecyclerView() {

        categoryAdapter = new CategoryAdapter(this, categoryArraList, listener);

        categoryBinding.categoryLists.setLayoutManager(new LinearLayoutManager(this));
        categoryBinding.categoryLists.setHasFixedSize(true);
        categoryBinding.categoryLists.setAdapter(categoryAdapter);

        categoryAdapter.setCategoryList(categoryArraList);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                CategoryModel categoryModel = categoryArraList.get(viewHolder.getAdapterPosition());


            }
        });

    }
}