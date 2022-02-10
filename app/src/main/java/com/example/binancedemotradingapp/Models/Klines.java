package com.example.binancedemotradingapp.Models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Klines {
    private String open_time;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private String close_time;
    private String quote_asset_volume;
    private String number_of_trades;
    private String taker_buy_base_asset_volume;
    private String taker_buy_quote_asset_volume;
    private String ignore;

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        long lopen_time = new BigDecimal(open_time).longValue();
        Timestamp ts=new Timestamp(lopen_time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.open_time = formatter.format(ts);
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        long lclose_time = new BigDecimal(close_time).longValue();
        Timestamp ts=new Timestamp(lclose_time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.close_time = formatter.format(ts);
    }

    public String getQuote_asset_volume() {
        return quote_asset_volume;
    }

    public void setQuote_asset_volume(String quote_asset_volume) {
        this.quote_asset_volume = quote_asset_volume;
    }

    public String getNumber_of_trades() {
        return number_of_trades;
    }

    public void setNumber_of_trades(String number_of_trades) {
        this.number_of_trades = number_of_trades;
    }

    public String getTaker_buy_base_asset_volume() {
        return taker_buy_base_asset_volume;
    }

    public void setTaker_buy_base_asset_volume(String taker_buy_base_asset_volume) {
        this.taker_buy_base_asset_volume = taker_buy_base_asset_volume;
    }

    public String getTaker_buy_quote_asset_volume() {
        return taker_buy_quote_asset_volume;
    }

    public void setTaker_buy_quote_asset_volume(String taker_buy_quote_asset_volume) {
        this.taker_buy_quote_asset_volume = taker_buy_quote_asset_volume;
    }

    public String getIgnore() {
        return ignore;
    }

    public void setIgnore(String ignore) {
        this.ignore = ignore;
    }



}
