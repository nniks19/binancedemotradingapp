package com.example.binancedemotradingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binancedemotradingapp.Models.SymbolPrice;
import com.example.binancedemotradingapp.Models.Trade;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeAdapter extends  RecyclerView.Adapter<TradeAdapter.tradeViewHolder>{
    ArrayList<Trade> mTradeList;
    Context context;
    private final TradeOnClickListener listener;
    public String current_price;
    String userId;

    public TradeAdapter(Context ct, ArrayList<Trade> tl, TradeOnClickListener tradelistener){
        context = ct;
        mTradeList = tl;
        this.listener = tradelistener;
    }
    @NonNull
    @Override
    public tradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trade_row, parent, false);
        return new tradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tradeViewHolder holder, int position) {
        holder.txtViewTradeAmount.setText(mTradeList.get(holder.getAdapterPosition()).getsAmount());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(mTradeList.get(holder.getAdapterPosition()).getlTimestamp());
        holder.txtViewTradeDate.setText(format.format(date));
        holder.txtViewTradeSymbol.setText(mTradeList.get(holder.getAdapterPosition()).getsPair());
        if(mTradeList.get(holder.getAdapterPosition()).getsStatus().equals("active")){
            holder.tradeConstraintLayout.setBackgroundColor(Color.rgb(0,255,0));
            holder.cardViewTrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<List<SymbolPrice>> symbolPriceCall = RetrofitClient.getInstance().getMyApi().getSymbolPrices();
                    symbolPriceCall.enqueue(new Callback<List<SymbolPrice>>() {
                        @Override
                        public void onResponse(Call<List<SymbolPrice>> call, Response<List<SymbolPrice>> response) {
                            List<SymbolPrice> mySymbolPriceList = response.body();
                            assert mySymbolPriceList != null;
                            for (SymbolPrice oSymbolPrice : mySymbolPriceList) {
                                if (oSymbolPrice.getSymbol().equals(mTradeList.get(holder.getAdapterPosition()).getsPair())) {
                                    current_price = oSymbolPrice.getPrice();
                                }
                            }
                            makeAlertDialog(view,holder,format,date);
                        }

                        @Override
                        public void onFailure(Call<List<SymbolPrice>> call, Throwable t) {

                        }
                    });

                }
            });
        }
        if(mTradeList.get(holder.getAdapterPosition()).getsStatus().equals("closed")){
            holder.cardViewTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference();
                myRef.child("trades").child(userId).child(mTradeList.get(holder.getAdapterPosition()).getsKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Trade oTrade = dataSnapshot.getValue(Trade.class);
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage(view.getContext().getString(R.string.trade_buy_date) + " " + format.format(date) + "\n"
                                    + view.getContext().getString(R.string.trade_pair) + " " + mTradeList.get(holder.getAdapterPosition()).getsPair() + "\n"
                                    + view.getContext().getString(R.string.trade_investment) + " " + mTradeList.get(holder.getAdapterPosition()).getsAmount() + "\n"
                                    + view.getContext().getString(R.string.trade_amount) + " " + Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsAmount()) / Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsBuy_price()) +  " " + mTradeList.get(holder.getAdapterPosition()).getsPair().replace("USDT","") + "\n"
                                    + view.getContext().getString(R.string.trade_bought_price) + " " + Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsBuy_price()) +"\n"
                                    + view.getContext().getString(R.string.trade_sold_price) + " " + Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsSell_price()) + "\n"
                                    + view.getContext().getString(R.string.trade_sell_price_red) + " " + Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsProfit()) + " USD"
                            ).setNegativeButton(R.string.close_buy_fragment, (dialog, id) -> {
                                dialog.cancel();
                            }).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.tradeConstraintLayout.setBackgroundColor(Color.rgb(255, 0, 0));
        }


    }

    @Override
    public int getItemCount() {
        final int size = mTradeList.size();
        return size;
    }
    public void clear() {
        int size = mTradeList.size();
        mTradeList.clear();
        notifyItemRangeRemoved(0, size);
    }
    public class tradeViewHolder extends RecyclerView.ViewHolder{
        TextView txtViewTradeDate;
        TextView txtViewTradeSymbol;
        TextView txtViewTradeAmount;
        public CardView cardViewTrade;
        ConstraintLayout tradeConstraintLayout;
        public tradeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewTradeAmount = itemView.findViewById(R.id.txtViewTradeAmount);
            txtViewTradeSymbol = itemView.findViewById(R.id.txtViewTradeSymbol);
            txtViewTradeDate = itemView.findViewById(R.id.txtViewTradeDate);
            cardViewTrade = itemView.findViewById(R.id.cardViewTrade);
            tradeConstraintLayout = itemView.findViewById(R.id.tradeConstraintLayout);
        }


    }
    public void makeAlertDialog(View view,@NonNull tradeViewHolder holder,SimpleDateFormat format, Date date){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(view.getContext());
        Float razlika_cijena;
        razlika_cijena = Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsAmount()) / Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsBuy_price()) * Float.parseFloat(current_price);
        builder.setMessage(view.getContext().getString(R.string.trade_buy_date) + " " + format.format(date) + "\n"
                + view.getContext().getString(R.string.trade_pair) + " " + mTradeList.get(holder.getAdapterPosition()).getsPair() + "\n"
                + view.getContext().getString(R.string.trade_investment) + " " + mTradeList.get(holder.getAdapterPosition()).getsAmount() + "\n"
                + view.getContext().getString(R.string.trade_amount) + " " + Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsAmount()) / Float.parseFloat(mTradeList.get(holder.getAdapterPosition()).getsBuy_price())+ " " + mTradeList.get(holder.getAdapterPosition()).getsPair().replace("USDT","") + "\n"
                + view.getContext().getString(R.string.trade_current_price) + " " + Float.parseFloat(current_price) + " USD" +"\n"
                + "\n"
                + view.getContext().getString(R.string.trade_sell_price) + " " + razlika_cijena + " USD"
                )
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference();
                    myRef.child("wallets").child(userId + "_1").child("oCurrency").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Float fCurrency_Amount = Float.parseFloat(Objects.requireNonNull(snapshot.child("dCurrency_amount").getValue()).toString());
                            myRef.child("wallets").child(userId + "_1").child("oCurrency").child("dCurrency_amount").setValue(fCurrency_Amount + razlika_cijena);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    DatabaseReference mySetRef = database.getReference().child("trades").child(userId).child(mTradeList.get(holder.getAdapterPosition()).getsKey());
                    Trade oTradeSet = mTradeList.get(holder.getAdapterPosition());
                    oTradeSet.setsStatus("closed");
                    oTradeSet.setsSell_price(current_price);
                    oTradeSet.setsProfit(razlika_cijena.toString());
                    mySetRef.setValue(oTradeSet);
                    Toast.makeText(view.getContext(), view.getContext().getString(R.string.sell_success),
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.no, (dialog, id) -> {
                    dialog.cancel();
                    Toast.makeText(view.getContext(), view.getContext().getString(R.string.cancel_sell),
                            Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(view.getContext(), view.getContext().getString(R.string.time_expired_sell),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();
            }
        });

        alert.setTitle(view.getContext().getString(R.string.info_sell_title));
        alert.show();
    }
    public void setUserId(String loggedid){
        userId =loggedid;
    }
}
