package com.example.binancedemotradingapp;

import com.example.binancedemotradingapp.Models.Symbol;
import com.example.binancedemotradingapp.Models.SymbolPrice;
import com.example.binancedemotradingapp.Models.SymbolResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "https://api.binance.com";
    @GET("api/v3/exchangeInfo")
    Call<SymbolResponse> getSymbols();
}
