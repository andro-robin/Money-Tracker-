package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding passwordBinding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        passwordBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(passwordBinding.getRoot());


        setSupportActionBar(passwordBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Change Password");
        }

        firebaseAuth = FirebaseAuth.getInstance();

        passwordBinding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && !TextUtils.isEmpty(user.getEmail())) {
                    firebaseAuth.sendPasswordResetEmail(user.getEmail());
                    Toast.makeText(ChangePasswordActivity.this, "Password reset email sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "No logged-in user", Toast.LENGTH_LONG).show();
                }
            }
        });


        passwordBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptChangePassword();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptChangePassword() {
        String oldPass = passwordBinding.oldPasswordEdit.getText() != null ? passwordBinding.oldPasswordEdit.getText().toString().trim() : "";
        String newPass = passwordBinding.newPasswordEdit.getText() != null ? passwordBinding.newPasswordEdit.getText().toString().trim() : "";

        boolean hasError = false;
        if (TextUtils.isEmpty(oldPass)) {
            passwordBinding.oldPasswordLayout.setError("Old password required");
            hasError = true;
        } else {
            passwordBinding.oldPasswordLayout.setError(null);
        }

        if (TextUtils.isEmpty(newPass)) {
            passwordBinding.newPasswordLayout.setError("New password required");
            hasError = true;
        } else if (newPass.length() < 6) {
            passwordBinding.newPasswordLayout.setError("New password must be at least 6 characters");
            hasError = true;
        } else {
            passwordBinding.newPasswordLayout.setError(null);
        }

        if (hasError) return;

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null || TextUtils.isEmpty(user.getEmail())) {
            Toast.makeText(this, "No logged-in user", Toast.LENGTH_LONG).show();
            return;
        }

        passwordBinding.progress.setVisibility(View.VISIBLE);
        passwordBinding.saveBtn.setEnabled(false);

        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), oldPass))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    passwordBinding.progress.setVisibility(View.GONE);
                                    passwordBinding.saveBtn.setEnabled(true);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            passwordBinding.progress.setVisibility(View.GONE);
                            passwordBinding.saveBtn.setEnabled(true);
                            Toast.makeText(ChangePasswordActivity.this, "Old password incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}