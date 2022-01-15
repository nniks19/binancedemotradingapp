package com.example.binancedemotradingapp.Models;

public class Currency {
    private String sCurrency_name;
    private Double dCurrency_amount;
    //Getteri

    public String getsCurrency_name() {
        return sCurrency_name;
    }

    public Double getdCurrency_amount() {
        return dCurrency_amount;
    }

    //Setteri

    public void setsCurrency_name(String sCurrency_name) {
        this.sCurrency_name = sCurrency_name;
    }

    public void setdCurrency_amount(Double dCurrency_amount) {
        this.dCurrency_amount = dCurrency_amount;
    }
}
