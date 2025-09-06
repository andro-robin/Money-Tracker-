package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityFontSizeBinding;

import utils.FontScaleUtil;
import utils.SharedPrefsManager;
import utils.User;

public class FontSizeActivity extends AppCompatActivity {

    private ActivityFontSizeBinding fontSizeBinding;

    // Scale range: 0.85 .. 1.35 (step 0.01), SeekBar max=50
    private static final float MIN_SCALE = 0.85f;
    private static final float MAX_SCALE = 1.35f;
    private static final int SEEKBAR_MAX = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        fontSizeBinding = ActivityFontSizeBinding.inflate(getLayoutInflater());
        setContentView(fontSizeBinding.getRoot());


        setupToolbar();
        setupSeekBar();
        updatePreview(SharedPrefsManager.getInstance(this).getAppFontScale());
    }

    private void setupToolbar() {
        fontSizeBinding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupSeekBar() {
        float savedScale = SharedPrefsManager.getInstance(this).getAppFontScale();
        int progress = scaleToProgress(savedScale);
        fontSizeBinding.seekFontScale.setMax(SEEKBAR_MAX);
        fontSizeBinding.seekFontScale.setProgress(progress);
        fontSizeBinding.tvScaleLabel.setText(scaleToLabel(savedScale));

        fontSizeBinding.seekFontScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = progressToScale(progress);
                fontSizeBinding.tvScaleLabel.setText(scaleToLabel(scale));
                updatePreview(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float scale = progressToScale(seekBar.getProgress());
                SharedPrefsManager.getInstance(FontSizeActivity.this).saveAppFontScale(scale);
                FontScaleUtil.applyFontScale(getApplicationContext(), scale);
                User.recreateAllActivities();
            }
        });
    }

    private void updatePreview(float scale) {
        setScaledSize(fontSizeBinding.previewA1, 12f, scale);
        setScaledSize(fontSizeBinding.previewA2, 16f, scale);
        setScaledSize(fontSizeBinding.previewA3, 20f, scale);
        setScaledSize(fontSizeBinding.previewA4, 26f, scale);
        setScaledSize(fontSizeBinding.previewA5, 32f, scale);
    }

    private void setScaledSize(TextView tv, float baseSp, float scale) {
        tv.setTextSize(baseSp * scale);
    }

    private int scaleToProgress(float scale) {
        if (scale < MIN_SCALE) scale = MIN_SCALE;
        if (scale > MAX_SCALE) scale = MAX_SCALE;
        float ratio = (scale - MIN_SCALE) / (MAX_SCALE - MIN_SCALE);
        return Math.round(ratio * SEEKBAR_MAX);
    }

    private float progressToScale(int progress) {
        if (progress < 0) progress = 0;
        if (progress > SEEKBAR_MAX) progress = SEEKBAR_MAX;
        float ratio = progress / (float) SEEKBAR_MAX;
        return MIN_SCALE + (ratio * (MAX_SCALE - MIN_SCALE));
    }

    private String scaleToLabel(float scale) {
        if (scale < 0.95f) return "Small";
        if (scale <= 1.05f) return "Medium";
        if (scale <= 1.20f) return "Large";
        return "Extra Large";
    }
}