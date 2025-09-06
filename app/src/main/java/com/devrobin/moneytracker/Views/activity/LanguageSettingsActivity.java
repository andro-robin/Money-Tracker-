package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityLanguageSettingsBinding;

import utils.LocaleManager;
import utils.SharedPrefsManager;

public class LanguageSettingsActivity extends AppCompatActivity {

    private RadioGroup languageGroup;
    private TextView saveHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_settings);

        languageGroup = findViewById(R.id.languageGroup);
        saveHint = findViewById(R.id.tvInfo);

        // Preselect saved language
        String saved = SharedPrefsManager.getInstance(this).getAppLanguage();
        checkLanguage(saved);

        languageGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String code = mapRadioToCode(checkedId);
            if (code != null) {
                // Only dynamic values changed; static strings remain from resources
                LocaleManager.applyAppLocale(this, code);
                // Recreate all open Activities to rebind resources (splash, main, etc.)
                utils.User.recreateAllActivities();
            }
        });

        updatePreviewTexts();
    }

    private void checkLanguage(String code) {
        int id = mapCodeToRadio(code);
        if (id != View.NO_ID) {
            RadioButton rb = findViewById(id);
            if (rb != null) rb.setChecked(true);
        }
    }

    private int mapCodeToRadio(String code) {
        if (code == null) return View.NO_ID;
        switch (code) {
            case "en": return R.id.rbEnglish;
            case "bn": return R.id.rbBangla;
            case "hi": return R.id.rbHindi;
            case "ar": return R.id.rbArabic;
            case "es": return R.id.rbSpanish;
        }
        return R.id.rbEnglish;
    }

    private String mapRadioToCode(int checkedId) {
        if (checkedId == R.id.rbEnglish) return "en";
        if (checkedId == R.id.rbBangla) return "bn";
        if (checkedId == R.id.rbHindi) return "hi";
        if (checkedId == R.id.rbArabic) return "ar";
        if (checkedId == R.id.rbSpanish) return "es";
        return null;
    }

    private void updatePreviewTexts() {
        setTitle(getString(R.string.language_settings_title));
        if (saveHint != null) {
            saveHint.setText(getString(R.string.language_settings_info));
        }
    }
}