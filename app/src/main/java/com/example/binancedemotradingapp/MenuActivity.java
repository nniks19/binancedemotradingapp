package com.example.binancedemotradingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                String login_message = extras.getString("login_success");
                Toast.makeText(MenuActivity.this, login_message, Toast.LENGTH_SHORT);
            }
        }
    }
}