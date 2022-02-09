package com.example.binancedemotradingapp.Models;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Wallet {
    private String sWallet_id;
    private String sUser_id;
    private Currency oCurrency;
    private Long lTimeStamp;
    // Getteri
    public String getsWallet_id(){
        return sWallet_id;
    }

    public String getsUser_id(){
        return sUser_id;
    }

    public Currency getoCurrency() {
        return oCurrency;
    }

    public Long getlTimeStamp() {
        return lTimeStamp;
    }
// Setteri

    public void setsUser_id(String sUser_id) {
        this.sUser_id = sUser_id;
    }
    public void setsWallet_id(String sWallet_id) {
        this.sWallet_id = sWallet_id;
    }
    public void setsCurrency(Currency oCurrency) {
        this.oCurrency = oCurrency;
    }
    public void setlTimeStamp(Long lTimeStamp) {
        this.lTimeStamp = lTimeStamp;
    }

    // Rad sa bazom podataka
    public void writeNewWallet(String user_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        Wallet oWallet = new Wallet();
        Currency oCurrency = new Currency();
        oCurrency.setsCurrency_name("USDT");
        oCurrency.setdCurrency_amount(500.00);
        oWallet.setsWallet_id(user_id + "_1");
        oWallet.setsUser_id(user_id);
        oWallet.setsCurrency(oCurrency);
        Long datetime = System.currentTimeMillis();
        // ?? get R.id.string... pitat
        oWallet.setlTimeStamp(datetime);
        myRef.child("wallets").child(String.valueOf(oWallet.getsWallet_id())).setValue(oWallet);
    }
}
