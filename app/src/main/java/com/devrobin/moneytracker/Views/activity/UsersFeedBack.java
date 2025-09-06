package com.devrobin.moneytracker.Views.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.devrobin.moneytracker.MVVM.MainViewModel.FeedBackViewModel;
import com.devrobin.moneytracker.MVVM.TransactionDatabase;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityUsersFeedBackBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

public class UsersFeedBack extends AppCompatActivity {

    private ActivityUsersFeedBackBinding feedBackBinding;
    private FeedBackViewModel feedBackViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        feedBackBinding = ActivityUsersFeedBackBinding.inflate(getLayoutInflater());
        setContentView(feedBackBinding.getRoot());

        feedBackViewModel = new ViewModelProvider(this).get(FeedBackViewModel.class);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.title_feedback));
        }


        feedBackBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int rating = Math.round(feedBackBinding.ratingBar.getRating());
                String feedback = feedBackBinding.etFeedback.getText() != null ? feedBackBinding.etFeedback.getText().toString().trim() : "";
                String contact = feedBackBinding.etContact.getText() != null ? feedBackBinding.etContact.getText().toString().trim() : "";

                if (rating <= 0) {
                    Toast.makeText(UsersFeedBack.this, R.string.feedback_select_rating, Toast.LENGTH_SHORT).show();
                    return;
                }

                TransactionDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(view, R.string.feedback_submitted, Snackbar.LENGTH_LONG).show();
                        maybePromptForPlayStore(rating);
                        finish();
                    }
                });

            }
        });

    }

    private void maybePromptForPlayStore(int rating) {
        if (rating >= 4) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.feedback_love_prompt_title)
                    .setMessage(R.string.feedback_love_prompt_msg)
                    .setPositiveButton(R.string.feedback_rate_playstore, (dialog, which) -> {
                        openPlayStore();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        } else {
            Toast.makeText(this, R.string.feedback_thanks_improve, Toast.LENGTH_SHORT).show();
        }
    }

    private void openPlayStore() {
        try {
            String packageName = getPackageName();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            String packageName = getPackageName();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            startActivity(intent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}