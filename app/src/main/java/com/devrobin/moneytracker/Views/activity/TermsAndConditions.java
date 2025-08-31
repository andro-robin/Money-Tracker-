package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityTermsAndConditionsBinding;

public class TermsAndConditions extends AppCompatActivity {

    private ActivityTermsAndConditionsBinding termsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        termsBinding = ActivityTermsAndConditionsBinding.inflate(getLayoutInflater());
        setContentView(termsBinding.getRoot());


        setSupportActionBar(termsBinding.mainToolbar);
        termsBinding.mainToolbar.setTitle("Terms & Condition");

    }
}