package com.example.binancedemotradingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ListView listView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(String param1, String param2) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        listView = (ListView) returnView.findViewById(R.id.listview);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();

        myRef.child("wallets").child(userId + "_1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtBalance.setText(dataSnapshot.child("oCurrency").child("dCurrency_amount").getValue().toString() + " " + dataSnapshot.child("oCurrency").child("sCurrency_name").getValue().toString());
                ArrayList<String> arrayList= new ArrayList<>();
                Date date = new Date(Long.parseLong(dataSnapshot.child("lTimeStamp").getValue().toString()));
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
                dateFormat.format(Long.parseLong(dataSnapshot.child("lTimeStamp").getValue().toString()));
                arrayList.add(getString(R.string.creation_date) + String.valueOf(dateFormat.format(date)));
                ArrayAdapter arrayAdapter = new ArrayAdapter(inflater.getContext(),
                        android.R.layout.simple_list_item_1,
                        arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return returnView;
    }
}