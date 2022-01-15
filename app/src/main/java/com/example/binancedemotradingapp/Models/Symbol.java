package com.example.binancedemotradingapp.Models;

public class Symbol {
     private String symbol;
     private String status;
     private String baseAsset;
     private Integer baseAssetPrecision;
     private String quoteAsset;
     private Integer quotePrecision;
     private Integer quoteAssetPrecision;
     private Integer baseComissionPrecision;
     private Integer quoteComissionPrecision;

     public String getSymbol() {
          return symbol;
     }

     public void setSymbol(String symbol) {
          this.symbol = symbol;
     }

     public String getStatus() {
          return status;
     }

     public void setStatus(String status) {
          this.status = status;
     }

     public String getBaseAsset() {
          return baseAsset;
     }

     public void setBaseAsset(String baseAsset) {
          this.baseAsset = baseAsset;
     }

     public Integer getBaseAssetPrecision() {
          return baseAssetPrecision;
     }

     public void setBaseAssetPrecision(Integer baseAssetPrecision) {
          this.baseAssetPrecision = baseAssetPrecision;
     }

     public String getQuoteAsset() {
          return quoteAsset;
     }

     public void setQuoteAsset(String quoteAsset) {
          this.quoteAsset = quoteAsset;
     }

     public Integer getQuotePrecision() {
          return quotePrecision;
     }

     public void setQuotePrecision(Integer quotePrecision) {
          this.quotePrecision = quotePrecision;
     }

     public Integer getQuoteAssetPrecision() {
          return quoteAssetPrecision;
     }

     public void setQuoteAssetPrecision(Integer quoteAssetPrecision) {
          this.quoteAssetPrecision = quoteAssetPrecision;
     }

     public Integer getBaseComissionPrecision() {
          return baseComissionPrecision;
     }

     public void setBaseComissionPrecision(Integer baseComissionPrecision) {
          this.baseComissionPrecision = baseComissionPrecision;
     }

     public Integer getQuoteComissionPrecision() {
          return quoteComissionPrecision;
     }

     public void setQuoteComissionPrecision(Integer quoteComissionPrecision) {
          this.quoteComissionPrecision = quoteComissionPrecision;
     }
}
