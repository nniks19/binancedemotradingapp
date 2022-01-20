package com.example.binancedemotradingapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


    private List<Symbol> allSymbols;
    View returnView;
    TextView textView;
    TextView txtViewPrice;
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
        textView = returnView.findViewById(R.id.text_view);
        txtViewPrice = returnView.findViewById(R.id.txtViewPrice);
        btnBuy = returnView.findViewById(R.id.btnBuy);
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
                    if(oSymbol.getSymbol().contains("USDT")) {
                        items.add(oSymbol.getSymbol());
                    }
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
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(getContext());


                                builder.setMessage(getString(R.string.info_buy_confirmation) + " " + oSymbolPrice.getSymbol() + "?\n" + getString(R.string.info_buy_confirmation_extended) + " " + oSymbolPrice.getPrice())
                                        .setCancelable(false)
                                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // Pritisak na tipku da
                                                Toast.makeText(getContext(),getString(R.string.yes_confirmation),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Pritisak na tipku ne
                                                dialog.cancel();
                                                Toast.makeText(getContext(),getString(R.string.no_confirmation),
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
                                                    Toast.makeText(getContext(),getString(R.string.time_expired),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }.start();
                                    }
                                });

                                alert.setTitle(getString(R.string.info_buy_title));
                                alert.show();
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