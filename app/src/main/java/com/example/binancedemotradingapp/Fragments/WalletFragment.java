package com.example.binancedemotradingapp.Fragments;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WalletFragment extends Fragment {


    public String userId = ((MenuActivity) requireActivity()).user_id;
    public ArrayList<Trade> myTradeList;
    RecyclerView recyclerViewTrades;

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
        recyclerViewTrades = returnView.findViewById(R.id.recyclerViewTrades);
        fillMyTradeList();
        TextView txtBalance = returnView.findViewById(R.id.txtViewBalanceAmount);



        return returnView;
    }
    public void fillMyTradeList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        myRef.child("trades").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Trade oTrade = postSnapshot.getValue(Trade.class);
                    myTradeList.add(oTrade);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}