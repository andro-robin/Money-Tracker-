package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityUsersFeedBackBinding;

public class UsersFeedBack extends AppCompatActivity {

    private ActivityUsersFeedBackBinding feedBackBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        feedBackBinding = ActivityUsersFeedBackBinding.inflate(getLayoutInflater());
        setContentView(feedBackBinding.getRoot());



    }
}