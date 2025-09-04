package com.devrobin.moneytracker.MVVM.MainViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.devrobin.moneytracker.MVVM.Repository.CurrencyRepository;

import java.util.Map;

import utils.CurrencyConverter;

public class CurrencyViewModel extends ViewModel {

    private CurrencyRepository currencyRepository;

    public CurrencyViewModel() {
        currencyRepository = new CurrencyRepository();
    }

    /**
     * Get loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return currencyRepository.getIsLoading();
    }

    /**
     * Get error message
     */
    public LiveData<String> getErrorMessage() {
        return currencyRepository.getErrorMessage();
    }

    /**
     * Get last update time
     */
    public LiveData<Long> getLastUpdateTime() {
        return currencyRepository.getLastUpdateTime();
    }

    /**
     * Set default currency
     */
    public void setDefaultCurrency(String currency) {
        CurrencyConverter.setDefaultCurrency(currency);
    }

    /**
     * Get default currency
     */
    public String getDefaultCurrency() {
        return CurrencyConverter.getDefaultCurrency();
    }

    /**
     * Get all supported currencies
     */
    public String[] getSupportedCurrencies() {
        return CurrencyConverter.getSupportedCurrencies();
    }

    /**
     * Get currency display name
     */
    public String getCurrencyDisplayName(String currency) {
        return CurrencyConverter.getCurrencyDisplayName(currency);
    }

    /**
     * Get currency symbol
     */
    public String getCurrencySymbol(String currency) {
        return CurrencyConverter.getCurrencySymbol(currency);
    }

    /**
     * Format amount with currency
     */
    public String formatAmount(double amount, String currency) {
        return CurrencyConverter.formatAmount(amount, currency);
    }

    /**
     * Convert amount between currencies
     */
    public double convert(double amount, String fromCurrency, String toCurrency) {
        return CurrencyConverter.convert(amount, fromCurrency, toCurrency);
    }

    /**
     * Convert amount to default currency
     */
    public double convertToDefault(double amount, String fromCurrency) {
        return CurrencyConverter.convertToDefault(amount, fromCurrency);
    }

    /**
     * Get current exchange rates
     */
    public Map<String, Double> getCurrentRates() {
        return CurrencyConverter.getCurrentRates();
    }

    /**
     * Fetch latest exchange rates
     */
    public void fetchLatestRates() {
        currencyRepository.fetchLatestRates();
    }

    /**
     * Refresh rates manually
     */
    public void refreshRates() {
        currencyRepository.refreshRates();
    }

    /**
     * Clear error message
     */
    public void clearError() {
        currencyRepository.clearError();
    }

    /**
     * Check if currency is supported
     */
    public boolean isCurrencySupported(String currency) {
        return CurrencyConverter.isCurrencySupported(currency);
    }


}
