package com.example.binancedemotradingapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.binancedemotradingapp.Activities.MenuActivity;
import com.example.binancedemotradingapp.Models.Symbol;
import com.example.binancedemotradingapp.Models.SymbolPrice;
import com.example.binancedemotradingapp.Models.SymbolResponse;
import com.example.binancedemotradingapp.Models.Trade;
import com.example.binancedemotradingapp.Models.Wallet;
import com.example.binancedemotradingapp.R;
import com.example.binancedemotradingapp.RetrofitClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradeFragment extends Fragment {

    public String userId;
    private List<Symbol> allSymbols;
    public Float fCurrency_Amount;
    public Float total_amount;
    View returnView;
    TextView textView;
    TextView txtViewPrice;
    EditText editTextPin;
    EditText editTextBuyAmount;
    Button btnBuy;


    public TradeFragment() {
        // Required empty public constructor
    }


    public static TradeFragment newInstance() {
        TradeFragment fragment = new TradeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_trade, container, false);
        userId= ((MenuActivity) getActivity()).user_id;
        check_available_money();
        textView = returnView.findViewById(R.id.text_view);
        txtViewPrice = returnView.findViewById(R.id.txtViewPrice);
        editTextPin = returnView.findViewById(R.id.editTextNumberPassword);
        editTextBuyAmount = returnView.findViewById(R.id.editTextBuyAmount);
        btnBuy = returnView.findViewById(R.id.btnBuy);

        getSymbols();


        return returnView;
    }
    Handler mHandler = new Handler();

    private void getSymbols(){
        SearchableSpinner dropdown = (SearchableSpinner) returnView.findViewById(R.id.spinnerPairs);
        dropdown.setTitle(getString(R.string.choose_pair));
        Call<SymbolResponse> call = RetrofitClient.getInstance().getMyApi().getSymbols();
        call.enqueue(new Callback<SymbolResponse>() {
            @Override
            public void onResponse(Call<SymbolResponse> call, Response<SymbolResponse> response) {
                SymbolResponse mySymbolList = response.body();
                allSymbols = mySymbolList.getSymbols();
                List<String> items = new ArrayList<String>();
                for (Symbol oSymbol : allSymbols){
                    if(oSymbol.getSymbol().contains("USDT")) {
                        items.add(oSymbol.getSymbol());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(returnView.getContext(), android.R.layout.simple_list_item_1, items);
                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        boolean shouldStopLoop = false;
                        mHandler.removeCallbacksAndMessages(null);

                        String selectedValue = adapterView.getItemAtPosition(i).toString();
                        textView.setText(selectedValue);
                        getSymbolPrices(selectedValue);


                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                getSymbolPrices(selectedValue);
                                if (!shouldStopLoop) {
                                    mHandler.postDelayed(this, 5000);
                                }
                            }
                        };
                        mHandler.post(runnable);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                dropdown.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<SymbolResponse> call, Throwable t) {
                Log.d("Error", "error");
            }
        });
    }
    private void getSymbolPrices(String selectedValue){
        Call<List<SymbolPrice>> symbolPriceCall = RetrofitClient.getInstance().getMyApi().getSymbolPrices();
        symbolPriceCall.enqueue(new Callback<List<SymbolPrice>>() {
            @Override
            public void onResponse(Call<List<SymbolPrice>> call, Response<List<SymbolPrice>> response) {
                List<SymbolPrice> mySymbolPriceList = response.body();
                for (SymbolPrice oSymbolPrice : mySymbolPriceList){
                    if (oSymbolPrice.getSymbol().equals(selectedValue)){
                        String coinPrice = oSymbolPrice.getPrice();

                        Float fCoinPrice = Float.parseFloat(coinPrice);
                        txtViewPrice.setText(Float.toString(fCoinPrice));
                        btnBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(getContext());
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
                                DatabaseReference myRef = database.getReference();
                                if ( editTextBuyAmount.getText().toString().trim().length() == 0){
                                    Toast.makeText(getContext(), getString(R.string.amount_missing), Toast.LENGTH_SHORT).show();
                                } else if( Float.parseFloat(editTextBuyAmount.getText().toString()) > fCurrency_Amount){
                                    Toast.makeText(getContext(), getString(R.string.amount_greater_than), Toast.LENGTH_SHORT).show();
                                }else if( Float.parseFloat(editTextBuyAmount.getText().toString()) < 10) {
                                    Toast.makeText(getContext(), getString(R.string.amount_greater_than_ten), Toast.LENGTH_SHORT).show();
                                } else {
                                    myRef.child("users").child(userId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String enteredPin = editTextPin.getText().toString();
                                            String user_real_pin = dataSnapshot.child("pin").getValue().toString();
                                            if (enteredPin.equals(user_real_pin)) {
                                                total_amount = Float.parseFloat(editTextBuyAmount.getText().toString());
                                                total_amount = total_amount / fCoinPrice;
                                                builder.setMessage(getString(R.string.info_buy_confirmation) + "\n" + Float.toString(total_amount) + " " + oSymbolPrice.getSymbol().replace("USDT", "") + "?\n" + getString(R.string.info_buy_confirmation_extended) + " " + editTextBuyAmount.getText().toString() + " USD")
                                                        .setCancelable(false)
                                                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                // Pritisak na tipku da
                                                                saveTrade(oSymbolPrice.getSymbol(), oSymbolPrice.getPrice());
                                                                Toast.makeText(getContext(), getString(R.string.yes_confirmation),
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                //  Pritisak na tipku ne
                                                                dialog.cancel();
                                                                Toast.makeText(getContext(), getString(R.string.no_confirmation),
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                AlertDialog alert = builder.create();
                                                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                                                    private static final int AUTO_DISMISS_MILLIS = 6000;

                                                    @Override
                                                    public void onShow(final DialogInterface dialog) {
                                                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                                                        final CharSequence negativeButtonText = defaultButton.getText();
                                                        new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                                                            @Override
                                                            public void onTick(long millisUntilFinished) {
                                                                defaultButton.setText(String.format(
                                                                        Locale.getDefault(), "%s (%d)",
                                                                        negativeButtonText,
                                                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                                                                ));
                                                            }

                                                            @Override
                                                            public void onFinish() {
                                                                if (((AlertDialog) dialog).isShowing()) {
                                                                    dialog.dismiss();
                                                                    Toast.makeText(getContext(), getString(R.string.time_expired),
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }.start();
                                                    }
                                                });

                                                alert.setTitle(getString(R.string.info_buy_title));
                                                alert.show();
                                            } else {
                                                Toast.makeText(getContext(), getString(R.string.invalid_pin), Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<List<SymbolPrice>> call, Throwable t) {

            }
            public boolean User_buy_amount_validation(String user_buy_amount){
                if(!user_buy_amount.isEmpty()) {
                    if(user_buy_amount.contains(".")) {
                        if (user_buy_amount.split(".").length == 2) {
                            return true;
                        }else{
                            return false;
                        }
                    }
                    return true;
                }else{
                    return false;
                }
            }
        });
    }
    public void check_available_money(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        Boolean buy_possible = false;
        myRef.child("wallets").child(userId + "_1").child("oCurrency").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fCurrency_Amount = Float.parseFloat(snapshot.child("dCurrency_amount").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void saveTrade(String symbol_bought_pair, String symbol_bought_price){
    //total_amount -> Upisana kolicina od korisnika koliko zeli uloziti dolara
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference myRef = database.getReference().child("trades");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Trade oTrade = new Trade();
                oTrade.setsAmount(editTextBuyAmount.getText().toString());
                oTrade.setsPair(symbol_bought_pair);
                oTrade.setsStatus("active");
                oTrade.setsBuy_price(symbol_bought_price);
                oTrade.setsProfit(" ");
                oTrade.setsSell_price(" ");
                oTrade.setlTimestamp(System.currentTimeMillis());
                if(snapshot.child(userId).exists()){
                    String key =  myRef.child(userId).push().getKey();           //this returns the unique key generated by firebase
                    myRef.child(userId).child(key).setValue(oTrade);
                    subtractFromUser();

                } else{
                    myRef.setValue(userId);
                    String key =  myRef.child(userId).push().getKey();           //this returns the unique key generated by firebas
                    myRef.child(userId).child(key).setValue(oTrade);
                    subtractFromUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void subtractFromUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRefWallet = database.getReference().child("wallets").child(userId + "_1").child("oCurrency");
        myRefWallet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Float current_balance = Float.parseFloat(snapshot.child("dCurrency_amount").getValue().toString());
                String sCurrency_Name = snapshot.child("sCurrency_name").getValue().toString();
                myRefWallet.child("dCurrency_amount").setValue(current_balance - Float.parseFloat(editTextBuyAmount.getText().toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}