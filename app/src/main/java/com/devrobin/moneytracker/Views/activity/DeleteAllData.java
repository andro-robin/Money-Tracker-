package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityDeleteAllDataBinding;

public class DeleteAllData extends AppCompatActivity {

    private ActivityDeleteAllDataBinding deleteDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        deleteDataBinding = ActivityDeleteAllDataBinding.inflate(getLayoutInflater());
        setContentView(deleteDataBinding.getRoot());


        setSupportActionBar(deleteDataBinding.mainToolbar);


    }
}