package com.devrobin.moneytracker.MVVM.Repository;



import com.devrobin.moneytracker.MVVM.Model.CurrencyApiResponse;
import com.devrobin.moneytracker.MVVM.Model.CurrencyResponse;
import com.devrobin.moneytracker.MVVM.Model.ExchangeRateResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyApiService {

    // Fixer.io API (free tier)
    // Base URL: http://data.fixer.io/api/

    @GET("latest")
    Call<CurrencyResponse> getLatestRates(@Query("access_key") String apiKey,
                                          @Query("base") String baseCurrency);
}
