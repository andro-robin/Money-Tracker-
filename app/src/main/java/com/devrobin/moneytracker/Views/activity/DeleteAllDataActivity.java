package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.devrobin.moneytracker.databinding.ActivityDeleteAllDataBinding;

public class DeleteAllDataActivity extends AppCompatActivity {

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