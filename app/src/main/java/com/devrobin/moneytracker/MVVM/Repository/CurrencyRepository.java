package com.devrobin.moneytracker.MVVM.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devrobin.moneytracker.MVVM.Model.CurrencyResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.CurrencyConverter;

public class CurrencyRepository {

    private static final String TAG = "CurrencyRepository";

    // Fixer.io API configuration
    private static final String FIXER_API_KEY = "YOUR_FIXER_API_KEY"; // Get from https://fixer.io/
    private static final String FIXER_BASE_URL = "http://data.fixer.io/api/";


    private CurrencyApiService fixerService;


    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Long> lastUpdateTime = new MutableLiveData<>();

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    public CurrencyRepository() {
        initializeApiService();
    }

    private void initializeApiService() {
        // Initialize Fixer.io service
        Retrofit fixerRetrofit = new Retrofit.Builder()
                .baseUrl(FIXER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fixerService = fixerRetrofit.create(CurrencyApiService.class);
    }

    /**
     * Fetch latest exchange rates from Fixer.io API
     */
    public void fetchLatestRates() {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        // Check if API key is configured
        if ("YOUR_FIXER_API_KEY".equals(FIXER_API_KEY)) {
            // No API key configured, use fallback rates
            useFallbackRates("Please configure your Fixer.io API key in CurrencyRepository.java");
            return;
        }

        // Fetch from Fixer.io
        fetchFromFixer();
    }

    private void fetchFromFixer() {
        Call<CurrencyResponse> call = fixerService.getLatestRates(FIXER_API_KEY, "USD");
        call.enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrencyResponse currencyResponse = response.body();
                    if (!currencyResponse.hasError()) {
                        Map<String, Double> rates = currencyResponse.getRates();
                        if (rates != null) {
                            updateRates(rates);
                            return;
                        }
                    } else {
                        // API returned an error
                        String error = currencyResponse.getErrorMessage();
                        Log.e(TAG, "Fixer API error: " + error);
                        useFallbackRates("Fixer API error: " + error);
                        return;
                    }
                }
                // If response is not successful, use fallback rates
                useFallbackRates("Failed to get response from Fixer API");
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                Log.e(TAG, "Fixer API failed: " + t.getMessage());
                useFallbackRates("Network error: " + t.getMessage());
            }
        });

    }


    private void updateRates(Map<String, Double> rates) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                // Convert rates to USD base if needed
                Map<String, Double> usdBasedRates = new HashMap<>();
                usdBasedRates.put("USD", 1.0);

                for (Map.Entry<String, Double> entry : rates.entrySet()) {
                    String currency = entry.getKey();
                    Double rate = entry.getValue();
                    if (rate != null && !currency.equals("USD")) {
                        usdBasedRates.put(currency, rate);
                    }
                }

                // Update the CurrencyConverter with new rates
                CurrencyConverter.updateLiveRates(usdBasedRates);

                // Update UI
                lastUpdateTime.postValue(System.currentTimeMillis());
                isLoading.postValue(false);
                Log.d(TAG, "Exchange rates updated successfully from Fixer.io API");

            }
        });

    }

    private void useFallbackRates(String reason) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Reset to fallback rates
                CurrencyConverter.resetToFallbackRates();

                // Update UI
                lastUpdateTime.postValue(System.currentTimeMillis());
                isLoading.postValue(false);
                errorMessage.postValue("Using fallback exchange rates. " + reason);
                Log.w(TAG, "Using fallback exchange rates: " + reason);
            }
        });

    }




    /**
     * Get loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Get error message
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Get last update time
     */
    public LiveData<Long> getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Clear error message
     */
    public void clearError() {
        errorMessage.setValue(null);
    }

    /**
     * Refresh rates manually
     */
    public void refreshRates() {
        fetchLatestRates();
    }


}
