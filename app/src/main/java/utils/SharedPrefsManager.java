package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    private static final String PREF_NAME = "MoneyTrackerPrefs";
    private static final String KEY_DEFAULT_CURRENCY = "default_currency";
    private static final String KEY_LAST_RATES_UPDATE = "last_rates_update";

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
     * Clear all preferences
     */
    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
