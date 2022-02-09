package com.example.binancedemotradingapp.Models;

public class Trade {
    private String sAmount;
    private String sBuy_price;
    private String sPair;
    private String sProfit;
    private String sSell_price;
    private String sStatus;
    private Long lTimestamp;

    public Long getlTimestamp() {
        return lTimestamp;
    }

    public void setlTimestamp(Long lTimestamp) {
        this.lTimestamp = lTimestamp;
    }

    public String getsAmount() {
        return sAmount;
    }

    public void setsAmount(String sAmount) {
        this.sAmount = sAmount;
    }

    public String getsBuy_price() {
        return sBuy_price;
    }

    public void setsBuy_price(String sBuy_price) {
        this.sBuy_price = sBuy_price;
    }

    public String getsPair() {
        return sPair;
    }

    public void setsPair(String sPair) {
        this.sPair = sPair;
    }

    public String getsProfit() {
        return sProfit;
    }

    public void setsProfit(String sProfit) {
        this.sProfit = sProfit;
    }

    public String getsSell_price() {
        return sSell_price;
    }

    public void setsSell_price(String sSell_price) {
        this.sSell_price = sSell_price;
    }

    public String getsStatus() {
        return sStatus;
    }

    public void setsStatus(String sStatus) {
        this.sStatus = sStatus;
    }
}
