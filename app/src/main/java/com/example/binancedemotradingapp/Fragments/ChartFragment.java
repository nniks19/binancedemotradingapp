package com.example.binancedemotradingapp.Fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.binancedemotradingapp.Models.Klines;
import com.example.binancedemotradingapp.Models.Symbol;
import com.example.binancedemotradingapp.Models.SymbolResponse;
import com.example.binancedemotradingapp.R;
import com.example.binancedemotradingapp.RetrofitClient;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChartFragment extends Fragment {
    RadioButton checkedRadioBtn;
    RadioGroup radioGroupTimeFrame;
    CandleStickChart candleChart;
    ImageButton imgButton;
    View returnView;
    public boolean isRunning = false;
    public String makeNewLine = "\n";
    public String chosenSymbol = "BTCUSDT";
    public String chosenTimeFrame = "15m";
    public int redCandleCount = 0;
    public int greenCandleCount = 0;
    public int equalCandleCount = 0;
    private List<Symbol> allSymbols;
    public List<Klines> lKlines = new ArrayList<>();
    public ArrayList<String> dates= new ArrayList<>();
    public ArrayList<CandleEntry> arrayCandles = new ArrayList<>();

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        returnView = inflater.inflate(R.layout.fragment_chart, container, false);
        candleChart = (CandleStickChart) returnView.findViewById(R.id.candlestick_chart);
        candleChart.setTouchEnabled(true);
        candleChart.setNoDataText(getString(R.string.data_loading));
        Paint p = candleChart.getPaint(Chart.PAINT_INFO);
        p.setColor(Color.rgb(0,0,0));
        Description description = candleChart.getDescription();
        description.setText("...");
        candleChart.setBackgroundColor(getResources().getColor(R.color.bckgrnd_color));
        candleChart.getXAxis().setPosition(XAxisPosition.BOTTOM_INSIDE);

        radioGroupTimeFrame = returnView.findViewById(R.id.radioGroupTimeFrame);
        imgButton = returnView.findViewById(R.id.btnChartInfo);
        setImageButtonInfoListener();
        setRadioGroupListener();
        getSymbols();

        return returnView;
    }
    private synchronized void loadChart(String chosenSymbol, String chosenTimeFrame){
        if(!isRunning) {
            isRunning = true;
            candleChart.invalidate();
            candleChart.notifyDataSetChanged();
            lKlines.clear();
            candleChart.clear();
            dates.clear();
            arrayCandles.clear();
            redCandleCount = 0;
            greenCandleCount = 0;
            equalCandleCount = 0;
            Call<List<Object>> call = RetrofitClient.getInstance().getMyApi().getKlines(chosenSymbol, chosenTimeFrame);
            call.enqueue(new Callback<List<Object>>() {
                @Override
                public void onResponse(@NonNull Call<List<Object>> call, @NonNull Response<List<Object>> response) {
                    List<Object> myKlineList = response.body();
                    assert myKlineList != null;
                    for (Object kline_o : myKlineList) {
                        Klines oKline = new Klines();
                        ArrayList lPodaci = (ArrayList) kline_o;
                        oKline.setOpen_time(lPodaci.get(0).toString());
                        oKline.setOpen(lPodaci.get(1).toString());
                        oKline.setHigh(lPodaci.get(2).toString());
                        oKline.setLow(lPodaci.get(3).toString());
                        oKline.setClose(lPodaci.get(4).toString());
                        oKline.setVolume(lPodaci.get(5).toString());
                        oKline.setClose_time(lPodaci.get(6).toString());
                        oKline.setQuote_asset_volume(lPodaci.get(7).toString());
                        oKline.setNumber_of_trades(lPodaci.get(8).toString());
                        oKline.setTaker_buy_base_asset_volume(lPodaci.get(9).toString());
                        oKline.setTaker_buy_quote_asset_volume(lPodaci.get(10).toString());
                        oKline.setIgnore(lPodaci.get(11).toString());
                        if (lKlines.size() > 1) {
                            if (Float.parseFloat(oKline.getClose()) > Float.parseFloat(lKlines.get(lKlines.size() - 1).getClose())) {
                                greenCandleCount += 1;
                            }
                            if (Float.parseFloat(oKline.getClose()) < Float.parseFloat(lKlines.get(lKlines.size() - 1).getClose())) {
                                redCandleCount += 1;
                            }
                            if (Float.parseFloat(oKline.getClose()) == Float.parseFloat(lKlines.get(lKlines.size() - 1).getClose())) {
                                equalCandleCount += 1;
                            }

                        } else if (lKlines.size() == 1) {
                            greenCandleCount += 1;
                        }
                        lKlines.add(oKline);
                    }
                    ArrayList<String> dates = new ArrayList<>();
                    ArrayList<CandleEntry> arrayCandles = new ArrayList<>();
                    for (int i = 0; i < lKlines.size(); i++) {
                        // x os
                        dates.add(lKlines.get(i).getClose_time());
                        // y os
                        arrayCandles.add(new CandleEntry(
                                i,
                                Float.parseFloat(lKlines.get(i).getHigh()),
                                Float.parseFloat(lKlines.get(i).getLow()),
                                Float.parseFloat(lKlines.get(i).getOpen()),
                                Float.parseFloat(lKlines.get(i).getClose())

                        ));
                    }

                    CandleDataSet dataSetCandle = new CandleDataSet(arrayCandles, "Svijeće");

                    dataSetCandle.setColor(Color.rgb(80, 80, 80));
                    dataSetCandle.setShadowColor(getResources().getColor(R.color.purple_700));
                    dataSetCandle.setShadowWidth(1f);
                    dataSetCandle.setDecreasingColor(Color.rgb(224, 8, 4));
                    dataSetCandle.setDecreasingPaintStyle(Paint.Style.FILL);

                    dataSetCandle.setIncreasingColor(Color.rgb(156, 212, 60));
                    dataSetCandle.setIncreasingPaintStyle(Paint.Style.FILL);

                    CandleData candleData = new CandleData(dataSetCandle);
                    candleData.setValueTextSize(12f);

                    //candleChart.getXAxis().setDrawGridLines(false);

                    ValueFormatter formatter = new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            if ((int) value < 0 && (int) value > dates.size() - 1) {
                                return "";
                            }
                            return dates.get((int) value);
                        }
                    };
                    candleChart.setData(candleData);
                    XAxis xAxis = candleChart.getXAxis();
                    xAxis.setValueFormatter(formatter);
                    xAxis.setLabelCount(1);
                    candleChart.invalidate();
                    candleChart.notifyDataSetChanged();
                    isRunning=false;
                    candleChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {
                            int entry_index = Math.round(e.getX());
                            String date = dates.get(entry_index);
                            Klines oKline = lKlines.get(entry_index);
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getString(R.string.alert_dialog_candle_info_title))
                                    .setMessage(
                                            getString(R.string.date) + " " + date + makeNewLine +
                                                    getString(R.string.open) + " " + Float.parseFloat(oKline.getOpen()) + " USD" + makeNewLine +
                                                    getString(R.string.high) + " " + Float.parseFloat(oKline.getHigh()) + " USD" + makeNewLine +
                                                    getString(R.string.low) + " " + Float.parseFloat(oKline.getLow()) + " USD" + makeNewLine +
                                                    getString(R.string.close) + " " + Float.parseFloat(oKline.getClose()) + " USD" + makeNewLine +
                                                    getString(R.string.volume) + " " + Float.parseFloat(oKline.getVolume()) * 1000 + " USD" + makeNewLine +
                                                    getString(R.string.date_info) + makeNewLine + getString(R.string.open_info) + makeNewLine +
                                                    getString(R.string.high_info) + makeNewLine + getString(R.string.low_info) + makeNewLine +
                                                    getString(R.string.close_info) + makeNewLine + getString(R.string.volume_info) + makeNewLine
                                    )

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(getString(R.string.close_alert_dialog_candle_info), null)
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });
                }

                @Override
                public void onFailure(@NonNull Call<List<Object>> call, @NonNull Throwable t) {
                    Log.d("Error", "error");
                }
            });
        }
    }
    private void getSymbols(){
        SearchableSpinner dropdown = (SearchableSpinner) returnView.findViewById(R.id.spinnerChartPairs);
        dropdown.setTitle(getString(R.string.choose_pair));
        Call<SymbolResponse> call = RetrofitClient.getInstance().getMyApi().getSymbols();
        call.enqueue(new Callback<SymbolResponse>() {
            @Override
            public void onResponse(@NonNull Call<SymbolResponse> call, @NonNull Response<SymbolResponse> response) {
                SymbolResponse mySymbolList = response.body();
                assert mySymbolList != null;
                allSymbols = mySymbolList.getSymbols();
                List<String> items = new ArrayList<>();
                for (Symbol oSymbol : allSymbols){
                    if(oSymbol.getSymbol().contains("USDT")) {
                        items.add(oSymbol.getSymbol());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(returnView.getContext(), android.R.layout.simple_list_item_1, items);
                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        chosenSymbol = adapterView.getItemAtPosition(i).toString();
                        loadChart(chosenSymbol, chosenTimeFrame);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                dropdown.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<SymbolResponse> call, @NonNull Throwable t) {
                Log.d("Error", "error");
            }
        });
    }
    private void setRadioGroupListener(){
        radioGroupTimeFrame.setOnCheckedChangeListener((radioGroup, i) -> {
            checkedRadioBtn = returnView.findViewById(radioGroupTimeFrame.getCheckedRadioButtonId());
            chosenTimeFrame = (String) checkedRadioBtn.getText();
            loadChart(chosenSymbol, chosenTimeFrame);
        });
    }
    private void setImageButtonInfoListener(){
        imgButton.setOnClickListener(view -> new AlertDialog.Builder(getContext()).
                setTitle(getString(R.string.chart_info)).
                setMessage(
                        getString(R.string.chosen_symbol) + " " + chosenSymbol + makeNewLine +
                                getString(R.string.chosen_timeframe) + " " + chosenTimeFrame + makeNewLine +
                                getString(R.string.chart_candle_amount) + " " + lKlines.size() + makeNewLine +
                                getString(R.string.red_candle_count) + " " + redCandleCount + makeNewLine +
                                getString(R.string.green_candle_count) + " " + greenCandleCount + makeNewLine +
                                getString(R.string.equal_candle_count) + " " + equalCandleCount
                )
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(getString(R.string.close_alert_dialog_candle_info), null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show());
    }
}