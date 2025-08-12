package com.devrobin.moneytracker.Views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.devrobin.moneytracker.databinding.ActivityLogInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogInBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();


        binding.btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent logIntent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(logIntent);

            }
        });

        binding.regTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goActivity = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(goActivity);

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
//        firebaseAuth.addAuthStateListener(authStateListener);

        if (currentUser != null){
            Intent goHome = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(goHome);
            finish();
        }

    }
}