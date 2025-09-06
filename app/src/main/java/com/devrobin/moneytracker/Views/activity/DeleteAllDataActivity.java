package com.devrobin.moneytracker.Views.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.devrobin.moneytracker.MVVM.TransactionDatabase;
import com.devrobin.moneytracker.databinding.ActivityDeleteAllDataBinding;
import com.google.firebase.auth.FirebaseAuth;

import utils.SharedPrefsManager;

public class DeleteAllDataActivity extends AppCompatActivity {

    private ActivityDeleteAllDataBinding deleteDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        deleteDataBinding = ActivityDeleteAllDataBinding.inflate(getLayoutInflater());
        setContentView(deleteDataBinding.getRoot());

        setSupportActionBar(deleteDataBinding.mainToolbar);


        deleteDataBinding.deleteAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAndDelete();
            }
        });
    }

    private void confirmAndDelete() {

        new AlertDialog.Builder(this)
                .setTitle("Delete All Data")
                .setMessage("Are you sure you want to permanently delete all app data? This action cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        performDelete();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performDelete() {

        // 1) Clear Room database tables
        try {
            TransactionDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    TransactionDatabase.getInstance(getApplicationContext()).clearAllTables();
                }
            });
        } catch (Exception ignored) { }

        // 2) Clear shared preferences
        SharedPrefsManager.getInstance(this).clearAll();

        // 3) Sign out from Firebase
        try { FirebaseAuth.getInstance().signOut(); } catch (Exception ignored) { }

        Toast.makeText(this, "All data deleted", Toast.LENGTH_SHORT).show();


        // 4) Navigate back to login and clear task stack
        Intent intent = new Intent(DeleteAllDataActivity.this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}