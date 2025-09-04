package com.devrobin.moneytracker.MVVM.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CurrencyApiResponse {
    @SerializedName("meta")
    private MetaInfo meta;

    @SerializedName("data")
    private Map<String, CurrencyData> data;

    public static class MetaInfo {
        @SerializedName("last_updated_at")
        private String lastUpdatedAt;

        public String getLastUpdatedAt() { return lastUpdatedAt; }
    }

    public static class CurrencyData {
        @SerializedName("code")
        private String code;

        @SerializedName("value")
        private double value;

        public String getCode() { return code; }
        public double getValue() { return value; }
    }

    // Getters
    public MetaInfo getMeta() { return meta; }
    public Map<String, CurrencyData> getData() { return data; }

    // Check if response is successful
    public boolean isSuccess() {
        return meta != null && data != null && !data.isEmpty();
    }

    // Get rates as Map<String, Double> for easy conversion
    public Map<String, Double> getRates() {
        if (data == null) return null;

        Map<String, Double> rates = new java.util.HashMap<>();
        for (Map.Entry<String, CurrencyData> entry : data.entrySet()) {
            rates.put(entry.getKey(), entry.getValue().getValue());
        }
        return rates;
    }
}