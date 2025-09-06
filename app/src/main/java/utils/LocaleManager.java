package utils;

import android.content.Context;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

public class LocaleManager {
    private LocaleManager() {}

    public static final String DEFAULT_LANGUAGE = "en";

    /**
     * Apply app locale using AppCompatDelegate for AndroidX. Works instantly across activities.
     */
    public static void applyAppLocale(@NonNull Context context, @NonNull String languageCode) {
        String tag = languageCode;
        if (tag.contains("_")) {
            tag = tag.replace('_', '-');
        }
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(tag);
        AppCompatDelegate.setApplicationLocales(appLocale);

        // Persist preference
        SharedPrefsManager.getInstance(context).saveAppLanguage(languageCode);
    }

    /**
     * Load persisted language and apply on app start (e.g., from Splash or Application).
     */
    public static void initializeLocale(@NonNull Context context) {
        String code = SharedPrefsManager.getInstance(context).getAppLanguage();
        applyAppLocale(context, code);
    }

    /**
     * Get a Java Locale from code like "en", "bn", "hi", "ar", "es".
     */
    public static Locale toLocale(String languageCode) {
        try {
            if (languageCode == null || languageCode.isEmpty()) return Locale.ENGLISH;
            if (languageCode.contains("-")) {
                String[] parts = languageCode.split("-");
                return new Locale(parts[0], parts.length > 1 ? parts[1] : "");
            } else if (languageCode.contains("_")) {
                String[] parts = languageCode.split("_");
                return new Locale(parts[0], parts.length > 1 ? parts[1] : "");
            }
            return new Locale(languageCode);
        } catch (Exception e) {
            return Locale.ENGLISH;
        }
    }
}