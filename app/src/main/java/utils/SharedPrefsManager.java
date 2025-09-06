package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    private static final String PREF_NAME = "MoneyTrackerPrefs";
    private static final String KEY_APP_LANGUAGE = "app_language";
    private static final String KEY_DEFAULT_CURRENCY = "default_currency";
    private static final String KEY_LAST_RATES_UPDATE = "last_rates_update";
    private static final String KEY_APP_THEME = "app_theme";
    private static final String KEY_APP_FONT_SCALE = "app_font_scale";

    private static SharedPrefsManager instance;
    private SharedPreferences sharedPreferences;

    private SharedPrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Save default currency
     */
    public void saveDefaultCurrency(String currency) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_DEFAULT_CURRENCY, currency);
        editor.apply();
    }

    /**
     * Get default currency
     */
    public String getDefaultCurrency() {
        return sharedPreferences.getString(KEY_DEFAULT_CURRENCY, "BDT");
    }

    /**
     * Save app language (e.g., en, bn, hi, ar, es).
     */
    public void saveAppLanguage(String languageCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_APP_LANGUAGE, languageCode);
        editor.apply();
    }

    /**
     * Get app language with English as fallback.
     */
    public String getAppLanguage() {
        return sharedPreferences.getString(KEY_APP_LANGUAGE, "en");
    }

    /**
     * Save last rates update time
     */
    public void saveLastRatesUpdate(long timestamp) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_LAST_RATES_UPDATE, timestamp);
        editor.apply();
    }

    /**
     * Get last rates update time
     */
    public long getLastRatesUpdate() {
        return sharedPreferences.getLong(KEY_LAST_RATES_UPDATE, 0);
    }

    /**
     * Persist selected app theme (style resource id)
     */
    public void saveAppTheme(int themeResId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_APP_THEME, themeResId);
        editor.apply();
    }

    /**
     * Get saved app theme, falling back to default blue theme
     */
    public int getAppTheme() {
        return sharedPreferences.getInt(KEY_APP_THEME, com.devrobin.moneytracker.R.style.Theme_MoneyTracker);
    }

    /**
     * Persist selected app font scale (e.g., 0.85f, 1.0f, 1.15f)
     */
    public void saveAppFontScale(float scale) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_APP_FONT_SCALE, scale);
        editor.apply();
    }

    /**
     * Get saved app font scale, falling back to medium (1.0f)
     */
    public float getAppFontScale() {
        return sharedPreferences.getFloat(KEY_APP_FONT_SCALE, 1.0f);
    }

    /**
     * Clear all preferences
     */
    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
