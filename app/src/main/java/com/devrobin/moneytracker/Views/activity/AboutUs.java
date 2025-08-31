package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityAboutUsBinding;

public class AboutUs extends AppCompatActivity {

    private ActivityAboutUsBinding aboutUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        aboutUsBinding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(aboutUsBinding.getRoot());


        setSupportActionBar(aboutUsBinding.mainToolbar);
        aboutUsBinding.mainToolbar.setTitle("About Us");

    }
}