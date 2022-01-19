package com.example.binancedemotradingapp;

import com.example.binancedemotradingapp.Models.Symbol;
import com.example.binancedemotradingapp.Models.SymbolPrice;
import com.example.binancedemotradingapp.Models.SymbolResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "https://api.binance.com/api/";
    @GET("v3/exchangeInfo")
    Call<SymbolResponse> getSymbols();
    @GET("v3/ticker/price")
    Call<List<SymbolPrice>> getSymbolPrices();
}
