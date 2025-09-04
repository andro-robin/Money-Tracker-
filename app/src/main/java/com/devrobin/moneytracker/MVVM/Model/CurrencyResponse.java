package com.devrobin.moneytracker.MVVM.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CurrencyResponse {


    @SerializedName("success")
    private boolean success;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("base")
    private String base;

    @SerializedName("date")
    private String date;

    @SerializedName("rates")
    private Map<String, Double> rates;

    @SerializedName("error")
    private ErrorInfo error;

    public static class ErrorInfo {
        @SerializedName("code")
        private int code;

        @SerializedName("type")
        private String type;

        @SerializedName("info")
        private String info;

        public int getCode() { return code; }
        public String getType() { return type; }
        public String getInfo() { return info; }
    }

    // Getters
    public boolean isSuccess() { return success; }
    public long getTimestamp() { return timestamp; }
    public String getBase() { return base; }
    public String getDate() { return date; }
    public Map<String, Double> getRates() { return rates; }
    public ErrorInfo getError() { return error; }

    // Check if response has error
    public boolean hasError() {
        return !success || error != null;
    }

    // Get error message
    public String getErrorMessage() {
        if (error != null) {
            return error.getInfo();
        }
        return "Unknown error occurred";
    }
}
