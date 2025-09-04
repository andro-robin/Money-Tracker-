package utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {

    private static final String TAG = "CurrencyConverter";

    // Fallback exchange rates relative to USD (as base currency)
    // These are used when API is unavailable
    private static final Map<String, Double> FALLBACK_RATES = new HashMap<>();

    static {
        // Base currency is USD
        FALLBACK_RATES.put("USD", 1.0);
        FALLBACK_RATES.put("BDT", 110.0);  // 1 USD = 110 BDT
        FALLBACK_RATES.put("EUR", 0.85);   // 1 USD = 0.85 EUR
        FALLBACK_RATES.put("GBP", 0.73);   // 1 USD = 0.73 GBP
        FALLBACK_RATES.put("JPY", 110.0);  // 1 USD = 110 JPY
        FALLBACK_RATES.put("INR", 75.0);   // 1 USD = 75 INR
        FALLBACK_RATES.put("CAD", 1.25);   // 1 USD = 1.25 CAD
        FALLBACK_RATES.put("AUD", 1.35);   // 1 USD = 1.35 AUD
        FALLBACK_RATES.put("PKR", 160.0);  // 1 USD = 160 PKR
        FALLBACK_RATES.put("RUB", 75.0);   // 1 USD = 75 RUB
    }

    // Current live rates (updated from API)
    private static Map<String, Double> liveRates = new HashMap<>(FALLBACK_RATES);

    // Default currency for the app
    private static String defaultCurrency = "BDT";
    private static SharedPrefsManager prefsManager;

    /**
     * Initialize the CurrencyConverter with context
     * @param context Application context
     */
    public static void init(Context context) {

        if (prefsManager == null) {
            prefsManager = SharedPrefsManager.getInstance(context);
            defaultCurrency = prefsManager.getDefaultCurrency();
        }

    }

    /**
     * Set the default currency for the app
     * @param currency Currency code
     */
    public static void setDefaultCurrency(String currency) {

        if (isCurrencySupported(currency)) {
            defaultCurrency = currency;
            // Save to SharedPreferences if initialized
            if (prefsManager != null) {
                prefsManager.saveDefaultCurrency(currency);
            }
        }

    }

    /**
     * Get the default currency
     * @return Default currency code
     */
    public static String getDefaultCurrency() {
        return defaultCurrency;
    }

    /**
     * Convert amount from one currency to another using live rates
     * @param amount Amount to convert
     * @param fromCurrency Source currency code
     * @param toCurrency Target currency code
     * @return Converted amount
     */
    public static double convert(double amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        try {
            // Convert to USD first, then to target currency
            double usdAmount = amount / liveRates.getOrDefault(fromCurrency, 1.0);
            return usdAmount * liveRates.getOrDefault(toCurrency, 1.0);
        } catch (Exception e) {
            Log.e(TAG, "Error converting currency: " + e.getMessage());
            // Fallback to fallback rates
            double usdAmount = amount / FALLBACK_RATES.getOrDefault(fromCurrency, 1.0);
            return usdAmount * FALLBACK_RATES.getOrDefault(toCurrency, 1.0);
        }
    }

    /**
     * Convert amount to default currency
     * @param amount Amount to convert
     * @param fromCurrency Source currency code
     * @return Amount in default currency
     */
    public static double convertToDefault(double amount, String fromCurrency) {
        return convert(amount, fromCurrency, defaultCurrency);
    }

    /**
     * Get exchange rate between two currencies
     * @param fromCurrency Source currency
     * @param toCurrency Target currency
     * @return Exchange rate
     */
    public static double getExchangeRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return 1.0;
        }

        try {
            double fromRate = liveRates.getOrDefault(fromCurrency, 1.0);
            double toRate = liveRates.getOrDefault(toCurrency, 1.0);
            return toRate / fromRate;
        } catch (Exception e) {
            Log.e(TAG, "Error getting exchange rate: " + e.getMessage());
            // Fallback to fallback rates
            double fromRate = FALLBACK_RATES.getOrDefault(fromCurrency, 1.0);
            double toRate = FALLBACK_RATES.getOrDefault(toCurrency, 1.0);
            return toRate / fromRate;
        }
    }

    /**
     * Update live exchange rates from API
     * @param newRates New exchange rates
     */
    public static void updateLiveRates(Map<String, Double> newRates) {
        if (newRates != null && !newRates.isEmpty()) {
            liveRates.clear();
            liveRates.putAll(newRates);
            Log.d(TAG, "Live rates updated successfully");
        }
    }

    /**
     * Reset to fallback rates
     */
    public static void resetToFallbackRates() {
        liveRates.clear();
        liveRates.putAll(FALLBACK_RATES);
        Log.d(TAG, "Reset to fallback rates");
    }

    /**
     * Check if currency is supported
     * @param currency Currency code
     * @return True if supported
     */
    public static boolean isCurrencySupported(String currency) {
        return FALLBACK_RATES.containsKey(currency);
    }

    /**
     * Get all supported currencies
     * @return Array of supported currency codes
     */
    public static String[] getSupportedCurrencies() {
        return FALLBACK_RATES.keySet().toArray(new String[0]);
    }

    /**
     * Get currency symbol
     * @param currency Currency code
     * @return Currency symbol
     */
    public static String getCurrencySymbol(String currency) {
        switch (currency) {
            case "USD":
                return "$";
            case "BDT":
                return "৳";
            case "EUR":
                return "€";
            case "GBP":
                return "£";
            case "JPY":
                return "¥";
            case "INR":
                return "₹";
            case "CAD":
                return "C$";
            case "AUD":
                return "A$";
            case "PKR":
                return "₨";
            case "RUB":
                return "₽";
            default:
                return currency;
        }
    }

    /**
     * Get currency name for display
     * @param currency Currency code
     * @return Currency display name
     */
    public static String getCurrencyDisplayName(String currency) {
        switch (currency) {
            case "USD":
                return "United States (USD)";
            case "BDT":
                return "Bangladeshi (Taka)";
            case "EUR":
                return "European Union (EUR)";
            case "GBP":
                return "United Kingdom (GBP)";
            case "JPY":
                return "Japan (JPY)";
            case "INR":
                return "India (Rupee)";
            case "CAD":
                return "Canada (CAD)";
            case "AUD":
                return "Australia (AUD)";
            case "PKR":
                return "Pakistan (PKR)";
            case "RUB":
                return "Russia (Ruble)";
            default:
                return currency;
        }
    }

    /**
     * Format amount with currency symbol
     * @param amount Amount to format
     * @param currency Currency code
     * @return Formatted string
     */
    public static String formatAmount(double amount, String currency) {
        String symbol = getCurrencySymbol(currency);

        // For currencies like JPY, don't show decimal places
        if (currency.equals("JPY")) {
            return symbol + String.format("%.0f", amount);
        }

        // For other currencies, show 2 decimal places
        return symbol + String.format("%.2f", amount);
    }

    /**
     * Get current rates (live or fallback)
     * @return Current exchange rates
     */
    public static Map<String, Double> getCurrentRates() {
        return new HashMap<>(liveRates);
    }
}
