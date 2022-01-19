package com.example.binancedemotradingapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.binancedemotradingapp.Models.Symbol;
import com.example.binancedemotradingapp.Models.SymbolPrice;
import com.example.binancedemotradingapp.Models.SymbolResponse;
import com.example.binancedemotradingapp.R;
import com.example.binancedemotradingapp.RetrofitClient;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradeFragment extends Fragment {


    private List<Symbol> allSymbols;
    View returnView;
    TextView textView;
    TextView txtViewPrice;
    Button btnBuy;
    Button btnSell;

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
        textView = returnView.findViewById(R.id.text_view);
        txtViewPrice = returnView.findViewById(R.id.txtViewPrice);
        btnBuy = returnView.findViewById(R.id.btnBuy);
        btnSell = returnView.findViewById(R.id.btnSell);
        getSymbols();

        return returnView;
    }
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
                    items.add(oSymbol.getSymbol());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(returnView.getContext(), android.R.layout.simple_list_item_1, items);
                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedValue = adapterView.getItemAtPosition(i).toString();
                        textView.setText(selectedValue);
                        getSymbolPrices(selectedValue);
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
                        txtViewPrice.setText(oSymbolPrice.getPrice());
                        btnBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TradeBuyFragment fragment2 = new TradeBuyFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(this, fragment2);
                                fragmentTransaction.commit();
                            }
                        });
                        btnSell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SymbolPrice>> call, Throwable t) {

            }
        });
    }
}