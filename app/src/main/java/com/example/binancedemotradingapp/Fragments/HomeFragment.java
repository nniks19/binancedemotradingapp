package com.example.binancedemotradingapp.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.binancedemotradingapp.Activities.MenuActivity;
import com.example.binancedemotradingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class HomeFragment extends Fragment {

    TextView txtNameSurname;
    TextView txtTotalTrades;
    TextView txtTotalSell;
    TextView txtViewTradesPlus;
    TextView txtViewTradesMinus;
    TextView txtViewProfit;
    TextView txtRegisterSecondLength;
    String userId;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userId = ((MenuActivity) requireActivity()).user_id;
        View returnView = inflater.inflate(R.layout.fragment_home, container, false);
        txtNameSurname = returnView.findViewById(R.id.txtViewWelcomeName);
        txtTotalTrades = returnView.findViewById(R.id.txtViewTotalTrades);
        txtTotalSell = returnView.findViewById(R.id.txtViewTotalSell);
        txtViewTradesPlus = returnView.findViewById(R.id.txtViewTradesPlus);
        txtViewTradesMinus = returnView.findViewById(R.id.txtViewTradesMinus);
        txtViewProfit = returnView.findViewById(R.id.txtViewProfit);
        txtRegisterSecondLength = returnView.findViewById(R.id.txtRegisterSecondLength);
        setNameSurname();
        getStats();
        return returnView;
    }
    public void setNameSurname(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();

        myRef.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtNameSurname.setText(Objects.requireNonNull(dataSnapshot.child("nameSurname").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void getStats() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        myRef.child("trades").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtTotalTrades.setText(String.valueOf(snapshot.getChildrenCount()));
                int NumOfSells = 0;
                int NumOfPositiveTrades = 0;
                int NumOfNegativeTrades = 0;
                float TotalProfit = 0.00f;
                for (DataSnapshot childd : snapshot.getChildren()) {
                    if (Objects.requireNonNull(childd.child("sStatus").getValue()).toString().equals("closed")) {
                        NumOfSells = NumOfSells + 1;
                        if(Float.parseFloat(Objects.requireNonNull(childd.child("sProfit").getValue()).toString()) > Float.parseFloat(Objects.requireNonNull(childd.child("sAmount").getValue()).toString())){
                            NumOfPositiveTrades = NumOfPositiveTrades + 1;
                            TotalProfit += Float.parseFloat(Objects.requireNonNull(childd.child("sProfit").getValue()).toString()) - Float.parseFloat(Objects.requireNonNull(childd.child("sAmount").getValue()).toString());
                        }
                        if(Float.parseFloat(Objects.requireNonNull(childd.child("sProfit").getValue()).toString()) < Float.parseFloat(Objects.requireNonNull(childd.child("sAmount").getValue()).toString())){
                            NumOfNegativeTrades = NumOfNegativeTrades + 1;
                            TotalProfit += Float.parseFloat(Objects.requireNonNull(childd.child("sProfit").getValue()).toString()) - Float.parseFloat(Objects.requireNonNull(childd.child("sAmount").getValue()).toString());
                        }
                    }
                }
                txtTotalSell.setText(String.valueOf(NumOfSells));
                txtViewTradesPlus.setText(String.valueOf(NumOfPositiveTrades));
                txtViewTradesMinus.setText(String.valueOf(NumOfNegativeTrades));
                txtViewProfit.setText(String.valueOf(TotalProfit));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("wallets").child(userId + "_1").addListenerForSingleValueEvent(new ValueEventListener() {
            
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long lRegisterTimestamp = Long.parseLong(Objects.requireNonNull(snapshot.child("lTimeStamp").getValue()).toString()) / 1000;
                refreshCurrentDateTime(lRegisterTimestamp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void refreshCurrentDateTime(Long lRegisterTimestamp) {
        new Handler().postDelayed(() -> {
            Long lCurrentTimestamp = System.currentTimeMillis() / 1000;
            Long lDiffTimestamp = lCurrentTimestamp - lRegisterTimestamp;
            //int Hours = Integer.parseInt(String.valueOf(lDiffTimestamp / 3600));
            int Hours = Integer.parseInt(String.valueOf(lDiffTimestamp));
            txtRegisterSecondLength.setText(String.valueOf(Hours));
            refreshCurrentDateTime(lRegisterTimestamp);
        }, 1000);
    }
}