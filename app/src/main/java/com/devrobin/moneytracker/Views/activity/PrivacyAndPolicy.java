package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityPrivacyAndPolicyBinding;

public class PrivacyAndPolicy extends AppCompatActivity {

    private ActivityPrivacyAndPolicyBinding privacyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        privacyBinding = ActivityPrivacyAndPolicyBinding.inflate(getLayoutInflater());
        setContentView(privacyBinding.getRoot());


        setSupportActionBar(privacyBinding.mainToolbar);
        privacyBinding.mainToolbar.setTitle("Privacy & Policy");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}