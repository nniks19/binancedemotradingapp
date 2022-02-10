package com.example.binancedemotradingapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Objects;

public class WalletFragment extends Fragment {


    ListView listViewTrades;


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
        String userId = ((MenuActivity) requireActivity()).user_id;
        View returnView = inflater.inflate(R.layout.fragment_wallet, container, false);
        TextView txtBalance = (TextView) returnView.findViewById(R.id.txtViewBalanceAmount);
        listViewTrades = (ListView) returnView.findViewById(R.id.listViewTrades);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();

        myRef.child("wallets").child(userId + "_1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtBalance.setText(Objects.requireNonNull(dataSnapshot.child("oCurrency").child("dCurrency_amount").getValue()) + " " + Objects.requireNonNull(dataSnapshot.child("oCurrency").child("sCurrency_name").getValue()));
                ArrayList<String> arrayList= new ArrayList<>();
                /*Date date = new Date(Long.parseLong(dataSnapshot.child("lTimeStamp").getValue().toString()));
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
                dateFormat.format(Long.parseLong(dataSnapshot.child("lTimeStamp").getValue().toString()));
                arrayList.add(getString(R.string.creation_date) + String.valueOf(dateFormat.format(date)));
                ArrayAdapter arrayAdapter = new ArrayAdapter(inflater.getContext(),
                        android.R.layout.simple_list_item_1,
                        arrayList);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        myRef.child("trades").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList= new ArrayList<>();
                arrayList.add("Ovdje dodati tradeove");
                ArrayAdapter arrayAdapter = new ArrayAdapter(inflater.getContext(),
                        android.R.layout.simple_list_item_1,
                        arrayList);
                listViewTrades.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return returnView;
    }
}