package com.example.binancedemotradingapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binancedemotradingapp.Activities.MenuActivity;
import com.example.binancedemotradingapp.Models.Trade;
import com.example.binancedemotradingapp.R;
import com.example.binancedemotradingapp.TradeAdapter;
import com.example.binancedemotradingapp.TradeOnClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class WalletFragment extends Fragment implements TradeOnClickListener {


    public String userId;
    public ArrayList<Trade> myTradeList = new ArrayList<>();
    RecyclerView recyclerViewTrades;
    TradeAdapter tradeAdapter;
    TextView txtBalance;
    TradeOnClickListener tradeClickListener;
    public Boolean firstTime = true;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View returnView = inflater.inflate(R.layout.fragment_wallet, container, false);
        userId= ((MenuActivity) requireActivity()).user_id;
        recyclerViewTrades = returnView.findViewById(R.id.recyclerViewTrades);
        recyclerViewTrades.setLayoutManager(new LinearLayoutManager(getContext()));
        tradeClickListener=this;
        fillMyTradeList(returnView);
        balanceListener();

        txtBalance = returnView.findViewById(R.id.txtViewBalanceAmount);


        return returnView;
    }
    public void fillMyTradeList(View returnView){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        myRef.child("trades").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!firstTime) {
                    myTradeList.clear();
                    tradeAdapter.clear();
                }
                if (firstTime){
                    firstTime = false;
                }
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Trade oTrade = postSnapshot.getValue(Trade.class);
                    myTradeList.add(oTrade);
                }
                tradeAdapter = new TradeAdapter(getContext(), myTradeList, tradeClickListener);
                tradeAdapter.setUserId(userId);
                recyclerViewTrades.setAdapter(tradeAdapter);
                recyclerViewTrades.setLayoutManager(new LinearLayoutManager(getContext()));
                if(myTradeList.size() == 0){
                    returnView.findViewById(R.id.txtViewShowTrades).setVisibility(View.INVISIBLE);
                    returnView.findViewById(R.id.constraintLayoutHeader).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void balanceListener(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        myRef.child("wallets").child(userId + "_1").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double number = Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("oCurrency").child("dCurrency_amount").getValue()).toString());
                number = Math.round(number * 100.0) / 100.0;
                txtBalance.setText(number + " " + Objects.requireNonNull(dataSnapshot.child("oCurrency").child("sCurrency_name").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemClicked(Trade oTrade) {
    }
}