package com.devrobin.moneytracker.Views.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devrobin.moneytracker.MVVM.MainViewModel.CurrencyViewModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.adapter.CurrencyAdapter;
import com.devrobin.moneytracker.databinding.ActivityCurrencySettingsBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import utils.SharedPrefsManager;

public class CurrencySettingsActivity extends AppCompatActivity implements CurrencyAdapter.OnCurrencySelectedListener{

    private static final String TAG = "CurrencySettings";

    private ActivityCurrencySettingsBinding currencyBinding;
    private CurrencyViewModel currencyViewModel;
    private CurrencyAdapter currencyAdapter;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        currencyBinding = ActivityCurrencySettingsBinding.inflate(getLayoutInflater());
        setContentView(currencyBinding.getRoot());

        // Initialize SharedPrefsManager first
        prefsManager = SharedPrefsManager.getInstance(this);

        // Initialize CurrencyConverter
        utils.CurrencyConverter.init(this);

        // Initialize ViewModel
        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        // Initialize views
        setupRecyclerView();
        setupObservers();
        setupClickListeners();

        // Load initial data
        loadInitialData();

    }


    private void setupRecyclerView() {
        // Ensure CurrencyConverter is initialized before creating adapter
        utils.CurrencyConverter.init(this);

        currencyAdapter = new CurrencyAdapter(currencyViewModel, this);
        currencyBinding.rvCurrencies.setLayoutManager(new LinearLayoutManager(this));
        currencyBinding.rvCurrencies.setAdapter(currencyAdapter);

        // Load saved currency from SharedPreferences
        loadSavedCurrency();
    }


    private void loadSavedCurrency() {
        // Get the saved currency from SharedPreferences
        String savedCurrency = prefsManager.getDefaultCurrency();
        Log.d(TAG, "Loading saved currency: " + savedCurrency);

        // Set the selection in the adapter
        if (savedCurrency != null && !savedCurrency.isEmpty()) {
            currencyAdapter.clearAndSetSelection(savedCurrency);
            Log.d(TAG, "Adapter selection set to: " + savedCurrency);
        } else {
            Log.w(TAG, "No saved currency found, using default");
            // Set default selection
            currencyAdapter.clearAndSetSelection("BDT");
        }
    }

    private void setupObservers() {

        // Observe loading state
        currencyViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {

                currencyBinding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                currencyBinding.btnRefreshRates.setEnabled(!isLoading);
            }
        });

        // Observe error messages
        currencyViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    currencyBinding.tvStatusMessage.setText(errorMessage);
                    currencyBinding.layoutStatus.setVisibility(View.VISIBLE);
                    Toast.makeText(CurrencySettingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Observe last update time
        currencyViewModel.getLastUpdateTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long timestamp) {
                if (timestamp != null && timestamp > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                    String formattedDate = sdf.format(new Date(timestamp));
                    currencyBinding.tvLastUpdate.setText("Last Updated: " + formattedDate);
                    currencyBinding.layoutStatus.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void setupClickListeners() {

        currencyBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        currencyBinding.btnRefreshRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencyViewModel.clearError();
                currencyViewModel.fetchLatestRates();
            }
        });
    }



    private void loadInitialData() {
        // Fetch latest rates
        currencyViewModel.fetchLatestRates();
    }


    @Override
    public void onCurrencySelected(String currency) {
        // Update default currency in the system
        currencyViewModel.setDefaultCurrency(currency);

        // Show confirmation
        String message = "Default currency set to " + currencyViewModel.getCurrencyDisplayName(currency);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Update the adapter selection to reflect the change
        currencyAdapter.clearAndSetSelection(currency);

        Log.d("CurrencySettings", "Currency changed to: " + currency + ", adapter updated");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload saved currency when returning to the screen
        loadSavedCurrency();
    }


}


