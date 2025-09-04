package com.devrobin.moneytracker.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.MainViewModel.CurrencyViewModel;
import com.devrobin.moneytracker.R;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>{

    private List<String> currencies;
    private String selectedCurrency;
    private OnCurrencySelectedListener listener;
    private CurrencyViewModel currencyViewModel;

    public interface OnCurrencySelectedListener {
        void onCurrencySelected(String currency);
    }

    public CurrencyAdapter(CurrencyViewModel currencyViewModel, OnCurrencySelectedListener listener) {
        this.currencyViewModel = currencyViewModel;
        this.listener = listener;
        this.currencies = new ArrayList<>();
        this.selectedCurrency = ""; // Will be set later after initialization

        // Load supported currencies
        loadCurrencies();
    }

    private void loadCurrencies() {
        currencies.clear();
        String[] supportedCurrencies = currencyViewModel.getSupportedCurrencies();
        for (String currency : supportedCurrencies) {
            currencies.add(currency);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        String currency = currencies.get(position);
        holder.bind(currency);
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public void setSelectedCurrency(String currency) {
        Log.d("CurrencyAdapter", "Setting selected currency from '" + this.selectedCurrency + "' to '" + currency + "'");
        // Only update if the currency actually changed
        if (!currency.equals(this.selectedCurrency)) {
            this.selectedCurrency = currency;
            // Force a complete refresh of all items to ensure proper radio button states
            notifyDataSetChanged();
        }
    }

    /**
     * Clear all selections and set only the specified currency as selected
     */
    public void clearAndSetSelection(String currency) {
        Log.d("CurrencyAdapter", "Clearing all selections and setting '" + currency + "' as selected");
        this.selectedCurrency = currency;
        Log.d("CurrencyAdapter", "Selected currency is now: " + this.selectedCurrency);
        notifyDataSetChanged();
    }

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCurrencyName;
        private RadioButton rbCurrency;
        private String currentCurrency; // Track the currency for this ViewHolder

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCurrencyName = itemView.findViewById(R.id.tvCurrencyName);
            rbCurrency = itemView.findViewById(R.id.rbCurrency);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String clickedCurrency = currencies.get(position);
                    // Only proceed if this is a different currency
                    if (!clickedCurrency.equals(selectedCurrency)) {
                        setSelectedCurrency(clickedCurrency);
                        if (listener != null) {
                            listener.onCurrencySelected(clickedCurrency);
                        }
                    }
                }
            });
        }

        public void bind(String currency) {
            currentCurrency = currency; // Store the currency for this ViewHolder

            // Set currency display name
            String displayName = currencyViewModel.getCurrencyDisplayName(currency);
            tvCurrencyName.setText(displayName);

            // Set radio button state - only this currency should be checked
            boolean isChecked = currency.equals(selectedCurrency);
            rbCurrency.setChecked(isChecked);
            Log.d("CurrencyAdapter", "Binding currency: " + currency + ", selected: " + selectedCurrency + ", checked: " + isChecked);

            // Additional debug info
            if (isChecked) {
                Log.d("CurrencyAdapter", "✓ This currency is CHECKED: " + currency);
            } else {
                Log.d("CurrencyAdapter", "✗ This currency is UNCHECKED: " + currency);
            }
        }
    }
}